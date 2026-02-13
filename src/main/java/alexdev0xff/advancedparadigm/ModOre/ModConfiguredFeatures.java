package alexdev0xff.advancedparadigm.ModOre;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import java.util.List;
import alexdev0xff.advancedparadigm.Blocks.ModBlocks;

public class ModConfiguredFeatures {
    // Ключ, по которому PlacedFeatures найдет этот конфиг


    public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "tin_ore")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAD_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "lead_ore")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "silver_ore")
    );
    public static final ResourceKey<ConfiguredFeature<?, ?>> URANIUM_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "uranium_ore")
    );


    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // 1. Правила: что мы можем заменять (камень и глубинный сланец)

        TagMatchTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);



        List<OreConfiguration.TargetBlockState> tinTargets = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.TIN_ORE.defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_TIN_ORE.defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> leadTargets = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.LEAD_ORE.defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_LEAD_ORE.defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> silverTargets = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.SILVER_ORE.defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.SILVER_LEAD_ORE.defaultBlockState())
        );
        List<OreConfiguration.TargetBlockState> uraniumTargets = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.URANIUM_ORE.defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_URANIUM_ORE.defaultBlockState())
        );



        // ConfiguredFeature задает "что генерим" и "размер жилы" (кол-во блоков в одной попытке генерации).
        // Делаем более ванильные размеры: олово примерно как железо (чуть меньше), свинец заметно реже.
        context.register(TIN_ORE_KEY, new ConfiguredFeature<>(
                Feature.ORE,
                new OreConfiguration(tinTargets, 8)
        ));

        context.register(LEAD_ORE_KEY, new ConfiguredFeature<>(
                Feature.ORE,
                new OreConfiguration(leadTargets, 6)
        ));

        context.register(SILVER_ORE_KEY, new ConfiguredFeature<>(
                Feature.ORE,
                new OreConfiguration(silverTargets, 7)
        ));

        context.register(URANIUM_ORE_KEY, new ConfiguredFeature<>(
                Feature.ORE,
                new OreConfiguration(uraniumTargets, 4)
        ));
    }
}
