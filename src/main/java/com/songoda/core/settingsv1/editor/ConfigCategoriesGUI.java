package com.songoda.core.settingsv1.editor;

import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.settingsv1.Category;
import com.songoda.core.settingsv1.Config;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigCategoriesGUI extends AbstractGUI {

    private final JavaPlugin plugin;
    private final Config config;
    private final ConfigSelectionGUI selection;

    public ConfigCategoriesGUI(JavaPlugin plugin, Player player, Config config, ConfigSelectionGUI selection) {
        super(player);
        this.plugin = plugin;
        this.config = config;
        this.selection = selection;
        init("Settings Editor", 54);
    }

    @Override
    protected void constructGUI() {
        createButton(0, LegacyMaterials.getMaterial("OAK_FENCE_GATE").getMaterial(), "Back");
        registerClickable(0, ((player1, inventory1, cursor, slot, type) ->
                selection.init("Settings Editor", selection.getInventory().getSize())));

        for (int i = 9; i - 9 < config.getCategories().size(); i++) {
            Category category = config.getCategories().get(i - 9);
            createButton(i, LegacyMaterials.WRITABLE_BOOK.getMaterial(), "&9&l" + category.getKey());
            registerClickable(i, ((player1, inventory1, cursor, slot, type) ->
                    new ConfigEditorGUI(plugin, player, category, null, this)));
        }
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }

    public Config getConfig() {
        return config;
    }
}
