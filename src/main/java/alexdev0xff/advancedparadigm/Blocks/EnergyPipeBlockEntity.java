package alexdev0xff.advancedparadigm.Blocks;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

public class EnergyPipeBlockEntity extends BlockEntity {
    private static final long CAPACITY = 1_000;
    private static final long MAX_INSERT = 25600;
    private static final long MAX_EXTRACT = 25600;
    private static final long TRANSFER_PER_TICK = 2560;

    public final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY, MAX_INSERT, MAX_EXTRACT) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    public EnergyPipeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENERGY_PIPE, pos, state);
    }

    // ← @Override удаляем
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putLong("Energy", energyStorage.amount);
    }

    // ← @Override удаляем
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        energyStorage.amount = Math.max(0, Math.min(CAPACITY, input.getLongOr("Energy", 0)));
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnergyPipeBlockEntity blockEntity) {
        if (level.isClientSide()) return;
        alexdev0xff.advancedparadigm.Energy.EnergyNetwork.pushFromPipeNetwork(
                level,
                pos,
                blockEntity.energyStorage,
                TRANSFER_PER_TICK
        );
    }
}