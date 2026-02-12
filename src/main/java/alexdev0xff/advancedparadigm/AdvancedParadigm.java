package alexdev0xff.advancedparadigm;

import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import alexdev0xff.advancedparadigm.Items.ModItems;
import alexdev0xff.advancedparadigm.ModOre.ModPlacedFeatures;
import alexdev0xff.advancedparadigm.ModTabs.ModTabs;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry.register;

public class AdvancedParadigm implements ModInitializer {
	public static final String MOD_ID = "advancedparadigm";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModBlocks.initialize();
		ModTabs.initialize();
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ModPlacedFeatures.RUBY_ORE_PLACED_KEY
		);
		BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.TIN_ORE_PLACED_KEY);
	}

}
