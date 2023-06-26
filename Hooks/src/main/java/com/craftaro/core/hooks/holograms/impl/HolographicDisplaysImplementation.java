package com.craftaro.core.hooks.holograms.impl;

import com.craftaro.core.hooks.holograms.AbstractHologram;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class HolographicDisplaysImplementation extends AbstractHologram {

    private final Map<String, Hologram> holograms = new HashMap<>();
    private final Plugin plugin;

    private HolographicDisplaysAPI api;
    public HolographicDisplaysImplementation(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getHookName() {
        return "HolographicDisplays";
    }

    @Override
    public boolean enableHook() {
        this.api = HolographicDisplaysAPI.get(plugin);
        return true;
    }

    @Override
    public double getHeightOffset() {
        return 1;
    }

    @Override
    public String createHologram(Location location, List<String> lines) {
        String randomId = UUID.randomUUID().toString();
        if (holograms.containsKey(randomId)) {
            // should never happen
            return null;
        }

        location = fixLocation(location);
        Hologram hologram = api.createHologram(location);

        for (String line : lines) {
            hologram.getLines().appendText(line);
        }

        holograms.put(randomId, hologram);
        return randomId;
    }

    @Override
    public void deleteHologram(String id) {
        Hologram hologram = holograms.remove(id);
        if (hologram != null) {
            hologram.delete();
        }
    }

    @Override
    public void updateHologram(String id, List<String> lines) {
        bulkUpdateHolograms(Collections.singletonMap(id, lines));
    }

    @Override
    public void bulkUpdateHolograms(Map<String, List<String>> hologramData) {
        for (Map.Entry<String, List<String>> entry : hologramData.entrySet()) {
            String id = entry.getKey();
            List<String> lines = entry.getValue();

            Hologram hologram = holograms.get(id);

            // only update if there is a change to the text
            boolean isChanged = lines.size() != hologram.getLines().size();

            if (!isChanged) {
                // double-check the lines
                for (int i = 0; !isChanged && i < lines.size(); ++i) {
                    isChanged = !hologram.getLines().get(i).toString().equals(lines.get(i));
                }
            }

            if (isChanged) {
                hologram.getLines().clear();

                for (String line : lines) {
                    hologram.getLines().appendText(line);
                }
            }
        }
    }

    @Override
    public void removeAllHolograms() {
        holograms.values().forEach(Hologram::delete);
    }

    @Override
    public boolean isHologramLoaded(String id) {
        return holograms.get(id) != null;
    }
}
