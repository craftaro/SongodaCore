package com.songoda.core.hooks.protection;

import com.songoda.core.hooks.Hook;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class Protection implements Hook {
    protected final Plugin plugin;

    public Protection(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract boolean canPlace(Player player, Location location);

    public abstract boolean canBreak(Player player, Location location);

    public abstract boolean canInteract(Player player, Location location);
}
