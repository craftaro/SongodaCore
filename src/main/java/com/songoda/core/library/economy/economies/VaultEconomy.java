package com.songoda.ultimateclaims.economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VaultEconomy implements Economy {
    private final net.milkbowl.vault.economy.Economy vault;

    public VaultEconomy() {
        this.vault = Bukkit.getServicesManager().
                getRegistration(net.milkbowl.vault.economy.Economy.class).getProvider();
    }

    @Override
    public boolean hasBalance(OfflinePlayer player, double cost) {
        return vault.has(player, cost);
    }

    @Override
    public boolean withdrawBalance(OfflinePlayer player, double cost) {
        return vault.withdrawPlayer(player, cost).transactionSuccess();
    }

    @Override
    public boolean deposit(OfflinePlayer player, double amount) {
        return vault.depositPlayer(player, amount).transactionSuccess();
    }
}
