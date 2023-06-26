package com.craftaro.core.locale;

import com.craftaro.core.CraftaroPlugin;
import com.craftaro.core.configuration.Config;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.File;
import java.util.stream.Collectors;

public class LocaleManager {

    private final CraftaroPlugin plugin;
    private final Config langFile;

    public LocaleManager(CraftaroPlugin plugin) {
        this.plugin = plugin;
        this.langFile = plugin.createUpdatingConfig(new File(plugin.getDataFolder(), "lang.yml"));
    }

    public Message getMessage(String path) {
        return new Message(plugin.getAdventure(), MiniMessage.miniMessage().deserialize(langFile.getString(path)));
    }

    public MessageList getMessageList(String path) {
        return new MessageList(plugin.getAdventure(), langFile.getStringList(path).stream().map(text -> MiniMessage.miniMessage().deserialize(text)).collect(Collectors.toList()));
    }
}

