package com.songoda.core.hooks;

import com.songoda.core.hooks.holograms.Holograms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramManager {

    private final static Map<HologramType, Holograms> registeredHolograms = new HashMap<>();
    private static Holograms defaultHolo = null;

    /**
     * Load all supported hologram plugins. <br />
     * Note: This method should be called in your plugin's onEnable() section
     *
     * @param javaPlugin the plugin owning the holograms
     */
    public static void load(JavaPlugin javaPlugin) {
        if (!registeredHolograms.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getPluginManager();

        for (HologramType type : HologramType.values()) {
            if (pluginManager.isPluginEnabled(type.plugin)) {
                Holograms holo = type.getInstance(javaPlugin);
                registeredHolograms.put(type, holo);
                if (defaultHolo == null)
                    defaultHolo = holo;
            }
        }
    }

    /**
     * Set the default hologram to a different plugin, if that plugin exists.
     * If the plugin is not loaded or supported, the previously defined default will be used. <br />
     * NOTE: using a default hologram assumes that this library is shaded
     *
     * @param type hologram plugin to use
     */
    public static void setPreferredHologramPlugin(HologramType type) {
        Holograms holo = getHolograms(type);
        if (holo != null)
            defaultHolo = holo;
    }

    /**
     * Set the default hologram to a different plugin, if that plugin exists.
     * If the plugin is not loaded or supported, the previously defined default will be used. <br />
     * NOTE: using a default hologram assumes that this library is shaded
     *
     * @param name name of the plugin to use
     */
    public static void setPreferredHologramPlugin(String name) {
        Holograms holo = getHolograms(name);
        if (holo != null)
            defaultHolo = holo;
    }

    /**
     * Try to grab the handler for this specific hologram plugin.
     *
     * @param name plugin to useH
     * @return returns null if plugin is not enabled
     */
    public static Holograms getHolograms(String name) {
        if (name == null) return null;
        final String plugin = name.trim();
        return registeredHolograms.get(registeredHolograms.keySet().stream()
                .filter(type -> type.plugin.equalsIgnoreCase(plugin))
                .findFirst().orElse(null));
    }

    /**
     * Try to grab the handler for this specific hologram plugin.
     *
     * @param hologram plugin to use
     * @return returns null if plugin is not enabled
     */
    public static Holograms getHolograms(HologramType hologram) {
        return registeredHolograms.get(hologram);
    }

    /**
     * Grab the default hologram plugin. <br />
     * NOTE: using a default hologram assumes that this library is shaded
     *
     * @return returns null if no plugin enabled
     */
    public static Holograms getHolograms() {
        return defaultHolo;
    }

    /**
     * Grab a list of all supported hologram plugins that are loaded.
     *
     * @return an immutable collection of the loaded hologram, handler instances
     */
    public static Collection<Holograms> getRegisteredHolograms() {
        return Collections.unmodifiableCollection(registeredHolograms.values());
    }

    public static void createHologram(Location location, String line) {
        if (defaultHolo != null)
            defaultHolo.createHologram(location, line);
    }

    public static void createHologram(Location location, List<String> lines) {
        if (defaultHolo != null)
            defaultHolo.createHologram(location, lines);
    }

    public static void removeHologram(Location location) {
        if (defaultHolo != null)
            defaultHolo.removeHologram(location);
    }

    public static void removeAllHolograms() {
        if (defaultHolo != null)
            defaultHolo.removeAllHolograms();
    }

    public static void updateHologram(Location location, String line) {
        if (defaultHolo != null)
            defaultHolo.updateHologram(location, line);
    }

    public static void updateHologram(Location location, List<String> lines) {
        if (defaultHolo != null)
            defaultHolo.updateHologram(location, lines);
    }
}
