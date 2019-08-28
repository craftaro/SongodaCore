package com.songoda.core.core;

import com.songoda.core.SongodaCore;
import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.ClickType;

final class SongodaCoreOverviewGUI extends Gui {

    private final SongodaCore update;

    protected SongodaCoreOverviewGUI(SongodaCore update) {
        this.update = update;
        List<PluginInfo> plugins = update.getPlugins();
        // could do pages, too, but don't think we'll have that many at a time for a while
        int max = (int) Math.ceil(plugins.size() / 9.);
        setRows(max);
        setTitle("Songoda Plugins");

        // TODO: this could use some decorating

        for (int i = 0; i < plugins.size(); i++) {
            PluginInfo plugin = plugins.get(i);
            if (plugin.hasUpdate()) {
                setButton(i, GuiUtils.createButtonItem(plugin.icon != null ? plugin.icon : LegacyMaterials.STONE,
                        ChatColor.GOLD + plugin.getJavaPlugin().getName(),
                        ChatColor.GRAY + "Latest Version: " + plugin.getLatestVersion(),
                        ChatColor.GRAY + "Installed Version: " + plugin.getJavaPlugin().getDescription().getVersion(),
                        "",
                        "Change log:",
                        plugin.getChangeLog(),
                        "",
                        ChatColor.GOLD + "Click for the marketplace page link."
                ),
                        ClickType.LEFT, (event) -> event.player.sendMessage(plugin.getMarketplaceLink()));
                highlightItem(i);
            } else {
                setButton(i, GuiUtils.createButtonItem(plugin.icon != null ? plugin.icon : LegacyMaterials.STONE,
                        ChatColor.GOLD + plugin.getJavaPlugin().getName(),
                        ChatColor.GRAY + "Installed Version: " + plugin.getJavaPlugin().getDescription().getVersion(),
                        "",
                        ChatColor.GOLD + "Click for the marketplace page link."
                ),
                        ClickType.LEFT, (event) -> event.player.sendMessage(plugin.getMarketplaceLink()));
            }
        }
    }
}
