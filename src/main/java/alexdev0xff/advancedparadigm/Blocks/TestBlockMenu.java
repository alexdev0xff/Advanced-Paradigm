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

public class TestBlockMenu extends AbstractContainerMenu {

    private static final int SLOT_FUEL = 0;
    private static final int MACHINE_SLOT_COUNT = 1;

    private static final int PLAYER_INV_START = MACHINE_SLOT_COUNT;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
    private static final int HOTBAR_START = PLAYER_INV_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private final Container container;
    private final ContainerData data;

    // Клиентский конструктор: создаем временный контейнер и data.
    // Сервер сам пришлет содержимое слотов и значения data через синхронизацию меню.
    public TestBlockMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(MACHINE_SLOT_COUNT), new SimpleContainerData(3));
    }

    // Серверный конструктор: сюда передаем реальный контейнер из BlockEntity и ContainerData.
    public TestBlockMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
        super(AdvancedParadigm.TEST_BLOCK_MENU, syncId);
        checkContainerSize(container, MACHINE_SLOT_COUNT);
        checkContainerDataCount(data, 3);
        this.container = container;
        this.data = data;

        // Топливный слот генератора (принимает только уголь/древесный уголь/угольный блок).
        this.addSlot(new Slot(container, SLOT_FUEL, 80, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return TestBlockEntity.isFuel(stack);
            }
        });

        // Основной инвентарь игрока: 3 ряда по 9 слотов (всего 27).
        // Индексы в Inventory:
        // - 0..8   = хотбар
        // - 9..35  = основной инвентарь (то, что рисуем здесь)
        //
        // Координаты:
        // - старт X = 8, старт Y = 84
        // - шаг 18px (16px слот + 2px отступ) как в ванильных GUI.
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

        // Хотбар игрока: 1 ряд из 9 слотов (индексы 0..8).
        // Обычно в ванильных GUI он расположен на Y=142 при высоте текстуры 166.
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        // Синхронизация "burnTime / burnTimeTotal / energy" на клиент.
        this.addDataSlots(this.data);
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

        if (index == SLOT_FUEL) {
            // Из топлива -> в инвентарь игрока
            if (!moveItemStackTo(stackInSlot, PLAYER_INV_START, HOTBAR_END, true)) {
                return empty;
            }
        } else {
            // Из инвентаря игрока -> в слот топлива (только если это топливо)
            if (TestBlockEntity.isFuel(stackInSlot)) {
                if (!moveItemStackTo(stackInSlot, SLOT_FUEL, SLOT_FUEL + 1, false)) {
                    return empty;
                }
            } else {
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

    public int getBurnTime() {
        return data.get(0);
    }

    public int getBurnTimeTotal() {
        return data.get(1);
    }

    public int getEnergy() {
        return data.get(2);
    }

    public int getBurnScaled(int pixels) {
        int total = getBurnTimeTotal();
        if (total <= 0) {
            return 0;
        }
        return Math.min(pixels, (getBurnTime() * pixels) / total);
    }

    public int getEnergyScaled(int pixels) {
        int energy = getEnergy();
        if (energy <= 0) {
            return 0;
        }
        return Math.min(pixels, (int) ((long) energy * pixels / 100_000L));
    }
}
