package com.songoda.update.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Clickable {

    void Clickable(Player player, Inventory inventory, ItemStack cursor, int slot, ClickType type);
}
