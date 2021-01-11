package com.songoda.core.hooks.protection;

import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.role.enums.RoleSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LandsProtection extends Protection {

    private final LandsIntegration landsIntegration;

    public LandsProtection(Plugin plugin) {
        super(plugin);
        this.landsIntegration = new LandsIntegration(plugin);
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return landsIntegration.getAreaByLoc(location).canSetting(player, RoleSetting.BLOCK_PLACE, false);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return landsIntegration.getAreaByLoc(location).canSetting(player, RoleSetting.BLOCK_BREAK, false);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return landsIntegration.getAreaByLoc(location).canSetting(player, RoleSetting.INTERACT_CONTAINER, false);
    }

    @Override
    public String getName() {
        return "Lands";
    }

    @Override
    public boolean isEnabled() {
        return landsIntegration != null;
    }
}
