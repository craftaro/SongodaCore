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

    protected abstract void add(Location location, ArrayList<String> lines);

    protected abstract void remove(Location location);

    protected abstract void update(Location location, ArrayList<String> lines);

}
