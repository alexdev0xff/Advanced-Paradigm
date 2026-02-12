package alexdev0xff.advancedparadigm.ModMaterial;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ModMaterial {



    public static final TagKey<Item> REPAIRS_GUIDITE_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "repairs_guidite_armor")
    );

    // ToolMaterial в 1.21.11 — record, создаём через конструктор:
    public static final ToolMaterial TIN_TIER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, // уровень как "железо" (что считается неправильным для дропа)
            455,                               // durability
            5.0F,                              // speed
            1.5F,                              // attackDamageBonus
            22,                                // enchantmentValue
            REPAIRS_GUIDITE_ARMOR              // repairItems (тег предметов для починки)
    );

    public static void initialize() {

    }

    // ... existing code ...
}