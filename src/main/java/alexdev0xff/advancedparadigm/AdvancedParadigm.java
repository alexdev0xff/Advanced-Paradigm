package alexdev0xff.advancedparadigm;

import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import alexdev0xff.advancedparadigm.Items.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.world.item.Item;
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
	}
}