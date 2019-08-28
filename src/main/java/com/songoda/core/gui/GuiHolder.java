package com.songoda.core.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Internal class for marking an inventory as a GUI inventory
 * 
 * @since 2019-08-25
 * @author jascotty2
 */
class GuiHolder implements InventoryHolder {

    final Gui gui;

    GuiHolder(Gui gui) {
        this.gui = gui;
    }

    @Override
    public Inventory getInventory() {
        return gui.inventory;
    }

    public Gui getGUI() {
        return gui;
    }
}
