package com.songoda.core.hooks.protection;

import com.songoda.ultimateclaims.UltimateClaims;
import com.songoda.ultimateclaims.claim.Claim;
import com.songoda.ultimateclaims.member.ClaimPerm;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class UltimateClaimsProtection extends Protection {

    private final UltimateClaims instance;

    public UltimateClaimsProtection(Plugin plugin) {
        super(plugin);
        instance = UltimateClaims.getInstance();
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return hasPerms(player, location, ClaimPerm.PLACE);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return hasPerms(player, location, ClaimPerm.BREAK);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return hasPerms(player, location, ClaimPerm.INTERACT);
    }

    private boolean hasPerms(Player player, Location location, ClaimPerm claimPerm) {
        Claim claim = instance.getClaimManager().getClaim(location.getChunk());
        if (claim == null) {
            return true;
        }

        return claim.playerHasPerms(player, claimPerm);
    }

    @Override
    public String getName() {
        return "UltimateClaims";
    }

    @Override
    public boolean isEnabled() {
        return instance != null;
    }
}
