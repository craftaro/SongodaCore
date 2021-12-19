package com.songoda.core.hooks.holograms;

import com.sainttx.holograms.api.Hologram;
import com.sainttx.holograms.api.HologramPlugin;
import com.sainttx.holograms.api.line.HologramLine;
import com.sainttx.holograms.api.line.TextLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HologramsHolograms extends Holograms {
    private final HologramPlugin hologramPlugin;
    private final Set<String> ourHolograms = new HashSet<>();

    public HologramsHolograms(Plugin plugin) {
        super(plugin);

        hologramPlugin = (HologramPlugin) Bukkit.getPluginManager().getPlugin("Holograms");
    }

    @Override
    public String getName() {
        return "Holograms";
    }

    @Override
    public boolean isEnabled() {
        return hologramPlugin.isEnabled();
    }

    @Override
    protected double defaultHeightOffset() {
        return 0.5;
    }

    @Override
    public void createHologram(String id, Location location, List<String> lines) {
        createAt(id, fixLocation(location), lines);
    }

    @Override
    public void removeHologram(String id) {
        Hologram hologram = hologramPlugin.getHologramManager().getHologram(id);

        if (hologram != null) {
            hologram.despawn();
            hologramPlugin.getHologramManager().removeActiveHologram(hologram);
        }

        ourHolograms.remove(id);
    }

    @Override
    public void removeAllHolograms() {
        for (String id : ourHolograms) {
            Hologram hologram = hologramPlugin.getHologramManager().getHologram(id);

            if (hologram != null) {
                hologram.despawn();
                hologramPlugin.getHologramManager().removeActiveHologram(hologram);
            }
        }

        ourHolograms.clear();
    }

    @Override
    public boolean isHologramLoaded(String id) {
        return hologramPlugin.getHologramManager().getHologram(id) != null;
    }

    @Override
    public void updateHologram(String id, List<String> lines) {
        Hologram hologram = hologramPlugin.getHologramManager().getHologram(id);

        if (hologram != null) {
            hologram.spawn();

            // only update if there is a change to the text
            boolean isChanged = lines.size() != hologram.getLines().size();

            if (!isChanged) {
                // double-check the lines
                for (int i = 0; !isChanged && i < lines.size(); ++i) {
                    isChanged = !hologram.getLine(i).getRaw().equals(lines.get(i));
                }
            }

            if (isChanged) {
                for (HologramLine line : hologram.getLines().toArray(new HologramLine[0])) {
                    hologram.removeLine(line);
                }
                for (String line : lines) {
                    hologram.addLine(new TextLine(hologram, line));
                }
            }

            return;
        }
    }

    @Override
    public void bulkUpdateHolograms(Map<String, List<String>> hologramData) {
        for (Map.Entry<String, List<String>> entry : hologramData.entrySet()) {
            updateHologram(entry.getKey(), entry.getValue());
        }
    }

    private void createAt(String id, Location location, List<String> lines) {
        Hologram hologram = new Hologram(id, location);

        for (String line : lines) {
            hologram.addLine(new TextLine(hologram, line));
        }

        hologramPlugin.getHologramManager().addActiveHologram(hologram);

        ourHolograms.add(id);
    }
}
