package com.craftaro.core.hooks.protection.impl;

import com.craftaro.core.hooks.protection.IProtection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DummyProtectionImplementation implements IProtection {
    @Override
    public String getHookName() {
        return "None";
    }

    @Override
    public boolean enableHook() {
        return true;
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return true;
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return true;
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return true;
    }
}
