package com.songoda.core.hooks.protection;

import com.songoda.core.hooks.PluginHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IProtection extends PluginHook {

    boolean canPlace(Player player, Location location);
    boolean canBreak(Player player, Location location);
    boolean canInteract(Player player, Location location);
}
