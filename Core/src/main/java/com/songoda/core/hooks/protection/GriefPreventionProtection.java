package com.songoda.core.hooks.protection;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GriefPreventionProtection extends Protection {

    private final DataStore dataStore;

    public GriefPreventionProtection(Plugin plugin) {
        super(plugin);
        this.dataStore = GriefPrevention.instance.dataStore;
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return getClaim(location).allowBuild(player, location.getBlock().getType()) == null;
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return getClaim(location).allowBreak(player, location.getBlock().getType()) == null;
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return getClaim(location).allowContainers(player) == null;
    }

    private Claim getClaim(Location location) {
        return dataStore.getClaimAt(location, true, null);
    }

    @Override
    public String getName() {
        return "GriefPrevention";
    }

    @Override
    public boolean isEnabled() {
        return GriefPrevention.instance != null;
    }
}
