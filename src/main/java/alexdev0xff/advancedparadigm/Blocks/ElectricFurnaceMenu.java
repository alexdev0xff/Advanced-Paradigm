package alexdev0xff.advancedparadigm.Blocks;

import alexdev0xff.advancedparadigm.AdvancedParadigm;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ElectricFurnaceMenu extends AbstractContainerMenu {
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT = 1;

    private static final int MACHINE_SLOT_COUNT = 2;

    private static final int PLAYER_INV_START = MACHINE_SLOT_COUNT;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
    private static final int HOTBAR_START = PLAYER_INV_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private final Container container;
    private final ContainerData data;

    public ElectricFurnaceMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(MACHINE_SLOT_COUNT), new SimpleContainerData(3));
    }

    public ElectricFurnaceMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(AdvancedParadigm.ELECTRIC_FURNACE_MENU, syncId);
        checkContainerSize(container, MACHINE_SLOT_COUNT);
        checkContainerDataCount(data, 3);
        this.container = container;
        this.data = data;

        // input
        this.addSlot(new Slot(container, SLOT_INPUT, 56, 35));
        // output
        this.addSlot(new Slot(container, SLOT_OUTPUT, 116, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        // player inv
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

        // hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        addDataSlots(this.data);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return empty;
        }

        ItemStack stackInSlot = slot.getItem();
        ItemStack original = stackInSlot.copy();

        if (index == SLOT_INPUT || index == SLOT_OUTPUT) {
            if (!moveItemStackTo(stackInSlot, PLAYER_INV_START, HOTBAR_END, true)) {
                return empty;
            }
        } else {
            if (!moveItemStackTo(stackInSlot, SLOT_INPUT, SLOT_INPUT + 1, false)) {
                return empty;
            }
        }

        if (stackInSlot.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return original;
    }

    public int getCookTime() {
        return data.get(0);
    }

    public int getCookTimeTotal() {
        return data.get(1);
    }

    public int getEnergy() {
        return data.get(2);
    }

    public int getCookScaled(int pixels) {
        int total = getCookTimeTotal();
        if (total <= 0) {
            return 0;
        }
        return Math.min(pixels, (getCookTime() * pixels) / total);
    }

    public int getEnergyScaled(int pixels) {
        // Must match the BE capacity (20_000).
        int energy = getEnergy();
        if (energy <= 0) {
            return 0;
        }
        return Math.min(pixels, (int) ((long) energy * pixels / 20_000L));
    }
}

