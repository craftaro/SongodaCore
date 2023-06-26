package com.craftaro.core.hooks.economy.impl;

import com.craftaro.core.hooks.economy.IEconomy;
import org.bukkit.OfflinePlayer;

public class DummyEconomyImplementation implements IEconomy {
    @Override
    public String getHookName() {
        return "None";
    }

    @Override
    public boolean enableHook() {
        return true;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return 0;
    }

    @Override
    public boolean hasBalance(OfflinePlayer player, double amount) {
        return true;
    }

    @Override
    public boolean withdrawBalance(OfflinePlayer player, double amount) {
        return true;
    }

    @Override
    public boolean depositBalance(OfflinePlayer player, double amount) {
        return true;
    }
}
