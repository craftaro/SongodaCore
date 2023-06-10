package com.craftaro.core.gui;

import org.bukkit.event.inventory.InventoryType;

public enum GuiType {
    STANDARD(InventoryType.CHEST, 6, 9),
    DISPENSER(InventoryType.DISPENSER, 9, 3),
    HOPPER(InventoryType.HOPPER, 5, 1);

    protected final InventoryType type;
    protected final int rows;
    protected final int columns;

    GuiType(InventoryType type, int rows, int columns) {
        this.type = type;
        this.rows = rows;
        this.columns = columns;
    }
}
