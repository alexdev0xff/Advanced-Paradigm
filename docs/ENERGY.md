# Energy System (Example)

This mod uses TechReborn Energy API (`teamreborn:energy`) to provide a simple, push-based energy system.

Key rule (important):
- Sources and wires *push* energy to neighbors.
- Consumers do **not** pull energy from neighbors.

## Blocks (Examples)

### 1) Coal Generator (`test_block`)

Files:
- Logic + storage: `src/main/java/alexdev0xff/advancedparadigm/Blocks/TestBlockEntity.java`
- Menu: `src/main/java/alexdev0xff/advancedparadigm/Blocks/TestBlockMenu.java`
- Screen: `src/client/java/alexdev0xff/advancedparadigm/Blocks/TestBlockScreen.java`
- Block (ticker + open GUI): `src/main/java/alexdev0xff/advancedparadigm/Blocks/TestBlock.java`

What it does:
- Has 1 fuel slot (coal / charcoal / coal block).
- Burns fuel and adds energy into its internal buffer.
- Each tick, pushes up to `ENERGY_MAX_EXTRACT` into adjacent energy storages.

GUI:
- Background texture is `assets/advancedparadigm/textures/gui/test_block.png` (176x166 suggested).
- Bars (burn + energy) are drawn with `GuiGraphics.fill(...)` on top of the texture,
  so the PNG can be just a "picture".

### 2) Energy Pipe (`energy_pipe`)

Files:
- Block: `src/main/java/alexdev0xff/advancedparadigm/Blocks/EnergyPipeBlock.java`
- BlockEntity: `src/main/java/alexdev0xff/advancedparadigm/Blocks/EnergyPipeBlockEntity.java`

What it does:
- Has a small internal buffer (default 1000).
- Accepts insertion from sources/cells.
- Each tick, pushes up to `TRANSFER_PER_TICK` from its buffer to adjacent storages.

Design note:
- The pipe **does not pull** from neighbors. It only moves what it already has.
  This follows the "push-based" rule.

### 3) Energy Cell (`energy_cell`)

Files:
- Block: `src/main/java/alexdev0xff/advancedparadigm/Blocks/EnergyCellBlock.java`
- BlockEntity: `src/main/java/alexdev0xff/advancedparadigm/Blocks/EnergyCellBlockEntity.java`
- Menu: `src/main/java/alexdev0xff/advancedparadigm/Blocks/EnergyCellMenu.java`
- Screen: `src/client/java/alexdev0xff/advancedparadigm/Blocks/EnergyCellScreen.java`

What it does:
- Large buffer (default 1,000,000).
- Acts like a "battery": accepts energy, stores it, and pushes it out to neighbors each tick.

GUI:
- Background texture: `assets/advancedparadigm/textures/gui/energy_cell.png`.
- Energy bar is drawn with `fill(...)`, so you only need the background PNG.

### 4) Electric Furnace (`electric_furnace`)

Files:
- Block: `src/main/java/alexdev0xff/advancedparadigm/Blocks/ElectricFurnaceBlock.java`
- BlockEntity: `src/main/java/alexdev0xff/advancedparadigm/Blocks/ElectricFurnaceBlockEntity.java`
- Menu: `src/main/java/alexdev0xff/advancedparadigm/Blocks/ElectricFurnaceMenu.java`
- Screen: `src/client/java/alexdev0xff/advancedparadigm/Blocks/ElectricFurnaceScreen.java`

What it does:
- Accepts energy (insertion only) and stores it internally.
- Uses vanilla smelting recipes (`RecipeType.SMELTING`) to process input -> output.
- Consumes `ENERGY_PER_TICK` while working and resets progress when it can't work.

GUI:
- Background texture: `assets/advancedparadigm/textures/gui/electric_furnace.png`.
- Progress arrow + energy bar are drawn with `fill(...)`.

## How Energy Is Exposed (TechReborn Energy API)

Registration happens in:
- `src/main/java/alexdev0xff/advancedparadigm/Blocks/ModBlockEntities.java`

Example pattern:
- Each BlockEntity owns a `SimpleEnergyStorage`.
- We register it with `EnergyStorage.SIDED.registerForBlockEntity(...)`.
- Other blocks/items can query neighbors with `EnergyStorage.SIDED.find(level, pos, side)`.

## Textures You Need To Add

Block textures (`assets/advancedparadigm/textures/block/`):
- `energy_pipe.png`
- `energy_cell.png`
- `electric_furnace.png`

GUI textures (`assets/advancedparadigm/textures/gui/`):
- `test_block.png` (generator background)
- `energy_cell.png` (cell background)
- `electric_furnace.png` (furnace background)

## Typical Setup In-Game

1) Place generator (`test_block`)
2) Place pipe(s) (`energy_pipe`)
3) Place consumer (`electric_furnace`) or buffer (`energy_cell`)

Energy flow:
- generator pushes -> pipe buffer -> pipe pushes -> consumer/cell
- cell pushes -> pipe -> consumer (acts like a battery/source)

