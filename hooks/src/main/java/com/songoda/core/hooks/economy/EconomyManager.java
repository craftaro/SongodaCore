package com.songoda.core.hooks.economy;

import com.songoda.core.SongodaCore;
import com.songoda.core.hooks.HookManager;
import com.songoda.core.hooks.economy.impl.VaultImplementation;

public class EconomyManager extends HookManager<IEconomy> {

    public EconomyManager(SongodaCore core) {
        super(core);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("Vault", new VaultImplementation());
    }
}
