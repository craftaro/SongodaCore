package com.songoda.core.library.settings.editor;

import com.songoda.core.library.settings.Category;
import com.songoda.core.library.settings.Config;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigCategoriesGUI extends AbstractGUI {

    private final Config config;

    public ConfigCategoriesGUI(Player player, Config config) {
        super(player);
        this.config = config;
        init("test", 54);
    }

    @Override
    protected void constructGUI() {
        for (int i = 0; i < config.getCategories().size(); i++) {
            Category category = config.getCategories().get(i);
            createButton(i, Material.STONE, category.getKey());
        }
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }
}
