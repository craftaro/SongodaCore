package com.songoda.core.configuration.editor;

import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.gui.SimplePagedGui;
import com.songoda.core.input.ChatPrompt;
import com.songoda.core.utils.ItemUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigEditorGui extends SimplePagedGui {

    final JavaPlugin plugin;
    final String file;
    final MemoryConfiguration config;
    final ConfigurationSection node;
    Method configSection_getCommentString = null;
    List<String> sections = new ArrayList();
    List<String> settings = new ArrayList();

    public ConfigEditorGui(JavaPlugin plugin, Gui parent, String file, MemoryConfiguration config) {
        this(plugin, parent, file, config, config);
        setOnClose((gui) -> save());
    }

    public ConfigEditorGui(JavaPlugin plugin, Gui parent, String file, MemoryConfiguration config, ConfigurationSection node) {
        super(parent);
        this.plugin = plugin;
        this.file = file;
        this.config = config;
        this.node = node;
        this.blankItem = GuiUtils.getBorderItem(LegacyMaterials.LIGHT_GRAY_STAINED_GLASS_PANE);

        // if we have a ConfigSection, we can also grab comments
        try {
            configSection_getCommentString = node.getClass().getDeclaredMethod("getCommentString", String.class);
        } catch (Exception ex) {
        }

        // decorate header
        this.setTitle(ChatColor.DARK_BLUE + file);
        this.setUseHeader(true);
        headerBackItem = footerBackItem = GuiUtils.getBorderItem(LegacyMaterials.GRAY_STAINED_GLASS_PANE.getItem());
        final String path = node.getCurrentPath();
        this.setItem(4, configItem(LegacyMaterials.FILLED_MAP, path, config, path, null));
        this.setButton(8, GuiUtils.createButtonItem(LegacyMaterials.OAK_DOOR, "Exit"), (event) -> event.player.closeInventory());

        // compile list of settings
        for (String key : node.getKeys(false)) {
            if (node.isConfigurationSection(key)) {
                sections.add(key);
            } else {
                settings.add(key);
            }
        }

        // next we need to display the config settings
        int index = 9;
        for (final String sectionKey : sections) {
            this.setButton(index++, configItem(LegacyMaterials.WRITABLE_BOOK, ChatColor.YELLOW + sectionKey, node, sectionKey, "Click to open this section"),
                    (event) -> event.manager.showGUI(event.player, new ConfigEditorGui(plugin, this, file, config, node.getConfigurationSection(sectionKey))));
        }

        // todo: display values of settings in gui, too
        // now display individual settings
        for (final String settingKey : settings) {
            final Object val = node.get(settingKey);
            if(val == null) continue;
            else if(val instanceof Boolean) {
                // toggle switch
                this.setButton(index, configItem(LegacyMaterials.LEVER, ChatColor.YELLOW + settingKey, node, settingKey, String.valueOf((Boolean) val), "Click to toggle this setting"),
                    (event) -> this.toggle(event.slot, settingKey));
                if((Boolean) val) {
                    this.highlightItem(index);
                }
            } else if (isNumber(val)) {
                // number dial
                this.setButton(index, configItem(LegacyMaterials.CLOCK, ChatColor.YELLOW + settingKey, node, settingKey, String.valueOf((Number) val), "Click to edit this setting"),
                        (event) -> {
                            event.gui.exit();
                            ChatPrompt prompt = ChatPrompt.showPrompt(plugin, event.player, "Enter your new value.", response -> {
                                if (!setNumber(event.slot, settingKey, response.getMessage().trim())) {
                                    event.player.sendMessage(ChatColor.RED + "Error: \"" + response.getMessage().trim() + "\" is not a number!");
                                }
                            });
                            prompt.setOnClose(() -> event.manager.showGUI(event.player, this));
                        });
            }

            ++index;
        }

    }
    
    void toggle(int clickCell, String path) {
        boolean val = !node.getBoolean(path);
        node.set(path, val);
        if(val) {
            inventory.setItem(clickCell, ItemUtils.addGlow(inventory.getItem(clickCell)));
        } else {
            removeHighlight(clickCell);
        }
    }

    boolean setNumber(int clickCell, String path, String input) {
        try {
            if (node.isInt(path)) {
                node.set(path, Integer.parseInt(input));
            } else if (node.isDouble(path)) {
                node.set(path, Double.parseDouble(input));
            } else if (node.isLong(path)) {
                node.set(path, Long.parseLong(input));
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    void save() {
        // could also check and call saveChanges()
        if (config instanceof FileConfiguration) {
            try {
                ((FileConfiguration) config).save(new File(plugin.getDataFolder(), file));
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Failed to save config changes to " + file, ex);
                return;
            }
        }
        plugin.reloadConfig();
    }

    private boolean isNumber(Object value) {
        return value != null && (
                value instanceof Long
                || value instanceof Integer
                || value instanceof Float
                || value instanceof Double);
    }

    ItemStack configItem(LegacyMaterials type, String name, ConfigurationSection node, String path, String def) {
        String[] info = null;
        if (configSection_getCommentString != null) {
            try {
                Object comment = configSection_getCommentString.invoke(config, path);
                if (comment != null) {
                    info = comment.toString().split("\n");
                }
            } catch (Exception ex) {
            }
        }
        return GuiUtils.createButtonItem(LegacyMaterials.FILLED_MAP, path, info != null ? info : (def != null ? def.split("\n") : null));
    }

    ItemStack configItem(LegacyMaterials type, String name, ConfigurationSection node, String path, String value, String def) {
        if(value == null) value = "";
        String[] info = null;
        if (configSection_getCommentString != null) {
            try {
                Object comment = configSection_getCommentString.invoke(config, path);
                if (comment != null) {
                    info = (value + "\n" + comment.toString()).split("\n");
                }
            } catch (Exception ex) {
            }
        }
        return GuiUtils.createButtonItem(LegacyMaterials.FILLED_MAP, path, info != null ? info : (def != null ? (value + "\n" + def).split("\n") : null));
    }
}
