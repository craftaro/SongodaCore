package com.songoda.core.hooks.protection;

import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
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
        return hasPerms(player, location, RoleSetting.BLOCK_PLACE);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return hasPerms(player, location, RoleSetting.BLOCK_BREAK);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return hasPerms(player, location, RoleSetting.INTERACT_CONTAINER);
    }

    private boolean hasPerms(Player player, Location location, RoleSetting roleSetting) {
        Area area = landsIntegration.getAreaByLoc(location);

        if (area == null) {
            return true;
        }

        return area.canSetting(player, roleSetting, false);
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
