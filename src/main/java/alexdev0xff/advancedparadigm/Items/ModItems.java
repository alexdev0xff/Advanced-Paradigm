package alexdev0xff.advancedparadigm.Items;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import alexdev0xff.advancedparadigm.ModMaterial.ModMaterial;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Function;

public class ModItems {
    public static <GenericItem extends Item> GenericItem register(String name, Function<Item.Properties, GenericItem> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, name));
        GenericItem item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    private static Item sword(ToolMaterial mat, float dmg, float spd, Item.Properties props) {
        return new Item(props.sword(mat, dmg, spd));
    }

    private static Item pickaxe(ToolMaterial mat, float dmg, float spd, Item.Properties props) {
        return new Item(props.pickaxe(mat, dmg, spd));
    }

    private static Item axe(ToolMaterial mat, float dmg, float spd, Item.Properties props) {
        return new Item(props.axe(mat, dmg, spd));
    }

    private static Item shovel(ToolMaterial mat, float dmg, float spd, Item.Properties props) {
        return new Item(props.shovel(mat, dmg, spd));
    }

    private static Item hoe(ToolMaterial mat, float dmg, float spd, Item.Properties props) {
        return new Item(props.hoe(mat, dmg, spd));
    }

    // items
    public static final Item RAW_TIN = register("raw_tin", Item::new, new Item.Properties());
    public static final Item TIN_INGOT = register("tin_ingot", Item::new, new Item.Properties());

    // tin toolset (одна схема, просто другой материал подставишь)
    public static final Item TIN_SWORD = register("tin_sword", p -> sword(ModMaterial.TIN_TIER, 1f, 1f, p), new Item.Properties());
    public static final Item TIN_PICKAXE = register("tin_pickaxe", p -> pickaxe(ModMaterial.TIN_TIER, 1f, 1f, p), new Item.Properties());
    public static final Item TIN_AXE = register("tin_axe", p -> axe(ModMaterial.TIN_TIER, 1f, 1f, p), new Item.Properties());
    public static final Item TIN_SHOVEL = register("tin_shovel", p -> shovel(ModMaterial.TIN_TIER, 1f, 1f, p), new Item.Properties());
    public static final Item TIN_HOE = register("tin_hoe", p -> hoe(ModMaterial.TIN_TIER, 1f, 1f, p), new Item.Properties());


    public static void initialize() {

    }
}