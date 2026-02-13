package alexdev0xff.advancedparadigm.ModOre;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.placement.*;
import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> LEAD_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "lead_ore_placed")
    );
    public static final ResourceKey<PlacedFeature> TIN_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "tin_ore_placed")
    );
    public static final ResourceKey<PlacedFeature> SILVER_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "silver_ore_placed")
    );
    public static final ResourceKey<PlacedFeature> URANIUM_ORE_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            Identifier.fromNamespaceAndPath("advancedparadigm", "uranium_ore_placed")
    );


    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);


        context.register(TIN_ORE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.TIN_ORE_KEY),
                List.of(
                        // Примерно как железо, но чуть реже.
                        CountPlacement.of(16),
                        InSquarePlacement.spread(),

                        HeightRangePlacement.of(
                                TrapezoidHeight.of(
                                        VerticalAnchor.absolute(-24),
                                        VerticalAnchor.absolute(56)
                                )
                        ),
                        BiomeFilter.biome()
                )
        ));

        context.register(LEAD_ORE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.LEAD_ORE_KEY),
                List.of(
                        // В несколько раз реже олова.
                        CountPlacement.of(4),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.of(
                                TrapezoidHeight.of(
                                        VerticalAnchor.absolute(-64),
                                        VerticalAnchor.absolute(32)
                                )
                        ),
                        BiomeFilter.biome()
                )
        ));

        context.register(SILVER_ORE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.SILVER_ORE_KEY),
                List.of(
                        // Редче железа, примерно "между" железом и золотом.
                        CountPlacement.of(8),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.of(
                                TrapezoidHeight.of(
                                        VerticalAnchor.absolute(-32),
                                        VerticalAnchor.absolute(48)
                                )
                        ),
                        BiomeFilter.biome()
                )
        ));

        context.register(URANIUM_ORE_PLACED_KEY, new PlacedFeature(
                configuredFeatures.getOrThrow(ModConfiguredFeatures.URANIUM_ORE_KEY),
                List.of(
                        // Очень редкая руда.
                        CountPlacement.of(2),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.of(
                                TrapezoidHeight.of(
                                        VerticalAnchor.absolute(-64),
                                        VerticalAnchor.absolute(-16)
                                )
                        ),
                        BiomeFilter.biome()
                )
        ));
    }
}
