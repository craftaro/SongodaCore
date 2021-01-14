package com.songoda.core.hooks.protection;

import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TownyProtection extends Protection {
    public TownyProtection(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.BUILD);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.DESTROY);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.ITEM_USE);
    }

    @Override
    public String getName() {
        return "Towny";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
