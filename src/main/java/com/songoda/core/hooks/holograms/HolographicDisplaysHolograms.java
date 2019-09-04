package com.songoda.core.hooks.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HolographicDisplaysHolograms extends Holograms {

    public HolographicDisplaysHolograms(Plugin plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "HolographicDisplays";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected double defaultHeightOffset() {
        return 1;
    }

    @Override
    public void createHologram(Location location, List<String> lines) {
        createAt(fixLocation(location), lines);
    }

    @Override
    public void removeHologram(Location location) {
        location = fixLocation(location);
        for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
            if (hologram.getX() != location.getX()
                    || hologram.getY() != location.getY()
                    || hologram.getZ() != location.getZ()) continue;
            hologram.delete();
        }
    }

    @Override
    public void updateHologram(Location location, List<String> lines) {
        location = fixLocation(location);
        for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
            if (hologram.getX() != location.getX()
                    || hologram.getY() != location.getY()
                    || hologram.getZ() != location.getZ()) continue;
            hologram.clearLines();
            for (String line : lines) {
                hologram.appendTextLine(line);
            }
            return;
        }
        createAt(location, lines);
    }

    private void createAt(Location location, List<String> lines) {
                Hologram hologram = HologramsAPI.createHologram(plugin, location);
        for (String line : lines) {
            hologram.appendTextLine(line);
        }
    }

    @Override
    public void removeAllHolograms() {
        HologramsAPI.getHolograms(plugin).stream().forEach(x -> x.delete());
    }

}
