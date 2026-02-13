package alexdev0xff.advancedparadigm.Blocks;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class EnergyCellBlockEntity extends BlockEntity implements MenuProvider {
    public static final long CAPACITY = 1_000_000;
    private static final long MAX_INSERT = 102004;
    private static final long MAX_EXTRACT = 102400;
    private static final long TRANSFER_PER_TICK = 1024;

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY, MAX_INSERT, MAX_EXTRACT) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    private final ContainerData dataAccess = new SimpleContainerData(1) {
        @Override
        public int get(int index) {
            if (index == 0) {
                return (int) energyStorage.amount;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                energyStorage.amount = Math.max(0, Math.min(CAPACITY, (long) value));
            }
        }
    };

    public EnergyCellBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGY_CELL, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Energy Cell");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new EnergyCellMenu(syncId, playerInventory, dataAccess);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Energy", energyStorage.amount);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.amount = Math.max(0, Math.min(CAPACITY, input.getLongOr("Energy", 0)));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyCellBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }
        pushEnergy(level, pos, blockEntity.energyStorage, TRANSFER_PER_TICK);
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
}

