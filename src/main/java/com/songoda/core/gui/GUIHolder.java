package com.songoda.core.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

class GUIHolder implements InventoryHolder {

    final GUI gui;

    GUIHolder(GUI gui) {
        this.gui = gui;
    }

    @Override
    public Inventory getInventory() {
        return gui.inventory;
    }

    public GUI getGUI() {
        return gui;
    }
}
