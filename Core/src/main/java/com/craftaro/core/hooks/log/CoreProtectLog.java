package com.craftaro.core.hooks.log;

import com.craftaro.core.compatibility.ServerVersion;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

public class CoreProtectLog extends Log {
    private CoreProtectAPI api;
    private boolean useDeprecatedMethod = ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_12);

    public CoreProtectLog() {
        this.api = CoreProtect.getInstance().getAPI();
    }

    @Override
    public String getName() {
        return "CoreProtect";
    }

    @Override
    public boolean isEnabled() {
        return api.isEnabled();
    }

    @Override
    public void logPlacement(OfflinePlayer player, Block block) {
        if (this.useDeprecatedMethod) {
            this.api.logPlacement(player.getName(), block.getLocation(), block.getType(), block.getData());
            return;
        }

        this.api.logPlacement(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
    }

    @Override
    public void logRemoval(OfflinePlayer player, Block block) {
        if (this.useDeprecatedMethod) {
            this.api.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getData());
            return;
        }

        this.api.logRemoval(player.getName(), block.getLocation(), block.getType(), block.getBlockData());
    }

    @Override
    public void logInteraction(OfflinePlayer player, Location location) {
        this.api.logInteraction(player.getName(), location);
    }
}
