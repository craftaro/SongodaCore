package com.songoda.core.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Internal class for marking an inventory as a GUI inventory
 */
class GuiHolder implements InventoryHolder {
    final Gui gui;
    final GuiManager manager;

    public GuiHolder(GuiManager manager, Gui gui) {
        this.gui = gui;
        this.manager = manager;
    }

    @Override
    public Inventory getInventory() {
        return gui.inventory;
    }

    public Gui getGUI() {
        return gui;
    }

    public Inventory newInventory(int size, String title) {
        return Bukkit.createInventory(this, size, title);
    }

    public Inventory newInventory(InventoryType type, String title) {
        return Bukkit.createInventory(this, type, title);
    }
}
