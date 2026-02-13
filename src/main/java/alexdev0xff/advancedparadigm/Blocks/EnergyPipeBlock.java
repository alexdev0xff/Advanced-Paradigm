package alexdev0xff.advancedparadigm.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import java.util.HashSet;
import java.util.Set;

public class EnergyPipeBlock extends Block implements EntityBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST  = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST  = BlockStateProperties.WEST;
    public static final BooleanProperty UP    = BlockStateProperties.UP;
    public static final BooleanProperty DOWN  = BlockStateProperties.DOWN;

    // Сюда добавляем все блоки, к которым труба может подключаться
    public static final Set<Block> CONNECTABLE_BLOCKS = new HashSet<>();

    static {
        CONNECTABLE_BLOCKS.add(ModBlocks.ENERGY_PIPE);
        CONNECTABLE_BLOCKS.add(ModBlocks.ENERGY_CELL);
        CONNECTABLE_BLOCKS.add(ModBlocks.ELECTRIC_FURNACE);
        CONNECTABLE_BLOCKS.add(ModBlocks.TEST_BLOCK);
        // можно добавлять новые блоки сюда
    }

    public EnergyPipeBlock(Properties properties) {
        super(properties.noOcclusion()); // noOcclusion убирает тени
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return updateConnections(ctx.getLevel(), ctx.getClickedPos(), defaultBlockState());
    }


    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelReader level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        return updateConnections(level, pos, state);
    }

    private boolean canConnect(BlockState state) {
        Block block = state.getBlock();
        return block instanceof EnergyPipeBlock
                || block instanceof EnergyCellBlock
                || block instanceof ElectricFurnaceBlock
                || block instanceof TestBlock; 
    }

    // Обновление соединений блока
    private BlockState updateConnections(LevelReader level, BlockPos pos, BlockState state) {
        for (Direction dir : Direction.values()) {
            BlockPos offset = pos.relative(dir);
            boolean connect = canConnect(level.getBlockState(offset));
            state = state.setValue(getProperty(dir), connect);
        }
        return state;
    }

    // Метод для получения свойства по направлению
    private static BooleanProperty getProperty(Direction dir) {
        return switch (dir) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST  -> EAST;
            case WEST  -> WEST;
            case UP    -> UP;
            case DOWN  -> DOWN;
        };
    }

    // Обновление соседних труб при установке
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pos.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof EnergyPipeBlock) {
                    level.setBlock(neighborPos,
                            ((EnergyPipeBlock) neighborState.getBlock())
                                    .updateConnections(level, neighborPos, neighborState), 3);
                }
            }
        }
    }

    // Сетевые вещи (BlockEntity)
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnergyPipeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        if (level.isClientSide()) return null;
        if (type != ModBlockEntities.ENERGY_PIPE) return null;

        return (lvl, pos, st, be) ->
                EnergyPipeBlockEntity.tick(lvl, pos, st, (EnergyPipeBlockEntity) be);
    }

    // Убираем тень

    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }
}