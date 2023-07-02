package com.craftaro.core.hooks.holograms;

import com.craftaro.core.hooks.HookManager;
import com.craftaro.core.hooks.holograms.impl.DecentHologramsImplementation;
import com.craftaro.core.hooks.holograms.impl.HolographicDisplaysImplementation;
import com.craftaro.core.hooks.holograms.impl.DummyHologramImplementation;
import org.bukkit.plugin.Plugin;

public class HologramManager extends HookManager<AbstractHologram> {

    public HologramManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("HolographicDisplays", HolographicDisplaysImplementation.class);
        registerHook("DecentHolograms", DecentHologramsImplementation.class);
    }

    @Override
    protected AbstractHologram getDummyHook() {
        return new DummyHologramImplementation();
    }
}
