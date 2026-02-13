package alexdev0xff.advancedparadigm.Blocks;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {
    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.setId(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, name));
    }

    // test_block: блок-машина, открывает меню по ПКМ
    public static final Block TEST_BLOCK = register(
            "test_block",
            TestBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.0F),
            true
    );


   //ORE


    public static final Block TIN_ORE = register(
            "tin_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(1, 3),
                    settings
            ),
            // Вот здесь настраивается прочность:
            // Первый параметр (3.0f) — твердость, второй (3.0f) — сопротивление взрыву
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 3.0f).requiresCorrectToolForDrops(),
            true
    );

    public static final Block DEEPSLATE_TIN_ORE = register(
            "deepslate_tin_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(5, 3),
                    settings
            ),
            // Для сланцевой руды в GTNH прочность обычно выше (4.5f)
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).strength(1.5f, 3.0f).requiresCorrectToolForDrops(),
            true
    );


    public static final Block LEAD_ORE = register(
            "lead_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(1, 3),
                    settings
            ),
            // Вот здесь настраивается прочность:
            // Первый параметр (3.0f) — твердость, второй (3.0f) — сопротивление взрыву
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 3.0f).requiresCorrectToolForDrops(),
            true
    );

    public static final Block DEEPSLATE_LEAD_ORE = register(
            "deepslate_lead_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(5, 3),
                    settings
            ),
            // Для сланцевой руды в GTNH прочность обычно выше (4.5f)
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).strength(1.5f, 3.0f).requiresCorrectToolForDrops(),
            true
    );
    public static final Block SILVER_ORE = register(
            "silver_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(1, 3),
                    settings
            ),
            // Вот здесь настраивается прочность:
            // Первый параметр (3.0f) — твердость, второй (3.0f) — сопротивление взрыву
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 3.0f).requiresCorrectToolForDrops(),
            true
    );

    public static final Block SILVER_LEAD_ORE = register(
            "silver_lead_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(5, 3),
                    settings
            ),
            // Для сланцевой руды в GTNH прочность обычно выше (4.5f)
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).strength(1.5f, 3.0f).requiresCorrectToolForDrops(),
            true
    );

    public static final Block URANIUM_ORE = register(
            "uranium_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(1, 3),
                    settings
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(5.0f, 3.0f).requiresCorrectToolForDrops(),
            true
    );

    public static final Block DEEPSLATE_URANIUM_ORE = register(
            "deepslate_uranium_ore",
            settings -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(1, 3),
                    settings
            ),
            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).strength(1.5f, 3.0f).requiresCorrectToolForDrops(),
            true
    );

    public static final Block ENERGY_PIPE = register(
            "energy_pipe",
            EnergyPipeBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(1.0F).noOcclusion(),
            true
    );

    public static final Block ENERGY_CELL = register(
            "energy_cell",
            EnergyCellBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.0F).noOcclusion(),
            true
    );

    public static final Block ELECTRIC_FURNACE = register(
            "electric_furnace",
            ElectricFurnaceBlock::new,
            // Do not copy vanilla furnace properties: it carries a lightLevel function that reads the LIT property.
            // Our block state doesn't define LIT, so that would crash at runtime.
            BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(3.5F),
            true
    );


    public static void initialize() {

    }

}
