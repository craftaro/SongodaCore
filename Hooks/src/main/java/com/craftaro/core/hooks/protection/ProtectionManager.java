package com.craftaro.core.hooks.protection;

import com.craftaro.core.hooks.protection.impl.BentoBoxImplementation;
import com.craftaro.core.hooks.HookManager;
import com.craftaro.core.hooks.protection.impl.DummyProtectionImplementation;
import com.craftaro.core.hooks.protection.impl.GriefPreventionImplementation;
import com.craftaro.core.hooks.protection.impl.WorldGuardImplementation;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ProtectionManager extends HookManager<IProtection> {

    public ProtectionManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    protected void registerDefaultHooks() {
        registerHook("BentoBox", new BentoBoxImplementation());
        registerHook("GriefPrevention", new GriefPreventionImplementation());
        registerHook("WorldGuard", new WorldGuardImplementation());
    }

    @Override
    protected IProtection getDummyHook() {
        return new DummyProtectionImplementation();
    }
    public ProtectionSet getHooksByName(List<String> names) {
        ProtectionSet hooks = new ProtectionSet();
        for (String name : names) {
            IProtection hook = registeredHooks.get(name);
            if (hook != null) {
                hooks.add(hook);
            }
        }

        if (hooks.isEmpty()) {
            hooks.add(getDummyHook());
        }

        return hooks;
    }

}
