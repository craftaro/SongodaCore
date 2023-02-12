package com.songoda.core.hooks.protection.impl;

import com.songoda.core.hooks.protection.IProtection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.flags.Flag;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.lists.Flags;
import world.bentobox.bentobox.managers.IslandsManager;

import java.util.Optional;

public class BentoBoxImplementation implements IProtection {

    private IslandsManager islandsManager;

    @Override
    public String getHookName() {
        return "BentoBox";
    }

    @Override
    public boolean enableHook() {
        this.islandsManager = BentoBox.getInstance().getIslands();
        return islandsManager != null;
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return hasPerms(player, location, Flags.PLACE_BLOCKS);
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return hasPerms(player, location, Flags.BREAK_BLOCKS);
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return hasPerms(player, location, Flags.CONTAINER);
    }

    private boolean hasPerms(Player player, Location location, Flag flag) {
        if (!BentoBox.getInstance().getIWM().inWorld(location)) {
            return true;
        }

        Optional<Island> optional = islandsManager.getIslandAt(location);
        if (!optional.isPresent()) {
            return flag.isSetForWorld(location.getWorld());
        }

        Island island = optional.get();
        User user = User.getInstance(player);

        return island.isAllowed(user, flag);
    }

}
