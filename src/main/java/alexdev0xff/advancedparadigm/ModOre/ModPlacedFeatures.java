package alexdev0xff.advancedparadigm.ModOre;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier; // Если будет ругаться, попробуй ResourceLocation
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.placement.*;
import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> RUBY_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "ruby_ore_placed")
    );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(RUBY_ORE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.RUBY_ORE_KEY),
                List.of(
                        CountPlacement.of(10),
                        InSquarePlacement.spread(),

                        HeightRangePlacement.of(
                                net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight.of(
                                        VerticalAnchor.absolute(-64),
                                        VerticalAnchor.absolute(80)
                                )
                        ),
                        BiomeFilter.biome()
                )
        ));
    }
    // В главном классе (например, AdvancedParadigm.java)
    public void onInitialize() {
        // ... остальной код ...

        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(), // Спавнить во всем обычном мире
                GenerationStep.Decoration.UNDERGROUND_ORES, // На этапе подземных руд
                ModPlacedFeatures.RUBY_ORE_PLACED_KEY // Твой ключ
        );
    }
}