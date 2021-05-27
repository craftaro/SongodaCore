package com.songoda.core.hooks.holograms;

import com.songoda.core.hooks.Hook;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Holograms implements Hook {

    protected double xOffset = 0.5;
    protected double yOffset = 0.5;
    protected double zOffset = 0.5;

    protected final Plugin plugin;

    public Holograms(Plugin plugin) {
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
        return location.clone().add((x - (int) x) + xOffset, (y - (int) y) + yOffset + defaultHeightOffset(), (z - (int) z) + zOffset);
    }

    protected abstract double defaultHeightOffset();

    public void createHologram(Location location, String line) {
        createHologram(location, Collections.singletonList(line));
    }

    public abstract void createHologram(Location location, List<String> lines);

    public abstract void removeHologram(Location location);

    public void updateHologram(Location location, String line) {
        updateHologram(location, Collections.singletonList(line));
    }

    public abstract void updateHologram(Location location, List<String> lines);

    public abstract void bulkUpdateHolograms(Map<Location, List<String>> hologramData);

    public abstract void removeAllHolograms();
}
