package com.craftaro.core.hooks.protection.impl;

import com.craftaro.core.hooks.protection.IProtection;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class WorldGuardImplementation implements IProtection {

    private RegionContainer container;

    @Override
    public String getHookName() {
        return "WorldGuard";
    }

    @Override
    public boolean enableHook() {
        if (WorldGuardPlugin.inst() == null) {
            return false;
        }
        container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        return true;
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        RegionManager regions = container.get(BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld())));
        if (regions == null) {
            return true;
        }
        ApplicableRegionSet applicableRegions = regions.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        if (applicableRegions.size() == 0) {
            return true;
        }
        return applicableRegions.testState(WorldGuardPlugin.inst().wrapPlayer(player), Flags.BUILD);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        RegionManager regions = container.get(BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld())));
        if (regions == null) {
            return true;
        }
        ApplicableRegionSet applicableRegions = regions.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        if (applicableRegions.size() == 0) {
            return true;
        }
        return applicableRegions.testState(WorldGuardPlugin.inst().wrapPlayer(player), Flags.BLOCK_BREAK);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        RegionManager regions = container.get(BukkitAdapter.adapt(Objects.requireNonNull(location.getWorld())));
        if (regions == null) {
            return true;
        }
        ApplicableRegionSet applicableRegions = regions.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        if (applicableRegions.size() == 0) {
            return true;
        }
        return applicableRegions.testState(WorldGuardPlugin.inst().wrapPlayer(player), Flags.INTERACT);
    }
}
