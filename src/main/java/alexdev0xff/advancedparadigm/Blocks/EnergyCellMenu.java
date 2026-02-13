package alexdev0xff.advancedparadigm.Blocks;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;

public class EnergyCellMenu extends AbstractContainerMenu {
    private static final long CAPACITY = EnergyCellBlockEntity.CAPACITY;

    private final ContainerData data;

    // Client ctor
    public EnergyCellMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new net.minecraft.world.inventory.SimpleContainerData(1));
    }

    public EnergyCellMenu(int syncId, Inventory playerInventory, ContainerData data) {
        super(AdvancedParadigm.ENERGY_CELL_MENU, syncId);
        checkContainerDataCount(data, 1);
        this.data = data;

        // Player inventory slots only (cell has no item slots)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(
                        playerInventory,
                        x + y * 9 + 9,
                        8 + x * 18,
                        84 + y * 18
                ));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        addDataSlots(this.data);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public net.minecraft.world.item.ItemStack quickMoveStack(Player player, int index) {
        return net.minecraft.world.item.ItemStack.EMPTY;
    }

    public int getEnergyScaled(int pixels) {
        int energy = data.get(0);
        if (energy <= 0) {
            return 0;
        }
        return Math.min(pixels, (int) ((long) energy * pixels / CAPACITY));
    }

    public int getEnergy() {
        return data.get(0);
    }
}

