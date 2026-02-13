package alexdev0xff.advancedparadigm;

import alexdev0xff.advancedparadigm.Blocks.ModBlockEntities;
import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import alexdev0xff.advancedparadigm.Blocks.EnergyCellMenu;
import alexdev0xff.advancedparadigm.Blocks.ElectricFurnaceMenu;
import alexdev0xff.advancedparadigm.Blocks.TestBlockMenu;
import alexdev0xff.advancedparadigm.Items.ModItems;
import alexdev0xff.advancedparadigm.ModMaterial.ModMaterial;
import alexdev0xff.advancedparadigm.ModOre.ModPlacedFeatures;
import alexdev0xff.advancedparadigm.ModRecipe.ExampleModRecipeProvider;
import alexdev0xff.advancedparadigm.ModTabs.ModTabs;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancedParadigm implements ModInitializer {
	public static final String MOD_ID = "advancedparadigm";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MenuType<TestBlockMenu> TEST_BLOCK_MENU = Registry.register(
			BuiltInRegistries.MENU,
			Identifier.fromNamespaceAndPath(MOD_ID, "test_block"),
			new MenuType<>(TestBlockMenu::new, FeatureFlags.DEFAULT_FLAGS)
	);

	public static final MenuType<EnergyCellMenu> ENERGY_CELL_MENU = Registry.register(
			BuiltInRegistries.MENU,
			Identifier.fromNamespaceAndPath(MOD_ID, "energy_cell"),
			new MenuType<>(EnergyCellMenu::new, FeatureFlags.DEFAULT_FLAGS)
	);

	public static final MenuType<ElectricFurnaceMenu> ELECTRIC_FURNACE_MENU = Registry.register(
			BuiltInRegistries.MENU,
			Identifier.fromNamespaceAndPath(MOD_ID, "electric_furnace"),
			new MenuType<>(ElectricFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS)
	);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModTabs.initialize();
		ExampleModRecipeProvider.initialize();
		ModMaterial.initialize();




		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ModPlacedFeatures.TIN_ORE_PLACED_KEY
		);
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ModPlacedFeatures.LEAD_ORE_PLACED_KEY
		);
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ModPlacedFeatures.SILVER_ORE_PLACED_KEY
		);
		BiomeModifications.addFeature(
				BiomeSelectors.foundInOverworld(),
				GenerationStep.Decoration.UNDERGROUND_ORES,
				ModPlacedFeatures.URANIUM_ORE_PLACED_KEY
		);

	}

}
