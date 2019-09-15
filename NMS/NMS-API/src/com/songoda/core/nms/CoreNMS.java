package com.songoda.core.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

public interface CoreNMS {
    public CustomAnvil createAnvil(Player player);
    public CustomAnvil createAnvil(Player player, InventoryHolder holder);
}
