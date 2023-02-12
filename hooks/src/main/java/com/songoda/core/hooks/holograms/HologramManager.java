package com.songoda.core.hooks.holograms;

import com.songoda.core.hooks.HookManager;
import com.songoda.core.hooks.holograms.impl.DecentHologramsImplementation;
import com.songoda.core.hooks.holograms.impl.HolographicDisplaysImplementation;
import org.bukkit.plugin.Plugin;

public class HologramManager extends HookManager<AbstractHologram> {

    public HologramManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("HolographicDisplays", new HolographicDisplaysImplementation(plugin));
        registerHook("DecentHolograms", new DecentHologramsImplementation());
    }
}
