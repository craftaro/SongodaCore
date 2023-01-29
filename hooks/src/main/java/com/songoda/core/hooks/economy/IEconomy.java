package com.songoda.core.hooks.economy;

import com.songoda.core.hooks.PluginHook;
import org.bukkit.OfflinePlayer;

public interface IEconomy extends PluginHook {

    /**
     * Get the players available balance
     *
     * @param player player
     *
     * @return the amount of available balance
     */
    double getBalance(OfflinePlayer player);

    /**
     * Check to see if a player has at least some balance available
     *
     * @param player player to check
     * @param amount minimum amount this player should have
     *
     * @return true if this player can have this amount withdrawn
     */
     boolean hasBalance(OfflinePlayer player, double amount);

    /**
     * Try to withdraw an amount from a player's balance
     *
     * @param player player to check
     * @param amount amount to remove from this player
     *
     * @return true if the total amount was withdrawn successfully
     */
     boolean withdrawBalance(OfflinePlayer player, double amount);

    /**
     * Try to add an amount to a player's balance
     *
     * @param player player to check
     * @param amount amount to add to this player
     *
     * @return true if the total amount was added successfully
     */
     boolean depositBalance(OfflinePlayer player, double amount);
}
