package com.songoda.core.hooks.holograms;

import com.songoda.core.SongodaCore;
import com.songoda.core.hooks.HookManager;
import com.songoda.core.hooks.holograms.impl.DecentHologramsImplementation;
import com.songoda.core.hooks.holograms.impl.HolographicDisplaysImplementation;

public class HologramManager extends HookManager<AbstractHologram> {

    public HologramManager(SongodaCore core) {
        super(core);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("HolographicDisplays", new HolographicDisplaysImplementation(core));
        registerHook("DecentHolograms", new DecentHologramsImplementation());
    }
}
