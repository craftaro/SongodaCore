package com.songoda.core.hooks.economies;

import com.songoda.core.hooks.Hook;
import org.bukkit.OfflinePlayer;

public interface Economy extends Hook {

    /**
     * Check to see if a player has at least some balance available
     *
     * @param player player to check
     * @param cost minimum amount this player should have
     * @return true if this player can have this amount withdrawn
     */
    boolean hasBalance(OfflinePlayer player, double cost);

    /**
     * Try to withdraw an amount from a player's balance
     *
     * @param player player to check
     * @param cost amount to remove from this player
     * @return true if the total amount was withdrawn successfully
     */
    boolean withdrawBalance(OfflinePlayer player, double cost);

    /**
     * Try to add an amount to a player's balance
     *
     * @param player player to check
     * @param amount amount to add to this player
     * @return true if the total amount was added successfully
     */
    boolean deposit(OfflinePlayer player, double amount);
}
