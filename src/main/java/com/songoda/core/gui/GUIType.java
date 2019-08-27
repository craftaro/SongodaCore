package com.songoda.core.gui;

import org.bukkit.event.inventory.InventoryType;

public enum GUIType {

    STANDARD(InventoryType.CHEST),
    DISPENSER(InventoryType.DISPENSER),
    HOPPER(InventoryType.HOPPER);

    protected final InventoryType type;

    private GUIType(InventoryType type) {
        this.type = type;
    }

}
