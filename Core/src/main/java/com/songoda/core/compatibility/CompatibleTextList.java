package com.songoda.core.compatibility;

import com.songoda.core.locale.TextPlaceholder;
import com.songoda.core.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompatibleTextList {

    private List<String> textList;

    public CompatibleTextList(List<String> textList) {
        this.textList = textList;
    }

    public static CompatibleTextList of(String... text) {
        return new CompatibleTextList(Arrays.asList(text));
    }

    /**
     * Translate the legacy color codes in the text list
     * @return The text list with color codes translated
     */
    public List<String> legacy() {
        return ColorUtils.format(textList);
    }

    /**
     * Replace internal placeholders in the text list
     * @param placeholders The internal placeholders to replace
     * @return The text list with placeholders replaced
     */
    public List<String> legacy(TextPlaceholder... placeholders) {
        List<String> replaced = ColorUtils.format(textList);
        for (TextPlaceholder placeholder : placeholders) {
            replaced.replaceAll(s -> s.replace("<"+placeholder.getPlaceholder()+">", placeholder.getReplacement()));
        }
        return replaced;
    }

    /**
     * Replace internal and PlaceholderAPI placeholders in the text list
     * @param player The player to replace placeholders for (PlaceholderAPI)
     * @param placeholders The internal placeholders to replace
     * @return The text list with placeholders replaced
     */
    public List<String> legacy(Player player, TextPlaceholder... placeholders) {
        List<String> replaced = textList;
        for (TextPlaceholder placeholder : placeholders) {
            replaced.replaceAll(s -> s.replace("<"+placeholder.getPlaceholder()+">", placeholder.getReplacement()));
        }
        return ColorUtils.parse(player, replaced);
    }

    /**
     * Convert the text list to a component list
     * @return The component list
     */
    public List<Component> component() {
        List<Component> result = new ArrayList<>();
        for (String text : textList) {
            result.add(ColorUtils.formatComponent(text));
        }
        return result;
    }

    /**
     * Convert the text list to a component list and replace internal placeholders
     * @param placeholders The internal placeholders to replace
     * @return The component list with placeholders replaced
     */
    public List<Component> component(TextPlaceholder... placeholders) {
        List<Component> replaced = ColorUtils.formatComponent(textList);
        Arrays.stream(placeholders).forEach(placeholder -> replaced.replaceAll(component -> component.replaceText(text -> text.matchLiteral("<"+placeholder.getPlaceholder()+">").replacement(placeholder.getReplacement()))));
        return replaced;
    }

    /**
     * Convert the text list to a component list and replace internal and PlaceholderAPI placeholders
     * @param player The player to replace placeholders for (PlaceholderAPI)
     * @param placeholders The internal placeholders to replace
     * @return The component list with placeholders replaced
     */
    public List<Component> component(Player player, TextPlaceholder... placeholders) {
        List<Component> replaced = ColorUtils.parseComponent(player, textList);
        Arrays.stream(placeholders).forEach(placeholder -> replaced.replaceAll(component -> component.replaceText(text -> text.matchLiteral("<"+placeholder.getPlaceholder()+">").replacement(placeholder.getReplacement()))));
        return replaced;
    }
}
