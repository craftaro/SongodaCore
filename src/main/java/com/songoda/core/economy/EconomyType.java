package com.songoda.core.library.economy;

import com.songoda.core.library.economy.economies.*;
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
