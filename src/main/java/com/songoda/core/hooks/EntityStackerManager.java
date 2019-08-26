package com.songoda.core.hooks;

import com.songoda.core.hooks.EntityStackerType;
import com.songoda.core.hooks.stackers.Stacker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public class EntityStackerManager {

    private final static Map<EntityStackerType, Stacker> registeredStackers = new HashMap<>();
    private static Stacker defaultStacker = null;

    /**
     * Load all supported Stacker plugins. <br />
     * Note: This method should be called in your plugin's onEnable() section
     */
    public static void load() {
        if (!registeredStackers.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getPluginManager();

        for (EntityStackerType type : EntityStackerType.values()) {
            if (pluginManager.isPluginEnabled(type.plugin)) {
                Stacker stacker = type.getInstance();
                registeredStackers.put(type, stacker);
                if (defaultStacker == null)
                    defaultStacker = stacker;
            }
        }
    }

    /**
     * Set the default stacker to a different plugin, if that plugin exists.
     * If the plugin is not loaded or supported, the previously defined default will be used. <br />
     * NOTE: using a default stacker assumes that this library is shaded
     *
     * @param type stacker plugin to use
     */
    public static void setPreferredStackerPlugin(EntityStackerType type) {
        Stacker stacker = getStacker(type);
        if (stacker != null)
            defaultStacker = stacker;
    }

    /**
     * Set the default stacker to a different plugin, if that plugin exists.
     * If the plugin is not loaded or supported, the previously defined default will be used. <br />
     * NOTE: using a default stacker assumes that this library is shaded
     *
     * @param name name of the plugin to use
     */
    public static void setPreferredStackerPlugin(String name) {
        Stacker stacker = getStacker(name);
        if (stacker != null)
            defaultStacker = stacker;
    }

    /**
     * Try to grab the handler for this specific stacker plugin.
     *
     * @param name plugin to useH
     * @return returns null if plugin is not enabled
     */
    public static Stacker getStacker(String name) {
        if (name == null) return null;
        final String plugin = name.trim();
        return registeredStackers.get(registeredStackers.keySet().stream()
                .filter(type -> type.plugin.equalsIgnoreCase(plugin))
                .findFirst().orElse(null));
    }

    /**
     * Try to grab the handler for this specific stacker plugin.
     *
     * @param stacker plugin to use
     * @return returns null if plugin is not enabled
     */
    public static Stacker getStacker(EntityStackerType stacker) {
        return registeredStackers.get(stacker);
    }

    /**
     * Grab the default hologram plugin. <br />
     * NOTE: using a default hologram assumes that this library is shaded
     *
     * @return returns null if no plugin enabled
     */
    public static Stacker getStacker() {
        return defaultStacker;
    }

    /**
     * Grab a list of all supported stacker plugins that are loaded.
     *
     * @return an immutable collection of the loaded stacker, handler instances
     */
    public static Collection<Stacker> getRegisteredStackers() {
        return Collections.unmodifiableCollection(registeredStackers.values());
    }


    public static boolean isStacked(LivingEntity entity) {
        return defaultStacker != null && defaultStacker.isStacked(entity);
    }

    public int getSize(LivingEntity entity) {
        return defaultStacker != null ? defaultStacker.getSize(entity) : 1;
    }

    public void removeOne(LivingEntity entity) {
        remove(entity, 1);
    }

    public void remove(LivingEntity entity, int amount) {
        if (defaultStacker != null)
            defaultStacker.remove(entity, amount);

    }

    public void addOne(LivingEntity entity) {
        add(entity, 1);
    }

    public void add(LivingEntity entity, int amount) {
        if (defaultStacker != null)
            defaultStacker.add(entity, amount);
    }
}
