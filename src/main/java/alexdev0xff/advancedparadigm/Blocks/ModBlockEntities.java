package alexdev0xff.advancedparadigm.Blocks;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;

public class ModBlockEntities {

    public static final BlockEntityType<TestBlockEntity> TEST_BLOCK = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "test_block"),
            FabricBlockEntityTypeBuilder.create(TestBlockEntity::new, ModBlocks.TEST_BLOCK).build()
    );

    public static final BlockEntityType<EnergyPipeBlockEntity> ENERGY_PIPE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "energy_pipe"),
            FabricBlockEntityTypeBuilder.create(EnergyPipeBlockEntity::new, ModBlocks.ENERGY_PIPE).build()
    );

    public static final BlockEntityType<EnergyCellBlockEntity> ENERGY_CELL = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "energy_cell"),
            FabricBlockEntityTypeBuilder.create(EnergyCellBlockEntity::new, ModBlocks.ENERGY_CELL).build()
    );

    public static final BlockEntityType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "electric_furnace"),
            FabricBlockEntityTypeBuilder.create(ElectricFurnaceBlockEntity::new, ModBlocks.ELECTRIC_FURNACE).build()
    );

    public static void initialize() {
        // Expose energy storage to other blocks/items via TechReborn Energy API.
        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                TEST_BLOCK
        );

        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                ENERGY_PIPE
        );

        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                ENERGY_CELL
        );

        EnergyStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> blockEntity.energyStorage,
                ELECTRIC_FURNACE
        );
    }
}
