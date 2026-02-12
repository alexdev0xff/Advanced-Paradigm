package alexdev0xff.advancedparadigm.ModTool;



import alexdev0xff.advancedparadigm.Items.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Consumer;

public final class ModTool {
    private ModTool() {}

    public record ToolSet(Item sword, Item pickaxe, Item axe, Item shovel, Item hoe) {}

    public static ToolSet registerToolSet(
            ToolMaterial material,
            String namePrefix,
            Consumer<Item> registrar,
            Item.Properties baseProps,
            float swordDamage, float swordSpeed,
            float pickaxeDamage, float pickaxeSpeed,
            float axeDamage, float axeSpeed,
            float shovelDamage, float shovelSpeed,
            float hoeDamage, float hoeSpeed
    ) {
        Item sword  = new Item(baseProps.sword(material, swordDamage, swordSpeed));
        Item pick   = new Item(baseProps.pickaxe(material, pickaxeDamage, pickaxeSpeed));
        Item axe    = new Item(baseProps.axe(material, axeDamage, axeSpeed));
        Item shovel = new Item(baseProps.shovel(material, shovelDamage, shovelSpeed));
        Item hoe    = new Item(baseProps.hoe(material, hoeDamage, hoeSpeed));

        registrar.accept(ModItems.register(namePrefix + "_sword", p -> sword,  new Item.Properties()));
        registrar.accept(ModItems.register(namePrefix + "_pickaxe",p -> pick,   new Item.Properties()));
        registrar.accept(ModItems.register(namePrefix + "_axe",    p -> axe,    new Item.Properties()));
        registrar.accept(ModItems.register(namePrefix + "_shovel", p -> shovel, new Item.Properties()));
        registrar.accept(ModItems.register(namePrefix + "_hoe",    p -> hoe,    new Item.Properties()));

        return new ToolSet(sword, pick, axe, shovel, hoe);
    }
}