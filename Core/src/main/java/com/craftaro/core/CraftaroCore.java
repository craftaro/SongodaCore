package com.craftaro.core;

import com.craftaro.core.builtin.CraftaroCoreCommand;
import com.craftaro.core.compatibility.ServerVersion;
import com.craftaro.core.nms.NMSGetter;
import com.craftaro.core.plugins.PluginInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CraftaroCore extends JavaPlugin {

    private static CraftaroCore instance;
    public static CraftaroCore getInstance() {
        return instance;
    }

    private final Set<PluginInfo> registeredPlugins = new HashSet<>();

    private BukkitCommandHandler commandManager;
    private NMSGetter nmsGetter;

    @Override
    public void onEnable() {
        instance = this;

        this.commandManager = BukkitCommandHandler.create(this);
        commandManager.register(new CraftaroCoreCommand(this));

        try {
            this.nmsGetter = (NMSGetter)Class.forName("com.craftaro.core.nms." + ServerVersion.getServerVersionString() + ".NMSGetterImpl").getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            getLogger().severe("Unable to load NMS hooks. Please update CraftaroCore. Your current server version: " + ServerVersion.getServerVersionString());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    public void registerPlugin(CraftaroPlugin plugin, int pluginId, String icon) {
        plugin.getLogger().info("Hooked into " + plugin.getName() + ".");
        PluginInfo info = new PluginInfo(plugin, pluginId, icon);
        registeredPlugins.add(info);

        // TODO: add an update checker
    }

    public Set<PluginInfo> getRegisteredPlugins() {
        return Collections.unmodifiableSet(registeredPlugins);
    }

    public NMSGetter getNMSGetter() {
        return nmsGetter;
    }
}
