package alexdev0xff.advancedparadigm.Blocks;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

@Environment(EnvType.CLIENT)
public class ElectricFurnaceScreen extends AbstractContainerScreen<ElectricFurnaceMenu> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            AdvancedParadigm.MOD_ID,
            "textures/gui/electric_furnace.png"
    );

    public ElectricFurnaceScreen(ElectricFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE,
                leftPos,
                topPos,
                0,
                0,
                imageWidth,
                imageHeight,
                256,
                256
        );

        // Progress arrow (24x16) as a filled bar on top of the texture.
        int progress = menu.getCookScaled(24);
        if (progress > 0) {
            int x0 = leftPos + 79;
            int y0 = topPos + 34;
            graphics.fill(x0, y0, x0 + progress, y0 + 16, 0xFFFFD54A);
        }

        // Energy bar (8x50), cyan/blue.
        int energy = menu.getEnergyScaled(50);
        if (energy > 0) {
            int x0 = leftPos + 156;
            int y0 = topPos + 16 + (50 - energy);
            graphics.fill(x0, y0, x0 + 8, y0 + energy, 0xFF00B7FF);
        }
    }
}

