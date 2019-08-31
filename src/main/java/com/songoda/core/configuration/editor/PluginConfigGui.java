package com.songoda.core.configuration.editor;

import com.songoda.core.SongodaPlugin;
import com.songoda.core.compatibility.LegacyMaterials;
import com.songoda.core.configuration.Config;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.core.gui.SimplePagedGui;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Edit all configuration files for a specific plugin
 *
 * @since 2019-08-31
 * @author jascotty2
 */
public class PluginConfigGui extends SimplePagedGui {

    final Plugin plugin;
    LinkedHashMap<String, MemoryConfiguration> configs = new LinkedHashMap();

    public PluginConfigGui(SongodaPlugin plugin) {
        this(plugin, null);
    }

    public PluginConfigGui(SongodaPlugin plugin, Gui parent) {
        super(parent);
        this.plugin = plugin;

        // collect list of plugins
        configs.put(plugin.getConfig().getConfig().getFile().getName(), plugin.getConfig().getConfig());
        List<Config> more = plugin.getExtraConfig();
        if (more != null && !more.isEmpty()) {
            for (Config cfg : more) {
                configs.put(cfg.getFile().getName(), cfg);
            }
        }
        init();
    }

    public PluginConfigGui(Plugin plugin) {
        this(plugin, null);
    }

    public PluginConfigGui(Plugin plugin, Gui parent) {
        super(parent);
        this.plugin = plugin;

        // collect list of plugins
        configs.put("config.yml", plugin.getConfig());

        try {
            // can we also grab extra config from this mysterious plugin?
            Object more = plugin.getClass().getDeclaredMethod("getExtraConfig").invoke(plugin);
            if (more instanceof List && !((List) more).isEmpty()) {
                try {
                    // if we have the getExtraConfig function, we should also be able to get the file
                    Method method_Config_getFile = ((List) more).get(0).getClass().getDeclaredMethod("getFile");
                    for (Object cfg : ((List) more)) {
                        configs.put(((File) method_Config_getFile.invoke(cfg)).getName(), (MemoryConfiguration) cfg);
                    }
                } catch (Exception  ex) {
                    // include a failsafe, I guess
                    ((List) more).forEach(cfg -> configs.put("(File " + configs.size() + ")", (MemoryConfiguration) cfg));
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // I guess not!
        }
        init();
    }

    private void init() {

        // decorate header
        this.setTitle(ChatColor.DARK_BLUE + plugin.getName() + " Plugin Config");
        this.setUseHeader(true);
        headerBackItem = footerBackItem = GuiUtils.getBorderItem(LegacyMaterials.GRAY_STAINED_GLASS_PANE.getItem());
        this.setButton(8, GuiUtils.createButtonItem(LegacyMaterials.OAK_DOOR, "Exit"), (event) -> event.player.closeInventory());

        // List out all config files that this plugin has
        int i = 9;
        for (Map.Entry<String, MemoryConfiguration> config : configs.entrySet()) {
            this.setButton(i++, GuiUtils.createButtonItem(LegacyMaterials.BOOK, ChatColor.YELLOW + config.getKey(), "Click to edit this config"),
                    (event) -> event.manager.showGUI(event.player, new ConfigEditorGui(config.getKey(), config.getValue())));
        }
    }

}
