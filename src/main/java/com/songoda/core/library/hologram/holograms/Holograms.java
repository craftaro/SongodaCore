package com.songoda.core.library.hologram.holograms;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public abstract class Holograms {

    protected double xOffset = 0.5;
    protected double yOffset = 1.5;
    protected double zOffset = 0.5;

    protected final JavaPlugin plugin;

    public Holograms(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public Holograms setPositionOffset(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
        return this;
    }

    /**
     * Center and offset this location
     * 
     * @param location location to offset
     * @return copy-safe location with the applied offset.
     */
    protected final Location fixLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return location.clone().add((x - (int) x) + xOffset, (y - (int) y) + yOffset, (z - (int) z) + zOffset);
    }

    public abstract String getName();

    public void createHologram(Location location, String line) {
        createHologram(location, Collections.singletonList(line));
    }

    public abstract void createHologram(Location location, List<String> lines);

    public abstract void removeHologram(Location location);

    public void updateHologram(Location location, String line) {
        updateHologram(location, Collections.singletonList(line));
    }

    public abstract void updateHologram(Location location, List<String> lines);

}
