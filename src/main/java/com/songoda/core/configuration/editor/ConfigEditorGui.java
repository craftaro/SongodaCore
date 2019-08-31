package com.songoda.core.configuration.editor;

import com.songoda.core.gui.SimplePagedGui;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.MemoryConfiguration;

public class ConfigEditorGui extends SimplePagedGui {

    final MemoryConfiguration config;
    List<String> keys = new ArrayList();
    List<String> sections = new ArrayList();
    List<String> settings = new ArrayList();

    public ConfigEditorGui(String file, MemoryConfiguration config) {
        this(file, config, config);
    }

    public ConfigEditorGui(String file, MemoryConfiguration config, MemoryConfiguration node) {
        this.config = config;
        for (String key : node.getKeys(false)) {
            if (node.isConfigurationSection(key)) {
                sections.add(key);
                keys.add(key); // sections listed first
            } else {
                settings.add(key);
            }
        }
        keys.addAll(settings); // normal settings next

        // next we need to display the config settings, with the ability to browse more pages
    }
}
