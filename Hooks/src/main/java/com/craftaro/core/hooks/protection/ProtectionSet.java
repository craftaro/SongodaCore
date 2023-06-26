package com.craftaro.core.hooks.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class ProtectionSet extends HashSet<IProtection> {

    public boolean canPlace(Player player, Location location) {
        return stream().allMatch(protection -> protection.canPlace(player, location));
    }

    public boolean canBreak(Player player, Location location) {
        return stream().allMatch(protection -> protection.canBreak(player, location));
    }

    public boolean canInteract(Player player, Location location) {
        return stream().allMatch(protection -> protection.canInteract(player, location));
    }
}
