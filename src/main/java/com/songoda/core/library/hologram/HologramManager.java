package com.songoda.core.library.hologram;

import com.songoda.core.library.hologram.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;

import java.util.*;

public class HologramManager {

    private final static Map<HologramType, Hologram> registeredHolograms = new HashMap<>();
    private static Hologram defaultHolo = null;

    /**
     * Load all supported hologram plugins. <br />
     * Note: This method should be called in your plugin's onEnable() section
     */
    public static void load() {
        if (!registeredHolograms.isEmpty()) return;

        PluginManager pluginManager = Bukkit.getPluginManager();

        for (HologramType type : HologramType.values()) {
            if (pluginManager.isPluginEnabled(type.plugin)) {
                Hologram holo = type.getInstance();
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
     * @param name name of the plugin to use
     */
    public static void setPreferredHologram(String name) {
        Hologram holo = getHologram(name);
        if (holo != null)
            defaultHolo = holo;
    }

    /**
     * Try to grab the handler for this specific hologram plugin.
     *
     * @param name plugin to useH
     * @return returns null if plugin is not enabled
     */
    public static Hologram getHologram(String name) {
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
    public static Hologram getHologram(HologramType hologram) {
        return registeredHolograms.get(hologram);
    }

    /**
     * Grab the default hologram plugin. <br />
     * NOTE: using a default hologram assumes that this library is shaded
     *
     * @return returns null if no plugin enabled
     */
    public static Hologram getHologram() {
        return defaultHolo;
    }

    /**
     * Grab a list of all supported hologram plugins.
     *
     * @return an immutable collection of the loaded hologram, handler instances
     */
    public static Collection<Hologram> getRegisteredHolograms() {
        return Collections.unmodifiableCollection(registeredHolograms.values());
    }

    public static void add(Location location, String line) {
        if (defaultHolo != null)
            defaultHolo.add(location, line);
    }

    public static void add(Location location, List<String> lines) {
        if (defaultHolo != null)
            defaultHolo.add(location, lines);
    }

    public static void remove(Location location) {
        if (defaultHolo != null)
            defaultHolo.remove(location);
    }

    public static void update(Location location, String line) {
        if (defaultHolo != null)
            defaultHolo.update(location, line);
    }

    public static void update(Location location, List<String> lines) {
        if (defaultHolo != null)
            defaultHolo.update(location, lines);
    }

}
