package com.songoda.core.library.settings.editor;

import com.songoda.core.library.compatibility.LegacyMaterials;
import com.songoda.core.library.settings.Config;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigSelectionGUI extends AbstractGUI {

    private final JavaPlugin plugin;

    private final List<Config> configs = new ArrayList<>();

    public ConfigSelectionGUI(JavaPlugin plugin, Player player, Config... configs) {
        super(player);
        this.plugin = plugin;
        this.configs.addAll(Arrays.asList(configs));
        init("Settings Editor", 9);
    }

    @Override
    protected void constructGUI() {
        for (int i = 0; i < configs.size(); i++) {
            Config config = configs.get(i);
            createButton(i, LegacyMaterials.WRITABLE_BOOK.getMaterial(), "&9&l" + config.getConfigName());
            registerClickable(i, ((player1, inventory1, cursor, slot, type) ->
                    new ConfigCategoriesGUI(plugin, player, config, this)));
        }
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }
}
