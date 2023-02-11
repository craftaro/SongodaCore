package com.songoda.core.locale;

import com.songoda.core.SongodaPlugin;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.File;

public class LocaleManager {

    private final SongodaPlugin plugin;
    private final YamlDocument langFile;

    public LocaleManager(SongodaPlugin plugin) {
        this.plugin = plugin;
        this.langFile = plugin.createUpdatingConfig(new File(plugin.getDataFolder(), "lang.yml"));
    }

    public Message getMessage(String path) {
        return new Message(plugin.getAdventure(), MiniMessage.miniMessage().deserialize(langFile.getString(path)));
    }
}

