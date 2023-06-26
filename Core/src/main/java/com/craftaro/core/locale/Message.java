package com.craftaro.core.locale;

import com.craftaro.core.utils.ColorUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {
    private final BukkitAudiences adventure;
    private Component component;

    public Message(BukkitAudiences adventure, Component component) {
        this.adventure = adventure;
        this.component = component;
    }

    public static Message of(BukkitAudiences adventure, String text) {
        return new Message(adventure, ColorUtils.formatComponent(text));
    }

    public Message replace(String placeholder, String replacement) {
        this.component = component.replaceText(text -> text.matchLiteral(placeholder).replacement(replacement));
        return this;
    }

    public Message replace(String placeholder, ComponentLike replacement) {
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

    public Component getComponent() {
        return component;
    }

    @Override
    public String toString() {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
