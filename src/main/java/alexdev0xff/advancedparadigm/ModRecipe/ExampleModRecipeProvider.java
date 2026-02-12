package alexdev0xff.advancedparadigm.ModRecipe;

import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import alexdev0xff.advancedparadigm.Items.ModItems; // Проверь путь к твоим предметам!
import java.util.concurrent.CompletableFuture;

public class ExampleModRecipeProvider extends FabricRecipeProvider {
    public ExampleModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                // Рецепт из РУДЫ в печи
                SimpleCookingRecipeBuilder.smelting(
                                Ingredient.of(ModBlocks.TIN_ORE),
                                RecipeCategory.MISC,
                                ModItems.TIN_INGOT,
                                0.7f,
                                200
                        ).unlockedBy(getHasName(ModItems.RAW_TIN), has(ModItems.RAW_TIN))
                        // Используем exporter и создаем правильный путь
                        .save(exporter, "advancedparadigm:tin_ore_from_smelting");

                // Рецепт из РУДЫ в плавильне
                SimpleCookingRecipeBuilder.blasting(
                                Ingredient.of(ModBlocks.TIN_ORE),
                                RecipeCategory.MISC,
                                ModItems.TIN_INGOT,
                                0.7f,
                                100
                        ).unlockedBy(getHasName(ModItems.RAW_TIN), has(ModItems.RAW_TIN))
                        .save(exporter, "advancedparadigm:tin_ore_from_blasting");

                // Рецепт из СЫРОЙ РУДЫ в печи
                SimpleCookingRecipeBuilder.smelting(
                                Ingredient.of(ModItems.RAW_TIN),
                                RecipeCategory.MISC,
                                ModItems.TIN_INGOT,
                                0.7f,
                                200
                        ).unlockedBy(getHasName(ModItems.RAW_TIN), has(ModItems.RAW_TIN))
                        .save(exporter, "advancedparadigm:tin_ingot_from_smelting");

                // Рецепт из СЫРОЙ РУДЫ в плавильне
                SimpleCookingRecipeBuilder.blasting(
                                Ingredient.of(ModItems.RAW_TIN),
                                RecipeCategory.MISC,
                                ModItems.TIN_INGOT,
                                0.7f,
                                100
                        ).unlockedBy(getHasName(ModItems.RAW_TIN), has(ModItems.RAW_TIN))
                        .save(exporter, "advancedparadigm:tin_ingot_from_blasting");
            }
        };
    }



    @Override
    public String getName() {
        return "ExampleModRecipeProvider";
    }
    public static void initialize() {

    }
}