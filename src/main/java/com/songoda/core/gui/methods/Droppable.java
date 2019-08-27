package com.songoda.core.gui.methods;

import com.songoda.core.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Droppable {

    boolean onDrop(Player player, Inventory inventory, GUI gui, ItemStack cursor);
}
