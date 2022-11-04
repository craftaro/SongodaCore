package com.songoda.core.hooks.protection;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlotSquaredProtection extends Protection {

    PlotSquared plotSquared;

    public PlotSquaredProtection(Plugin plugin) {
        super(plugin);
        plotSquared = PlotSquared.get();
    }

    @Override
    public String getName() {
        return "PlotSquared";
    }

    @Override
    public boolean isEnabled() {
        return plotSquared != null;
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return plotSquared.getPlotAreaManager().getApplicablePlotArea(com.plotsquared.core.location.Location.at(location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ())).getPlots().stream().anyMatch(p -> p.isAdded(player.getUniqueId()));
    }

    @Override
    public boolean canBreak(Player player, Location location) {
        return plotSquared.getPlotAreaManager().getApplicablePlotArea(com.plotsquared.core.location.Location.at(location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ())).getPlots().stream().anyMatch(p -> p.isAdded(player.getUniqueId()));
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        return plotSquared.getPlotAreaManager().getApplicablePlotArea(com.plotsquared.core.location.Location.at(location.getWorld().getName(), (int) location.getX(), (int) location.getY(), (int) location.getZ())).getPlots().stream().anyMatch(p -> p.isAdded(player.getUniqueId()));
    }
}
