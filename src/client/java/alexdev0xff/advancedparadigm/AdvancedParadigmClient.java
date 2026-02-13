package alexdev0xff.advancedparadigm;

import alexdev0xff.advancedparadigm.Blocks.ModBlocks;
import alexdev0xff.advancedparadigm.Blocks.TestBlockScreen;
import alexdev0xff.advancedparadigm.Blocks.EnergyCellScreen;
import alexdev0xff.advancedparadigm.Blocks.ElectricFurnaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.rendertype.RenderType;

public class AdvancedParadigmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(AdvancedParadigm.TEST_BLOCK_MENU, TestBlockScreen::new);
        MenuScreens.register(AdvancedParadigm.ENERGY_CELL_MENU, EnergyCellScreen::new);
        MenuScreens.register(AdvancedParadigm.ELECTRIC_FURNACE_MENU, ElectricFurnaceScreen::new);

    }
}
