package com.songoda.core.library.hologram.holograms;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public abstract class Hologram {

    protected double x = 0.5;
    protected double y = 1.5;
    protected double z = 0.5;

    protected final JavaPlugin plugin;

    public Hologram(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Hologram setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public abstract String getName();

    public void add(Location location, String line) {
        add(location, Collections.singletonList(line));
    }

    public abstract void add(Location location, List<String> lines);

    public abstract void remove(Location location);

    public void update(Location location, String line) {
        update(location, Collections.singletonList(line));
    }

    public abstract void update(Location location, List<String> lines);

    void fixLocation(Location location) {
        location.add(x, y, z);
    }

}
