package com.songoda.core.hooks;

import com.songoda.core.hooks.economies.Economy;
import com.songoda.core.hooks.economies.VaultEconomy;
import com.songoda.core.hooks.economies.ReserveEconomy;
import com.songoda.core.hooks.economies.PlayerPointsEconomy;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public enum EconomyType {

    VAULT("Vault", VaultEconomy.class),
    PLAYER_POINTS("Reserve", ReserveEconomy.class),
    RESERVE("PlayerPoints", PlayerPointsEconomy.class);

    public final String plugin;
    protected final Class managerClass;

    private EconomyType(String plugin, Class managerClass) {
        this.plugin = plugin;
        this.managerClass = managerClass;
    }

    protected Economy getInstance() {
        try {
            return (Economy) managerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unexpected Error while creating a new Economy Manager for " + name(), ex);
        }
        return null;
    }
}
