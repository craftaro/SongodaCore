package com.craftaro.core.hooks.economy.impl;

import com.craftaro.core.hooks.economy.IEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultImplementation implements IEconomy {

    public VaultImplementation(Plugin plugin) {}

    private Economy economy;

    @Override
    public String getHookName() {
        return "Vault";
    }

    @Override
    public boolean enableHook() {
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            return false;
        }

        economy = provider.getProvider();
        return true;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    @Override
    public boolean hasBalance(OfflinePlayer player, double amount) {
        return economy.getBalance(player) >= amount;
    }

    @Override
    public boolean withdrawBalance(OfflinePlayer player, double amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public boolean depositBalance(OfflinePlayer player, double amount) {
        return economy.depositPlayer(player, amount).transactionSuccess();
    }
}
