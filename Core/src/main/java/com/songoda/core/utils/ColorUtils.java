package com.songoda.core.utils;

import com.songoda.core.compatibility.ServerVersion;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorUtils {

    /**
     * Format a string with color codes both legacy and MiniMessage
     * @param text The text to format
     * @return The formatted text
     */
    public static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(replaceLegacy(text))));
    }

    /**
     * Replace all legacy color codes with MiniMessage tags
     * @param legacy The legacy text
     * @return The replaced text
     */
    public static String replaceLegacy(String legacy) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < legacy.length(); i++) {
            char current = legacy.charAt(i);
            char next = legacy.charAt(i == legacy.length() - 1 ? i : i + 1);
            if (current == '§' || current == '&') {
                if (next == 'x' && legacy.length() > i + 13) {
                    builder.append("<color:#");
                    builder.append(legacy.charAt(i + 3));
                    builder.append(legacy.charAt(i + 5));
                    builder.append(legacy.charAt(i + 7));

                    builder.append(legacy.charAt(i + 9));
                    builder.append(legacy.charAt(i + 11));
                    builder.append(legacy.charAt(i + 13));
                    builder.append(">");
                    i += 13;
                    continue;
                }
                String color = getColor(next);
                builder.append(color);
                i++;
            } else {
                builder.append(current);
            }
        }
        return builder.toString();
    }

    public static String getColor(char c) {
        ChatColor color = ChatColor.getByChar(c);
        if (color == null) return null;
        switch (c) {
            case '0':
                return  "<black>";
            case '1':
                return  "<dark_blue>";
            case '2':
                return  "<dark_green>";
            case '3':
                return  "<dark_aqua>";
            case '4':
                return  "<dark_red>";
            case '5':
                return  "<dark_purple>";
            case '6':
                return  "<gold>";
            case '7':
                return  "<gray>";
            case '8':
                return  "<dark_gray>";
            case '9':
                return  "<blue>";
            case 'a':
                return  "<green>";
            case 'b':
                return  "<aqua>";
            case 'c':
                return  "<red>";
            case 'd':
                return  "<light_purple>";
            case 'e':
                return  "<yellow>";
            case 'f':
                return  "<white>";
            case 'k':
                return  "<obfuscated>";
            case 'l':
                return  "<b>";
            case 'm':
                return  "<st>";
            case 'n':
                return  "<u>";
            case 'o':
                return  "<i>";
            case 'r':
                return  "<reset>";
            default:
                return "";
        }
    }

    /**
     * Format a list of strings with color codes both legacy and MiniMessage
     * @param list The list to format
     * @return The formatted list
     */
    public static List<String> format(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String line : list) {
            result.add(format(line));
        }
        return result;
    }

    /**
     * Format a text to a component with color codes both legacy and MiniMessage
     * @param text The text to format
     * @return The formatted component
     */
    public static Component formatComponent(String text) {
        Component component = MiniMessage.miniMessage().deserialize(replaceLegacy(text));
        if (!component.hasDecoration(TextDecoration.ITALIC)) {
            component = component.decoration(TextDecoration.ITALIC, false);
        }
        return component;
    }

    /**
     * Format a list of strings to a list of components with color codes both legacy and MiniMessage
     * @param list The list to format
     * @return The formatted list
     */
    public static List<Component> formatComponent(List<String> list) {
        List<Component> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatComponent(line));
        }
        return result;
    }

    /**
     * Parse a string with PlaceholderAPI placeholders and translate color codes both legacy and MiniMessage
     * @param player The player to parse placeholders for
     * @param text The message to parse
     * @return The parsed message
     */
    public static String parse(Player player, String text) {
        return PlaceholderAPI.setPlaceholders(player, format(text));
    }

    /**
     * Parse a list of strings with PlaceholderAPI placeholders
     * and translate color codes both legacy and MiniMessage
     * @param player The player to parse placeholders for
     * @param list The list to parse
     * @return The parsed list
     */
    public static List<String> parse(Player player, List<String> list) {
        List<String> result = new ArrayList<>();
        for (String line : list) {
            result.add(PlaceholderAPI.setPlaceholders(player, format(line)));
        }
        return result;
    }

    /**
     * Parse a string with PlaceholderAPI placeholders
     * and translate color codes both legacy and MiniMessage
     * and convert it to a component
     * @param player The player to parse placeholders for
     * @param text The message to parse
     * @return The parsed component
     */
    public static Component parseComponent(Player player, String text) {
        return formatComponent(PlaceholderAPI.setPlaceholders(player, text));
    }

    /**
     * Parse a list of strings with PlaceholderAPI placeholders
     * and translate color codes both legacy and MiniMessage
     * and convert it to a list of components
     * @param player The player to parse placeholders for
     * @param list The list to parse
     * @return The parsed list
     */
    public static List<Component> parseComponent(Player player, List<String> list) {
        List<Component> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatComponent(PlaceholderAPI.setPlaceholders(player, format(line))));
        }
        return result;
    }
}
