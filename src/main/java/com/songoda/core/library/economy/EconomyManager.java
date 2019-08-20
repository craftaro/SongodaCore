package com.songoda.core.library.economy;

import com.songoda.core.library.economy.economies.Economy;
import com.songoda.core.library.economy.economies.PlayerPointsEconomy;
import com.songoda.core.library.economy.economies.ReserveEconomy;
import com.songoda.core.library.economy.economies.VaultEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EconomyManager {

    private static Set<Economy> registeredEconomies = new HashSet<>();

    public static void load() {
        if (!registeredEconomies.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("Vault"))
            registeredEconomies.add(new VaultEconomy());
        if (pluginManager.isPluginEnabled("Reserve"))
            registeredEconomies.add(new ReserveEconomy());
        if (pluginManager.isPluginEnabled("PlayerPoints"))
            registeredEconomies.add(new PlayerPointsEconomy());
    }

    public static Economy getEconomy(String name) {
        return registeredEconomies.stream().filter(economy -> economy.getName().equalsIgnoreCase(name.trim()))
                .findFirst().orElse(null);
    }

    public static Set<Economy> getRegisteredEconomies() {
        return Collections.unmodifiableSet(registeredEconomies);
    }
}
