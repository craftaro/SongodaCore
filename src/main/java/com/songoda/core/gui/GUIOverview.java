package com.songoda.core.gui;

import com.songoda.core.PluginInfo;
import com.songoda.core.SongodaCore;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class GUIOverview extends AbstractGUI {

    private final SongodaCore update;

    public GUIOverview(SongodaCore update, Player player) {
        super(player);
        this.update = update;

        init("Songoda Update", 36);
    }

    @Override
    protected void constructGUI() {
        List<PluginInfo> plugins = update.getPlugins();
        for (int i = 0; i < plugins.size(); i++) {
            PluginInfo plugin = plugins.get(i);

            createButton(i + 9, Material.STONE, "&6" + plugin.getJavaPlugin().getName(),
                    "&7Latest Version: " + plugin.getLatestVersion(),
                    "&7Installed Version: " + plugin.getJavaPlugin().getDescription().getVersion(),
                    "",
                    "Change log:",
                    plugin.getChangeLog(),
                    "",
                    "&6Click for the marketplace page link.");

            registerClickable(i + 9, ((player1, inventory1, cursor, slot, type) ->
                    player.sendMessage(plugin.getMarketplaceLink())));

        }
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }
}
