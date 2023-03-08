package com.songoda.core.compatibility;

import com.songoda.core.SongodaCore;
import com.songoda.core.locale.TextPlaceholder;
import com.songoda.core.utils.ColorUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompatibleText {

    private final String text;

    public CompatibleText(String text) {
        this.text = text;
    }

    public static CompatibleText of(String text) {
        return new CompatibleText(text);
    }

    /**
     * Translate the legacy color codes in the text
     * @return The text with color codes translated
     */
    public String legacy() {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    /**
     * Replace internal placeholders in the text
     * @param placeholders The internal placeholders to replace
     * @return The text with placeholders replaced
     */
    public String legacy(TextPlaceholder... placeholders) {
        String replaced = text;
        for (TextPlaceholder placeholder : placeholders) {
            replaced = replaced.replace("<"+placeholder.getPlaceholder()+">", placeholder.getReplacement());
        }
        return ColorUtils.format(text);
    }

    /**
     * Replace internal and PlaceholderAPI placeholders in the text
     * @param player The player to replace placeholders for (PlaceholderAPI)
     * @param placeholders The internal placeholders to replace
     * @return The text with placeholders replaced
     */
    public String legacy(Player player, TextPlaceholder... placeholders) {
        String replaced = text;
        for (TextPlaceholder placeholder : placeholders) {
            replaced = replaced.replace("<"+placeholder.getPlaceholder()+">", placeholder.getReplacement());
        }
        return ColorUtils.parse(player, text);
    }

    /**
     * Convert the text to a component
     * @return The component
     */
    public Component component() {
        return ColorUtils.formatComponent(text);
    }

    /**
     * Convert the text to a component and replace internal placeholders
     * @param placeholders The internal placeholders to replace
     * @return The component with placeholders replaced
     */
    public Component component(TextPlaceholder... placeholders) {
        Component component = ColorUtils.formatComponent(text);
        for (TextPlaceholder placeholder : placeholders) {
            component = component.replaceText(text -> text.matchLiteral("<"+placeholder.getPlaceholder()+">").replacement(placeholder.getReplacement()));
        }
        return component;
    }

    /**
     * Convert the text to a component and replace internal and PlaceholderAPI placeholders
     * @param player The player to replace placeholders for (PlaceholderAPI)
     * @param placeholders The internal placeholders to replace
     * @return The component with placeholders replaced
     */
    public Component component(Player player, TextPlaceholder... placeholders) {
        Component component = ColorUtils.parseComponent(player, text);
        for (TextPlaceholder placeholder : placeholders) {
            component = component.replaceText(text -> text.matchLiteral("<"+placeholder.getPlaceholder()+">").replacement(placeholder.getReplacement()));
        }
        return component;
    }
}
