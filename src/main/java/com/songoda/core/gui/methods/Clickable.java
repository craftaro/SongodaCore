package com.songoda.core.gui.methods;

import com.songoda.core.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Clickable {

    void onClick(Player player, Inventory inventory, GUI gui, ItemStack cursor, int slot, ClickType type);
}
