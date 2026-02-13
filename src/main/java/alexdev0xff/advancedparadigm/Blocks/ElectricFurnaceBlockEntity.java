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
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class ElectricFurnaceBlockEntity extends BlockEntity implements MenuProvider, Container {
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;

    private static final long ENERGY_CAPACITY = 20_000;
    private static final long ENERGY_MAX_INSERT = 2560;
    private static final long ENERGY_MAX_EXTRACT = 0;

    private static final long ENERGY_PER_TICK = 16;
    private static final int COOK_TIME_TOTAL = 20;

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

    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private int cookTime;

    private final ContainerData dataAccess = new SimpleContainerData(3) {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> cookTime;
                case 1 -> COOK_TIME_TOTAL;
                case 2 -> (int) energyStorage.amount;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> cookTime = value;
                case 2 -> energyStorage.amount = Math.max(0, Math.min(ENERGY_CAPACITY, (long) value));
                default -> {
                }
            }
        }
    };

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_FURNACE, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Electric Furnace");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ElectricFurnaceMenu(syncId, playerInventory, this, dataAccess);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Energy", energyStorage.amount);
        output.putInt("CookTime", cookTime);
        output.store("Input", ItemStack.CODEC, items.get(SLOT_INPUT));
        output.store("Output", ItemStack.CODEC, items.get(SLOT_OUTPUT));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.amount = Math.max(0, Math.min(ENERGY_CAPACITY, input.getLongOr("Energy", 0)));
        cookTime = Math.max(0, input.getIntOr("CookTime", 0));
        items.set(SLOT_INPUT, input.read("Input", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        items.set(SLOT_OUTPUT, input.read("Output", ItemStack.CODEC).orElse(ItemStack.EMPTY));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ElectricFurnaceBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        boolean changed = false;

        RecipeHolder<SmeltingRecipe> recipe = blockEntity.findRecipe(level);
        boolean canSmelt = recipe != null && blockEntity.canSmelt(recipe.value());

        if (canSmelt && blockEntity.energyStorage.amount >= ENERGY_PER_TICK) {
            blockEntity.cookTime++;
            blockEntity.energyStorage.amount -= ENERGY_PER_TICK;
            changed = true;

            if (blockEntity.cookTime >= COOK_TIME_TOTAL) {
                blockEntity.cookTime = 0;
                blockEntity.doSmelt(recipe.value());
                changed = true;
            }
        } else {
            if (blockEntity.cookTime != 0) {
                blockEntity.cookTime = 0;
                changed = true;
            }
        }

        if (changed) {
            blockEntity.setChanged();
        }
    }

    private RecipeHolder<SmeltingRecipe> findRecipe(Level level) {
        ItemStack input = items.get(SLOT_INPUT);
        if (input.isEmpty()) {
            return null;
        }

        if (level.getServer() == null) {
            return null;
        }
        return level.getServer()
                .getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(input), level)
                .orElse(null);
    }

    private boolean canSmelt(SmeltingRecipe recipe) {
        ItemStack input = items.get(SLOT_INPUT);
        if (input.isEmpty()) {
            return false;
        }

        ItemStack result = recipe.assemble(new SingleRecipeInput(input), level.registryAccess());
        if (result.isEmpty()) {
            return false;
        }

        ItemStack output = items.get(SLOT_OUTPUT);
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(output, result)) {
            return false;
        }
        return output.getCount() + result.getCount() <= output.getMaxStackSize();
    }

    private void doSmelt(SmeltingRecipe recipe) {
        ItemStack input = items.get(SLOT_INPUT);
        if (input.isEmpty()) {
            return;
        }

        ItemStack result = recipe.assemble(new SingleRecipeInput(input), level.registryAccess());
        if (result.isEmpty()) {
            return;
        }

        ItemStack output = items.get(SLOT_OUTPUT);
        if (output.isEmpty()) {
            items.set(SLOT_OUTPUT, result.copy());
        } else if (ItemStack.isSameItemSameComponents(output, result)) {
            output.grow(result.getCount());
        }

        input.shrink(1);
    }

    // --- Container impl ---

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
