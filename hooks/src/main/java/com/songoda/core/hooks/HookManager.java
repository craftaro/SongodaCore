package com.songoda.core.hooks;

import com.songoda.core.SongodaCore;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public abstract class HookManager<T extends PluginHook> {

    protected final SongodaCore core;
    private final Map<String, T> registeredHooks = new HashMap<>();

    public HookManager(SongodaCore core) {
        this.core = core;
        registerDefaultHooks();
    }

    public T getHookByName(String name) {
        return registeredHooks.get(name);
    }

    public void registerHook(String requiredPlugin, T hook) {
        if (!Bukkit.getPluginManager().isPluginEnabled(requiredPlugin)) {
            return;
        }

        if (!hook.enableHook()) {
            return;
        }

        registeredHooks.put(hook.getHookName(), hook);
    }

    protected abstract void registerDefaultHooks();
}
