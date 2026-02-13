package alexdev0xff.advancedparadigm.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.Direction;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class TestBlockEntity extends BlockEntity implements MenuProvider, Container {

    private static final int SLOT_FUEL = 0;

    private static final long ENERGY_CAPACITY = 100_000;
    private static final long ENERGY_MAX_INSERT = 0;
    private static final long ENERGY_MAX_EXTRACT = 256;
    private static final long ENERGY_PER_TICK = 20;

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(
            ENERGY_CAPACITY,
            ENERGY_MAX_INSERT,
            ENERGY_MAX_EXTRACT
    ) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    private int burnTime;
    private int burnTimeTotal;

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> burnTime;
                case 1 -> burnTimeTotal;
                case 2 -> (int) energyStorage.amount;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> burnTime = value;
                case 1 -> burnTimeTotal = value;
                case 2 -> energyStorage.amount = Math.max(0, Math.min(ENERGY_CAPACITY, (long) value));
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEST_BLOCK, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Test Block");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new TestBlockMenu(syncId, playerInventory, this, dataAccess);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Energy", energyStorage.amount);
        output.putInt("BurnTime", burnTime);
        output.putInt("BurnTimeTotal", burnTimeTotal);
        output.store("Fuel", ItemStack.CODEC, items.get(SLOT_FUEL));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.amount = Math.max(0, Math.min(ENERGY_CAPACITY, input.getLongOr("Energy", 0)));
        burnTime = Math.max(0, input.getIntOr("BurnTime", 0));
        burnTimeTotal = Math.max(0, input.getIntOr("BurnTimeTotal", 0));
        items.set(SLOT_FUEL, input.read("Fuel", ItemStack.CODEC).orElse(ItemStack.EMPTY));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TestBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        long energy = blockEntity.energyStorage.amount;
        boolean changed = false;

        // Если накопитель полный - "пауза" горения, чтобы не сжигать топливо впустую.
        if (energy < ENERGY_CAPACITY) {
            if (blockEntity.burnTime > 0) {
                blockEntity.burnTime--;
                blockEntity.energyStorage.amount = Math.min(ENERGY_CAPACITY, energy + ENERGY_PER_TICK);
                changed = true;
            } else {
                ItemStack fuel = blockEntity.items.get(SLOT_FUEL);
                int burn = getFuelBurnTime(fuel);
                if (burn > 0) {
                    blockEntity.burnTime = burn;
                    blockEntity.burnTimeTotal = burn;
                    fuel.shrink(1);
                    changed = true;
                }
            }
        }

        if (changed) {
            blockEntity.setChanged();
        }

        // Источник энергии должен "пушить" энергию в соседей (pipe/cell/machines).
        pushEnergy(level, pos, blockEntity.energyStorage, ENERGY_MAX_EXTRACT);
    }

    private static void pushEnergy(Level level, BlockPos pos, SimpleEnergyStorage from, long maxPerTick) {
        if (from.amount <= 0) {
            return;
        }

        long remaining = Math.min(maxPerTick, from.amount);
        for (Direction dir : Direction.values()) {
            if (remaining <= 0) {
                break;
            }

            EnergyStorage target = EnergyStorage.SIDED.find(level, pos.relative(dir), dir.getOpposite());
            if (target == null || !target.supportsInsertion()) {
                continue;
            }

            try (Transaction tx = Transaction.openOuter()) {
                long inserted = target.insert(remaining, tx);
                if (inserted <= 0) {
                    continue;
                }

                long extracted = from.extract(inserted, tx);
                if (extracted == inserted) {
                    tx.commit();
                    remaining -= inserted;
                }
            }
        }
    }

    public static boolean isFuel(ItemStack stack) {
        return getFuelBurnTime(stack) > 0;
    }

    private static int getFuelBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        if (stack.is(Items.COAL) || stack.is(Items.CHARCOAL)) {
            return 1600;
        }
        // "Угольный блок" в ваниле - это BlockItem.
        if (stack.is(Blocks.COAL_BLOCK.asItem())) {
            return 16000;
        }
        return 0;
    }

    // --- Container implementation (1 fuel slot) ---

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack existing = items.get(slot);
        if (existing.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack split = existing.split(amount);
        if (!split.isEmpty()) {
            setChanged();
        }
        return split;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack existing = items.get(slot);
        items.set(slot, ItemStack.EMPTY);
        return existing;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null) {
            return false;
        }
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                worldPosition.getX() + 0.5D,
                worldPosition.getY() + 0.5D,
                worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < items.size(); i++) {
            items.set(i, ItemStack.EMPTY);
        }
        setChanged();
    }
}
