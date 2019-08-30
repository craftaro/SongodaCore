package com.songoda.core.settingsv2.editor;

import com.songoda.core.gui.Gui;
import com.songoda.core.settingsv2.Config;
import com.songoda.core.settingsv2.SongodaConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public class ConfigEditorGui extends Gui {
    final Config config;
    List<String> keys = new ArrayList();
    List<String> sections = new ArrayList();
    List<String> settings = new ArrayList();

    public ConfigEditorGui(Config config, SongodaConfigurationSection node) {
        this.config = config;
        for(String key : node.getKeys(false)) {
            if(node.isConfigurationSection(key)) {
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
