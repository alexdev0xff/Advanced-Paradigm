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

// Импортируй свои блоки (путь может отличаться)
import alexdev0xff.advancedparadigm.Blocks.ModBlocks;

public class ModConfiguredFeatures {
    // Ключ, по которому PlacedFeatures найдет этот конфиг


    public static final ResourceKey<ConfiguredFeature<?, ?>> TIN_ORE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "tin_ore")
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        // 1. Правила: что мы можем заменять (камень и глубинный сланец)

        TagMatchTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        TagMatchTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);



        List<OreConfiguration.TargetBlockState> tinTargets = List.of(
                OreConfiguration.target(stoneReplaceables, ModBlocks.TIN_ORE.defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, ModBlocks.DEEPSLATE_TIN_ORE.defaultBlockState())
        );



        // 3. Регистрация конфига:
        context.register(TIN_ORE_KEY, new ConfiguredFeature<>(Feature.ORE,
                new OreConfiguration(tinTargets, 64))); // Размер 64 — настоящая жила GregTech
    }
}