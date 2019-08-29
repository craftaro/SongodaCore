package com.songoda.core.settings.editor;

import com.songoda.core.input.ChatPrompt;
import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.settings.FoundSetting;
import com.songoda.core.settings.Narrow;
import com.songoda.core.settings.Setting;
import com.songoda.core.utils.gui.AbstractGUI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;

public class ConfigEditorGUI extends AbstractGUI {

    private final JavaPlugin plugin;
    private final Narrow narrow;
    private final String narrowed;
    private final ConfigCategoriesGUI categories;

    public ConfigEditorGUI(JavaPlugin plugin, Player player, Narrow narrow, String narrowed, ConfigCategoriesGUI categories) {
        super(player);
        this.plugin = plugin;
        this.narrow = narrow;
        this.narrowed = narrowed;
        this.categories = categories;
        init("Settings Editor", 54);
    }

    @Override
    protected void constructGUI() {
        resetClickables();

        createButton(0, LegacyMaterials.getMaterial("OAK_FENCE_GATE").getMaterial(), "Back");
        registerClickable(0, ((player1, inventory1, cursor, slot, type) ->
                categories.init("Settings Editor", categories.getInventory().getSize())));

        List<String> ran = new ArrayList<>();
        int j = 9;
        for (int i = 0; i < narrow.getSettings().size(); i++) {
            FoundSetting setting = narrow.getSettings().get(i);
            boolean canNarrow = setting.getKey().replace(narrowed == null ? "" : narrowed + ".", "").contains(".");
            String key = canNarrow ? splitByLast(setting.getKey()) : setting.getKey();

            if (!ran.contains(key)) {
                if (canNarrow) {
                    createButton(j, LegacyMaterials.BOOK.getMaterial(), "&9&l" + key);
                    registerClickable(j, ((player1, inventory1, cursor, slot, type) ->
                            new ConfigEditorGUI(plugin, player1, narrow.narrow(key), key, categories)));
                } else {
                    FileConfiguration config = setting.getConfig();
                    Material material = Material.STONE;

                    ArrayList<String> list = new ArrayList<>();
                    StringBuilder value = new StringBuilder("&a");
                    if (config.isString(setting.getCompleteKey())) {
                        value.append(setting.getString());
                        material = LegacyMaterials.PAPER.getMaterial();
                        registerClickable(j, ((player1, inventory1, cursor, slot, type) -> {
                            ChatPrompt prompt = ChatPrompt.showPrompt(plugin, player, "Enter your new value.", event ->
                                    config.set(setting.getCompleteKey(), event.getMessage().trim()));
                            prompt.setOnClose(() -> init("Settings Editor", inventory.getSize()));
                        }));
                    } else if (isNumber(setting)) {
                        material = LegacyMaterials.CLOCK.getMaterial();
                        registerClickable(j, ((player1, inventory1, cursor, slot, type) -> {
                            ChatPrompt prompt = ChatPrompt.showPrompt(plugin, player, "Enter your new value.", event -> {
                                try {
                                    if (config.isInt(setting.getCompleteKey())) {
                                        config.set(setting.getCompleteKey(), Integer.parseInt(event.getMessage().trim()));
                                    } else if (config.isDouble(setting.getCompleteKey())) {
                                        config.set(setting.getCompleteKey(), Double.parseDouble(event.getMessage().trim()));
                                    } else if (config.isLong(setting.getCompleteKey())) {
                                        config.set(setting.getCompleteKey(), Long.parseLong(event.getMessage().trim()));
                                    }
                                } catch (NumberFormatException e) {
                                    player1.sendMessage(ChatColor.RED + "Error: \"" + event.getMessage().trim() + "\" is not a number!");
                                }
                            });
                            prompt.setOnClose(() -> init("Settings Editor", inventory.getSize()));
                        }));
                        if (config.isInt(setting.getCompleteKey())) {
                            value.append(setting.getInt());
                        } else if (config.isDouble(setting.getCompleteKey())) {
                            value.append(setting.getDouble());
                        } else if (config.isLong(setting.getCompleteKey())) {
                            value.append(setting.getLong());
                        }
                    } else if (config.isBoolean(setting.getCompleteKey())) {
                        value.append(setting.getBoolean());
                        material = LegacyMaterials.LEVER.getMaterial();
                        registerClickable(j, ((player1, inventory1, cursor, slot, type) -> {
                            config.set(setting.getCompleteKey(), !setting.getBoolean());
                            constructGUI();
                        }));
                    } else if (config.isList(setting.getCompleteKey())) {
                        value.append(setting.getStringList());
                        material = LegacyMaterials.WRITABLE_BOOK.getMaterial();
                        registerClickable(j, ((player1, inventory1, cursor, slot, type) -> {
                            new ConfigListEditorGUI(plugin, player, setting, this);
                        }));
                    } else {
                        value.append("&cPreview Failed");
                    }
                    list.add(value.toString());

                    list.addAll(Arrays.asList(setting.getComments()));
                    createButton(j, material, "&c&l" + key, list);
                }
                ran.add(key);
                j++;
            }
        }
        categories.getConfig().save();
    }

    @Override
    protected void registerClickables() {

    }

    @Override
    protected void registerOnCloses() {

    }

    private boolean isNumber(Setting setting) {
        FileConfiguration config = setting.getConfig();
        return config.isInt(setting.getCompleteKey())
                || config.isDouble(setting.getCompleteKey())
                || config.isLong(setting.getCompleteKey());
    }

    private String splitByLast(String toSplit) {
        int index = toSplit.lastIndexOf(".");
        return toSplit.substring(0, index);
    }
}
