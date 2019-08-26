package com.songoda.core.library.settings.editor;

import com.songoda.core.input.ChatPrompt;
import com.songoda.core.library.compatibility.LegacyMaterials;
import com.songoda.core.library.settings.Setting;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ConfigListEditorGUI extends AbstractGUI {

    private final JavaPlugin plugin;
    private final Setting setting;
    private final ConfigEditorGUI editor;

    private final List<String> stringList;

    public ConfigListEditorGUI(JavaPlugin plugin, Player player, Setting setting, ConfigEditorGUI editor) {
        super(player);
        this.plugin = plugin;
        this.setting = setting;
        this.stringList = setting.getStringList();
        this.editor = editor;
        init("Settings Editor", 54);
    }

    @Override
    protected void constructGUI() {
        resetClickables();
        inventory.clear();
        for (int i = 0; i < stringList.size(); i++) {
            int j = i;
            String item = stringList.get(i);
            createButton(i, LegacyMaterials.PAPER.getMaterial(), "&7" + item, "&cClick to remove.");
            registerClickable(i, ((player1, inventory1, cursor, slot, type) -> {
                stringList.remove(j);
                constructGUI();
            }));
        }
        createButton(45, LegacyMaterials.LAVA_BUCKET.getMaterial(), "&cDiscard Changes");
        registerClickable(45, ((player1, inventory1, cursor, slot, type) -> {
            editor.init("Settings Editor", editor.getInventory().getSize());
        }));

        createButton(49, LegacyMaterials.CHEST.getMaterial(), "&9Add Item");
        registerClickable(49, ((player1, inventory1, cursor, slot, type) -> {
            ChatPrompt prompt = ChatPrompt.showPrompt(plugin, player, "Enter your value.", event ->
                    stringList.add(event.getMessage().trim()));
            prompt.setOnClose(() -> init("Settings Editor", inventory.getSize()));
        }));

        createButton(53, LegacyMaterials.REDSTONE.getMaterial(), "&aSave");
        registerClickable(53, ((player1, inventory1, cursor, slot, type) -> {
            setting.getConfig().set(setting.getCompleteKey(), stringList);
            editor.init("Settings Editor", editor.getInventory().getSize());
        }));
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }

}
