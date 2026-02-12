package alexdev0xff.advancedparadigm.ModTabs;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import alexdev0xff.advancedparadigm.Items.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public class ModTabs {
    public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "creative_tab"));
    public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.RAW_TIN))
            .title(Component.translatable("itemGroup.advancedparadigm"))
            .displayItems((params, output) -> {
                output.accept(ModItems.RAW_TIN);
                output.accept(ModBlocks.TIN_ORE);


                // And custom ItemStacks
                ItemStack stack = new ItemStack(Items.SEA_PICKLE);
                stack.set(DataComponents.ITEM_NAME, Component.literal("Pickle Rick"));
                stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("I'm pickle riiick!!"))));
                output.accept(stack);
            })
            .build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
    }
}
