package com.craftaro.core.locale;

import com.craftaro.core.utils.ColorUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageList {
    private final BukkitAudiences adventure;
    private final List<Component> components;
    public MessageList(BukkitAudiences adventure, List<Component> components) {
        this.adventure = adventure;
        this.components = components;
    }

    public static MessageList of(BukkitAudiences adventure, List<String> text) {
        return new MessageList(adventure, ColorUtils.formatComponent(text));
    }

    public MessageList replace(String placeholder, String replacement) {
        this.components.replaceAll(component -> component.replaceText(text -> text.matchLiteral(placeholder).replacement(replacement)));
        return this;
    }

    public MessageList replace(String placeholder, ComponentLike replacement) {
        this.components.replaceAll(component -> component.replaceText(text -> text.matchLiteral(placeholder).replacement(replacement)));
        return this;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void broadcastMessage() {
        for (Component component : components) {
            adventure.all().sendMessage(component);
        }
    }

    public void sendMessage(Player player) {
        for (Component component : components) {
            adventure.player(player).sendMessage(component);
        }
    }

    public void sendMessage(CommandSender sender) {
        for (Component component : components) {
            adventure.sender(sender).sendMessage(component);
        }
    }
}
