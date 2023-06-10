package com.craftaro.core.hooks.log;

import com.craftaro.core.hooks.Hook;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

public abstract class Log implements Hook {
    public abstract void logPlacement(OfflinePlayer player, Block block);

    public abstract void logRemoval(OfflinePlayer player, Block block);

    public abstract void logInteraction(OfflinePlayer player, Location location);
}
