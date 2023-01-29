package com.songoda.core.locale;

import com.songoda.core.SongodaPlugin;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

class Message {
    private final BukkitAudiences adventure;
    private Component component;

    public Message(BukkitAudiences adventure, Component component) {
        this.adventure = adventure;
        this.component = component;
    }

    public Message replace(String placeholder, String replacement) {
        this.component = component.replaceText(text -> text.matchLiteral(placeholder).replacement(replacement));
        return this;
    }

    public void broadcastMessage() {
        adventure.all().sendMessage(component);
    }

    public void sendMessage(Player player) {
        adventure.player(player).sendMessage(component);
    }

    public void sendMessage(CommandSender sender) {
        adventure.sender(sender).sendMessage(component);
    }

    public void broadcastActionBar() {
        adventure.all().sendActionBar(component);
    }

    public void sendActionBar(Player player) {
        adventure.player(player).sendActionBar(component);
    }
}
