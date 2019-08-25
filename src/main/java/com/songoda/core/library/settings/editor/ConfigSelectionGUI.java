package com.songoda.core.library.settings.editor;

import com.songoda.core.library.settings.Config;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigSelectionGUI extends AbstractGUI {

    private final List<Config> configs = new ArrayList<>();

    public ConfigSelectionGUI(Player player, Config... configs) {
        super(player);
        this.configs.addAll(Arrays.asList(configs));
        init("test", 54);
    }

    @Override
    protected void constructGUI() {
        for (int i = 0; i < configs.size(); i++) {
            Config config = configs.get(i);
            createButton(i, Material.STONE, config.getConfigName());
            registerClickable(i, ((player1, inventory1, cursor, slot, type) -> {
                new ConfigCategoriesGUI(player, config);
            }));
        }
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }
}
