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
public class TestBlockScreen extends AbstractContainerScreen<TestBlockMenu> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(
            AdvancedParadigm.MOD_ID,
            "textures/gui/test_block.png"
    );

    public TestBlockScreen(TestBlockMenu menu, Inventory inventory, Component title) {
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

        // Индикаторы рисуем поверх картинки обычной заливкой.
        // Так тебе не нужно рисовать отдельные элементы (u=176...) в PNG.

        // Burn indicator (14px tall), orange.
        int burn = menu.getBurnScaled(14);
        if (burn > 0) {
            int x0 = leftPos + 82;
            int y0 = topPos + 55 + (14 - burn);
            graphics.fill(x0, y0, x0 + 14, y0 + burn, 0xFFFFA000);
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
