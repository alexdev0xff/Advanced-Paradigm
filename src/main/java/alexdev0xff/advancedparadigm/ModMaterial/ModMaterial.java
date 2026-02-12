package alexdev0xff.advancedparadigm.ModMaterial;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

public class ModMaterial {

    public static final TagKey<Item> REPAIRS_GUIDITE_ARMOR = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "repairs_guidite_armor")
    );

    public static final ToolMaterial TIN_TIER = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            455,
            5.0F,
            1.5F,
            22,
            REPAIRS_GUIDITE_ARMOR
    );

    public static final ResourceKey<EquipmentAsset> TIN_EQUIPMENT_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(AdvancedParadigm.MOD_ID, "tin")
    );

    public static final ArmorMaterial TIN_ARMOR_MATERIAL = new ArmorMaterial(
            15,
            Map.of(
                    ArmorType.HELMET, 2,
                    ArmorType.CHESTPLATE, 5,
                    ArmorType.LEGGINGS, 4,
                    ArmorType.BOOTS, 1
            ),
            12,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.0F,
            0.0F,
            REPAIRS_GUIDITE_ARMOR,
            TIN_EQUIPMENT_ASSET
    );

    public static void initialize() {
    }
}