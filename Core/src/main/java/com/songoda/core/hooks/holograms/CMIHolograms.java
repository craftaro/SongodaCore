package com.songoda.core.hooks.holograms;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import com.Zrips.CMI.Modules.Holograms.HologramManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CMIHolograms extends Holograms {

    private static CMI cmi;
    private static HologramManager cmiHologramManager;
    private static HashSet<String> ourHolograms = new HashSet();
    private static Method cmi_CMIHologram_getLines;
    private static boolean useOldMethod;

    static {
        try {
            useOldMethod = CMIHologram.class.getDeclaredField("lines").getDeclaringClass() == String[].class;
            cmi_CMIHologram_getLines = CMIHologram.class.getMethod("getLines");
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public CMIHolograms(Plugin plugin) {
        super(plugin);
        cmi = (CMI) Bukkit.getPluginManager().getPlugin("CMI");
        if (cmi != null) {
            cmiHologramManager = cmi.getHologramManager();
        }
    }

    @Override
    public String getName() {
        return "CMI";
    }

    @Override
    public boolean isEnabled() {
        return cmi != null && cmi.isEnabled();
    }

    @Override
    protected double defaultHeightOffset() {
        return 0.5;
    }

    @Override
    public void createHologram(Location location, List<String> lines) {
        createAt(fixLocation(location), lines);
    }

    @Override
    public void removeHologram(Location location) {
        location = fixLocation(location);
        final String id = locStr(location);
        CMIHologram holo = cmiHologramManager.getByName(id);
        if (holo != null) {
            cmiHologramManager.removeHolo(holo);
        }
        ourHolograms.remove(id);
    }

    @Override
    public void removeAllHolograms() {
        for (String id : ourHolograms) {
            CMIHologram holo = cmiHologramManager.getByName(id);
            if (holo != null) {
                cmiHologramManager.removeHolo(holo);
            }
        }
        ourHolograms.clear();
    }

    @Override
    public void updateHologram(Location location, List<String> lines) {
        location = fixLocation(location);
        CMIHologram holo = cmiHologramManager.getByName(locStr(location));
        if (holo != null) {
            // only update if there is a change to the text
            List<String> holoLines;
            try {
                if (useOldMethod) {
                    holoLines = Arrays.asList((String[]) cmi_CMIHologram_getLines.invoke(holo));
                } else {
                    holoLines = (List<String>) cmi_CMIHologram_getLines.invoke(holo);
                }
            } catch (Exception ex) {
                Logger.getLogger(CMIHolograms.class.getName()).log(Level.SEVERE, "CMI Hologram error!", ex);
                holoLines = Collections.EMPTY_LIST;
            }
            boolean isChanged = lines.size() != holoLines.size();
            if (!isChanged) {
                // double-check the lines
                for (int i = 0; !isChanged && i < lines.size(); ++i) {
                    isChanged = !holo.getLine(i).equals(lines.get(i));
                }
            }
            if (isChanged) {
                holo.setLines(lines);
                holo.update();
            }
            return;
        }
        createAt(location, lines);
    }

    @Override
    public void bulkUpdateHolograms(Map<Location, List<String>> hologramData) {
        for (Map.Entry<Location, List<String>> entry : hologramData.entrySet()) {
            updateHologram(entry.getKey(), entry.getValue());
        }
    }

    private String locStr(Location loc) {
        return String.format("%s-%d-%d-%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private void createAt(Location location, List<String> lines) {

        final String id = locStr(location);
        CMIHologram holo = new CMIHologram(id, location);
        holo.setLines(lines);

        cmiHologramManager.addHologram(holo);
        holo.update();

        if (!ourHolograms.contains(id)) {
            ourHolograms.add(id);
        }
    }
}
