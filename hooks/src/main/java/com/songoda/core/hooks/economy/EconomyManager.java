package com.songoda.core.hooks.economy;

import com.songoda.core.hooks.HookManager;
import com.songoda.core.hooks.economy.impl.VaultImplementation;
import org.bukkit.plugin.Plugin;

public class EconomyManager extends HookManager<IEconomy> {

    public EconomyManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("Vault", new VaultImplementation());
    }
}
