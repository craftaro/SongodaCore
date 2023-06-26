package com.craftaro.core.hooks.protection.impl;

import com.craftaro.core.hooks.protection.IProtection;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.DataStore;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GriefPreventionImplementation implements IProtection {

    private     DataStore dataStore;

    @Override
    public String getHookName() {
        return "GriefPrevention";
    }

    @Override
    public boolean enableHook() {
        if (GriefPrevention.instance == null) {
            return false;
        }

        this.dataStore = GriefPrevention.instance.dataStore;
        return true;
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        Claim claim = getClaim(location);

        if (claim == null) {
            return true;
        }

        return claim.allowBuild(player, location.getBlock().getType()) == null;
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        Claim claim = getClaim(location);

        if (claim == null) {
            return true;
        }

        return claim.allowBreak(player, location.getBlock().getType()) == null;
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        Claim claim = getClaim(location);

        if (claim == null) {
            return true;
        }

        return claim.allowContainers(player) == null;
    }

    private Claim getClaim(Location location) {
        return dataStore.getClaimAt(location, true, null);
    }
}
