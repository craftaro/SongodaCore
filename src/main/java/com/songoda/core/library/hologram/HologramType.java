package com.songoda.core.library.hologram;

import com.songoda.core.library.hologram.holograms.Hologram;
import com.songoda.core.library.hologram.holograms.HolographicDisplaysHologram;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public enum HologramType {

    HOLOGRAPHIC_DISPLAYS("HolographicDisplays", HolographicDisplaysHologram.class);

    public final String plugin;
    protected final Class managerClass;

    private HologramType(String plugin, Class managerClass) {
        this.plugin = plugin;
        this.managerClass = managerClass;
    }

    protected Hologram getInstance(JavaPlugin javaPlugin) {
        try {
            return (Hologram) managerClass.getDeclaredConstructor(JavaPlugin.class).newInstance(javaPlugin);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unexpected Error while creating a new Hologram Manager for " + name(), ex);
        }
        return null;
    }
}
