package com.songoda.core.library.hologram.holograms;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

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

    public abstract void add(Location location, ArrayList<String> lines);

    public abstract void remove(Location location);

    public abstract void update(Location location, ArrayList<String> lines);

    void fixLocation(Location location) {
        location.add(x, y, z);
    }

}
