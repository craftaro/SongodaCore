package com.craftaro.core.hooks.holograms.impl;

import com.craftaro.core.hooks.holograms.AbstractHologram;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class DummyHologramImplementation extends AbstractHologram {
    @Override
    public String getHookName() {
        return "None";
    }

    @Override
    public boolean enableHook() {
        return true;
    }

    @Override
    protected double getHeightOffset() {
        return 0;
    }

    @Override
    public String createHologram(Location location, List<String> lines) {
        return null;
    }

    @Override
    public void deleteHologram(String id) {

    }

    @Override
    public void updateHologram(String id, List<String> lines) {

    }

    @Override
    public void bulkUpdateHolograms(Map<String, List<String>> hologramData) {

    }

    @Override
    public void removeAllHolograms() {

    }

    @Override
    public boolean isHologramLoaded(String id) {
        return false;
    }
}
