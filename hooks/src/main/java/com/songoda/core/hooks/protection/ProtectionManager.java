package com.songoda.core.hooks.protection;

import com.songoda.core.hooks.HookManager;
import com.songoda.core.hooks.protection.impl.BentoBoxImplementation;
import com.songoda.core.hooks.protection.impl.GriefPreventionImplementation;
import org.bukkit.plugin.Plugin;

public class ProtectionManager extends HookManager<IProtection> {

    public ProtectionManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("BentoBox", new BentoBoxImplementation());
        registerHook("GriefPrevention", new GriefPreventionImplementation());
    }
}
