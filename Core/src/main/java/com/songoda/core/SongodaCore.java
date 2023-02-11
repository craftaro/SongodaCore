package com.songoda.core;

import com.songoda.core.builtin.SongodaCoreCommand;
import com.songoda.core.plugins.PluginInfo;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SongodaCore extends JavaPlugin {

    private static SongodaCore instance;
    public static SongodaCore getInstance() {
        return instance;
    }

    private final Set<PluginInfo> registeredPlugins = new HashSet<>();

    private BukkitCommandHandler commandManager;

    @Override
    public void onEnable() {
        instance = this;

        this.commandManager = BukkitCommandHandler.create(this);
        commandManager.register(new SongodaCoreCommand(this));
    }

    public void registerPlugin(SongodaPlugin plugin, int pluginId, String icon) {
        plugin.getLogger().info("Hooked into " + plugin.getName() + ".");
        PluginInfo info = new PluginInfo(plugin, pluginId, icon);
        registeredPlugins.add(info);

        // TODO: add an update checker
    }

    public Set<PluginInfo> getRegisteredPlugins() {
        return Collections.unmodifiableSet(registeredPlugins);
    }
}
