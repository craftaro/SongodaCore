package com.songoda.update.gui;

import com.songoda.update.Plugin;
import com.songoda.update.SongodaUpdate;
import com.songoda.update.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class GUIOverview extends AbstractGUI {

    private final SongodaUpdate update;

    public GUIOverview(SongodaUpdate update, Player player) {
        super(player);
        this.update = update;

        init("Songoda Update", 36);
    }

    @Override
    protected void constructGUI() {
        List<Plugin> plugins = update.getPlugins();
        for (int i = 0; i < plugins.size(); i++) {
            Plugin plugin = plugins.get(i);

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
