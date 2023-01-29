package com.songoda.core.hooks.holograms;

import com.songoda.core.hooks.PluginHook;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public abstract class AbstractHologram implements PluginHook {
    protected double xOffset = 0.5;
    protected double yOffset = 0.5;
    protected double zOffset = 0.5;

    protected abstract double getHeightOffset();
    public abstract String createHologram(Location location, List<String> lines);
    public abstract void deleteHologram(String id);
    public abstract void updateHologram(String id, List<String> lines);
    public abstract void bulkUpdateHolograms(Map<String, List<String>> hologramData);
    public abstract void removeAllHolograms();
    public abstract boolean isHologramLoaded(String id);

    public AbstractHologram setPositionOffset(double x, double y, double z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;

        return this;
    }

    protected final Location fixLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return location.clone().add((x - (int) x) + xOffset, (y - (int) y) + yOffset + getHeightOffset(), (z - (int) z) + zOffset);
    }

}
