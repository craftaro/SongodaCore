package com.songoda.core.library.economy.economies;

import org.bukkit.OfflinePlayer;

public interface Economy {

    String getName();

    boolean hasBalance(OfflinePlayer player, double cost);

    boolean withdrawBalance(OfflinePlayer player, double cost);

    boolean deposit(OfflinePlayer player, double amount);
}
