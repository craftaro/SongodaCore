package com.craftaro.core.hooks.hologram;

import com.craftaro.core.hooks.BaseHookRegistry;
import com.craftaro.core.hooks.HookPriority;
import com.craftaro.core.hooks.hologram.adapter.CmiHologramHook;
import com.craftaro.core.hooks.hologram.adapter.DecentHologramsHook;
import com.craftaro.core.hooks.hologram.adapter.SainttxHologramsHook;
import org.bukkit.plugin.Plugin;

public class HologramHookRegistry extends BaseHookRegistry<HologramHook> {
    public HologramHookRegistry(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void registerDefaultHooks() {
        register(new DecentHologramsHook(), HookPriority.HIGH);
        register(new SainttxHologramsHook(), HookPriority.NORMAL);
        register(new CmiHologramHook(), HookPriority.LOW);
    }
}
