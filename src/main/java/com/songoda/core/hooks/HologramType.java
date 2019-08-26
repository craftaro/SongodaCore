package com.songoda.core.hooks;

import com.songoda.core.hooks.holograms.Holograms;
import com.songoda.core.hooks.holograms.HologramsHolograms;
import com.songoda.core.hooks.holograms.HolographicDisplaysHolograms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public enum HologramType {

    HOLOGRAPHIC_DISPLAYS("HolographicDisplays", HolographicDisplaysHolograms.class),
    HOLOGRAMS("Holograms", HologramsHolograms.class)
    ;

    public final String plugin;
    protected final Class managerClass;

    private HologramType(String plugin, Class managerClass) {
        this.plugin = plugin;
        this.managerClass = managerClass;
    }

    protected Holograms getInstance(JavaPlugin javaPlugin) {
        try {
            return (Holograms) managerClass.getDeclaredConstructor(JavaPlugin.class).newInstance(javaPlugin);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unexpected Error while creating a new Hologram Manager for " + name(), ex);
        }
        return null;
    }
}
