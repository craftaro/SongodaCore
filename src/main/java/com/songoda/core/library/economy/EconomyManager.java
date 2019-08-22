package com.songoda.core.library.economy;

import com.songoda.core.library.economy.economies.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private final static Map<EconomyType, Economy> registeredEconomies = new HashMap<>();
    private static Economy defaultEcon = null;

    /**
     * Load all supported economy plugins. <br />
     * Note: This method should be called in your plugin's onEnable() section
     */
    public static void load() {
        if (!registeredEconomies.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getPluginManager();

        for(EconomyType type : EconomyType.values()) {
            if (pluginManager.isPluginEnabled(type.plugin)) {
                Economy econ = type.getInstance();
                registeredEconomies.put(type, econ);
                if(defaultEcon == null)
                    defaultEcon = econ;
            }
        }
    }

    /**
     * Set the default economy to a different plugin, if that plugin exists. 
     * If the plugin is not loaded or supported, the previously defined default will be used. <br />
     * NOTE: using a default economy assumes that this library is shaded
     * @param name name of the plugin to use
     */
    public static void setPreferredEconomy(String name) {
        Economy econ = getEconomy(name);
        if(econ != null)
            defaultEcon = econ;
    }

    /**
     * Set the default economy to a different plugin, if that plugin exists. 
     * If the plugin is not loaded or supported, the previously defined default will be used. <br />
     * NOTE: using a default economy assumes that this library is shaded
     * @param economy plugin to use
     */
    public static void setPreferredEconomy(EconomyType economy) {
        Economy econ = getEconomy(economy);
        if(econ != null)
            defaultEcon = econ;
    }

    /**
     * Try to grab the handler for this specific economy plugin.
     * @param name plugin to use
     * @return returns null if plugin is not enabled
     */
    public static Economy getEconomy(String name) {
        if(name == null) return null;
        final String plugin = name.trim();
        return registeredEconomies.get(registeredEconomies.keySet().stream()
                .filter(type -> type.plugin.equalsIgnoreCase(plugin))
                .findFirst().orElse(null));
    }

    /**
     * Try to grab the handler for this specific economy plugin.
     * @param economy plugin to use
     * @return returns null if plugin is not enabled
     */
    public static Economy getEconomy(EconomyType economy) {
        return registeredEconomies.get(economy);
    }

    /**
     * Grab the default economy plugin. <br />
     * NOTE: using a default economy assumes that this library is shaded
     * @return returns null if no plugin enabled
     */
    public static Economy getEconomy() {
        return defaultEcon;
    }

    /**
     * Grab a list of all supported economy plugins.
     * @return an immutable collection of the loaded economy handler instances
     */
    public static Collection<Economy> getRegisteredEconomies() {
        return Collections.unmodifiableCollection(registeredEconomies.values());
    }

    /**
     * Check to see if a specific economy plugin is enabled.
     * @param name plugin to check
     * @return true if this economy plugin is supported and loaded
     */
    public static boolean isEnabled(String name) {
        return getEconomy(name) != null;
    }

    /**
     * Check to see if a specific economy plugin is enabled.
     * @param economy plugin to check
     * @return true if this economy plugin is supported and loaded
     */
    public static boolean isEnabled(EconomyType economy) {
        return registeredEconomies.containsKey(economy);
    }

    /**
     * Check to see if there is a default economy loaded. <br />
     * NOTE: using a default economy assumes that this library is shaded
     * @return returns false if there are no supported economy plugins
     */
    public static boolean isEnabled() {
        return defaultEcon != null;
    }

    /**
     * Get the name of the economy plugin being used. <br />
     * NOTE: using a default economy assumes that this library is shaded
     *
     * @return
     */
    public static String getName() {
        return defaultEcon != null ? defaultEcon.getName() : null;
    }

    /**
     * Check to see if a player has at least some balance available. <br />
     * NOTE: using a default economy assumes that this library is shaded
     *
     * @param player player to check
     * @param cost minimum amount this player should have
     * @return true if this player can have this amount withdrawn
     */
    public static boolean hasBalance(OfflinePlayer player, double cost) {
        return defaultEcon != null ? defaultEcon.hasBalance(player, cost) : false;
    }

    /**
     * Try to withdraw an amount from a player's balance. <br />
     * NOTE: using a default economy assumes that this library is shaded
     *
     * @param player player to check
     * @param cost amount to remove from this player
     * @return true if the total amount was withdrawn successfully
     */
    public static boolean withdrawBalance(OfflinePlayer player, double cost) {
        return defaultEcon != null ? defaultEcon.withdrawBalance(player, cost) : false;
    }

    /**
     * Try to add an amount to a player's balance. <br />
     * NOTE: using a default economy assumes that this library is shaded
     *
     * @param player player to check
     * @param amount amount to add to this player
     * @return true if the total amount was added successfully
     */
    public static boolean deposit(OfflinePlayer player, double amount) {
        return defaultEcon != null ? defaultEcon.deposit(player, amount) : false;
    }
}
