package com.craftaro.core.hooks.economy;

import com.craftaro.core.hooks.HookManager;
import com.craftaro.core.hooks.economy.impl.DummyEconomyImplementation;
import com.craftaro.core.hooks.economy.impl.VaultImplementation;
import org.bukkit.plugin.Plugin;

public class EconomyManager extends HookManager<IEconomy> {

    public EconomyManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("Vault", VaultImplementation.class);
    }

    @Override
    protected IEconomy getDummyHook() {
        return new DummyEconomyImplementation();
    }
}
