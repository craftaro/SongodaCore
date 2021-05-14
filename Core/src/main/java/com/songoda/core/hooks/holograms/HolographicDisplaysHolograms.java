package com.songoda.core.hooks.holograms;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        bulkUpdateHolograms(Collections.singletonMap(location, lines));
    }

    @Override
    public void bulkUpdateHolograms(Map<Location, List<String>> hologramData) {
        Collection<Hologram> holograms = HologramsAPI.getHolograms(plugin);

        outerFor:
        for (Map.Entry<Location, List<String>> entry : hologramData.entrySet()) {
            Location location = fixLocation(entry.getKey());
            List<String> lines = entry.getValue();

            for (Hologram hologram : holograms) {
                if (hologram.getX() != location.getX()
                        || hologram.getY() != location.getY()
                        || hologram.getZ() != location.getZ()) continue;

                // only update if there is a change to the text
                boolean isChanged = lines.size() != hologram.size();

                if (!isChanged) {
                    // double-check the lines
                    for (int i = 0; !isChanged && i < lines.size(); ++i) {
                        isChanged = !(hologram.getLine(i) instanceof TextLine) ||
                                !((TextLine) hologram.getLine(i)).getText().equals(lines.get(i));
                    }
                }

                if (isChanged) {
                    hologram.clearLines();

                    for (String line : lines) {
                        hologram.appendTextLine(line);
                    }
                }

                continue outerFor;
            }

            createAt(location, lines);
        }
    }

    private void createAt(Location location, List<String> lines) {
        Hologram hologram = HologramsAPI.createHologram(plugin, location);
        for (String line : lines) {
            hologram.appendTextLine(line);
        }
    }

    @Override
    public void removeAllHolograms() {
        HologramsAPI.getHolograms(plugin).forEach(Hologram::delete);
    }
}
