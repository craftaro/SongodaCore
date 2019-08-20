package com.songoda.core.library.economy;

import com.songoda.core.library.economy.economies.Economy;
import com.songoda.core.library.economy.economies.PlayerPointsEconomy;
import com.songoda.core.library.economy.economies.ReserveEconomy;
import com.songoda.core.library.economy.economies.VaultEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private static Map<Economies, Economy> registeredEconomies = new HashMap();

    public static void load() {
        if (!registeredEconomies.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (pluginManager.isPluginEnabled("Vault"))
            registeredEconomies.put(new VaultEconomy());
        if (pluginManager.isPluginEnabled("Reserve"))
            registeredEconomies.add(new ReserveEconomy());
        if (pluginManager.isPluginEnabled("PlayerPoints"))
            registeredEconomies.add(new PlayerPointsEconomy());
    }
}
