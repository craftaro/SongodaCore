package com.songoda.core.hooks.holograms.impl;

import com.songoda.core.hooks.holograms.AbstractHologram;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Location;

import java.util.*;

public class DecentHologramsImplementation extends AbstractHologram {
    private final Map<String, Hologram> holograms = new HashMap<>();

    @Override
    public String getHookName() {
        return "DecentHolograms";
    }

    @Override
    public boolean enableHook() {
        return true;
    }

    @Override
    protected double getHeightOffset() {
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

        if (isHologramLoaded(randomId)) {
            return null;
        }

        Hologram hologram = DHAPI.createHologram(randomId, location, lines);
        holograms.put(randomId, hologram);
        return randomId;
    }

    @Override
    public void deleteHologram(String id) {
        Hologram hologram = holograms.remove(id);

        if (hologram != null) {
            hologram.delete();
            DecentHologramsAPI.get().getHologramManager().removeHologram(id);
        }
    }

    @Override
    public void updateHologram(String id, List<String> lines) {
        Hologram hologram = DHAPI.getHologram(id);

        if (hologram != null) {
            DHAPI.setHologramLines(hologram, lines);
        }
    }

    @Override
    public void bulkUpdateHolograms(Map<String, List<String>> hologramData) {
        for (Map.Entry<String, List<String>> entry : hologramData.entrySet()) {
            updateHologram(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void removeAllHolograms() {
        holograms.values().forEach(hologram -> {
            hologram.delete();
            DecentHologramsAPI.get().getHologramManager().removeHologram(hologram.getId());
        });
    }

    @Override
    public boolean isHologramLoaded(String id) {
        return DHAPI.getHologram(id) != null;
    }
}
