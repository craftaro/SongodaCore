package com.craftaro.core.chat;

import com.bekvon.bukkit.residence.commands.list;
import com.craftaro.core.compatibility.ServerProject;
import com.craftaro.core.compatibility.ServerVersion;
import com.gamingmesh.jobs.commands.list.placeholders;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdventureUtils {

    private static Method displayNameMethod = null;
    private static Method loreMethod = null;

    static {
        if (ServerProject.isServer(ServerProject.PAPER) && ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16)) {
            try {
                displayNameMethod = ItemMeta.class.getDeclaredMethod("displayName", Component.class);
                loreMethod = ItemMeta.class.getDeclaredMethod("lore", List.class);
            } catch (Exception ignored) {}
        }
    }

    //Send message
    public static void sendMessage(Plugin plugin, Component message, Player... target) {
        try (BukkitAudiences bukkitAudiences = BukkitAudiences.create(plugin)){
            for (Player player : target) {
                bukkitAudiences.player(player).sendMessage(message);
            }
        }
    }

    //Items
    public static ItemStack formatItemName(ItemStack item, String name) {
        return formatItemName(item, formatComponent(name));
    }

    public static ItemStack formatItemLore(ItemStack item, List<String> lore) {
        return formatItemLore(item, lore.toArray(new String[0]));
    }

    public static ItemStack formatItemLore(ItemStack item, String... lore) {
        return formatItemLore(item, formatComponent(lore));
    }

    public static ItemStack formatItemLore(ItemStack item, List<Component> lore, String... unused) {
        return formatItemLore(item, lore.toArray(new Component[0]));
    }

    public static ItemStack formatItemName(ItemStack item, Component name) {
        return setItemName(item, name);
    }

    public static ItemStack formatItemLore(ItemStack item, Component... lore) {
        return setItemLore(item, lore);
    }

    public static boolean isMiniMessageEnabled() {
        return ServerProject.isServer(ServerProject.PAPER) && ServerVersion.isServerVersionAtLeast(ServerVersion.V1_16) && displayNameMethod != null && loreMethod != null;
    }

    private static ItemStack setItemName(ItemStack item, Component name) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        if (isMiniMessageEnabled()) {
            //Set names as component
            try {
                displayNameMethod.invoke(meta, name);
                item.setItemMeta(meta);
                return item;
            } catch (Exception ignored) {}
        }
        meta.setDisplayName(toLegacy(name));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack setItemLore(ItemStack item, Component... lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        if (isMiniMessageEnabled()) {
            //Set names as component
            try {
                loreMethod.invoke(meta, Arrays.asList(lore));
                item.setItemMeta(meta);
                return item;
            } catch (Exception ignored) {}
        }
        meta.setLore(toLegacy(lore));
        item.setItemMeta(meta);
        return item;
    }

    //Formatting stuff
    public static Component formatComponent(String text) {
        MiniMessage miniMessage = MiniMessage.builder().build();
        Component component = MiniMessage.miniMessage().deserialize(replaceLegacy(text));
        if (!component.hasDecoration(TextDecoration.ITALIC)) {
            component = component.decoration(TextDecoration.ITALIC, false);
        }
        return component;
    }

    public static Component formatComponent(String text, MiniMessagePlaceholder...placeholders) {
        MiniMessage miniMessage = MiniMessage.builder().editTags(builder -> {
            Arrays.stream(placeholders).forEach(placeholder ->
                    builder.resolver(Placeholder.parsed(placeholder.getPlaceholder(), placeholder.getValue()))
            );
        }).build();
        Component component = miniMessage.deserialize(replaceLegacy(text));
        if (!component.hasDecoration(TextDecoration.ITALIC)) {
            component = component.decoration(TextDecoration.ITALIC, false);
        }
        return component;
    }

    public static List<Component> formatComponent(List<String> list) {
        List<Component> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatComponent(line));
        }
        return result;
    }

    public static List<Component> formatComponent(String...list) {
        List<Component> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatComponent(line));
        }
        return result;
    }


    public static List<Component> formatComponent(List<String> list, MiniMessagePlaceholder... placeholders) {
        List<Component> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatComponent(line, placeholders));
        }
        return result;
    }

    public static String formatLegacy(String text) {
        return ChatColor.translateAlternateColorCodes('&',
                LegacyComponentSerializer.legacyAmpersand().serialize(MiniMessage.miniMessage().deserialize(replaceLegacy(text))));
    }

    public static List<String> formatLegacy(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatLegacy(line));
        }
        return result;
    }

    public static List<String> formatLegacy(String... list) {
        List<String> result = new ArrayList<>();
        for (String line : list) {
            result.add(formatLegacy(line));
        }
        return result;
    }

    public static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static List<String> toLegacy(List<Component> components) {
        List<String> list = new ArrayList<>();
        for (Component component : components) {
            list.add(toLegacy(component));
        }
        return list;
    }

    public static List<String> toLegacy(Component... components) {
        List<String> list = new ArrayList<>();
        for (Component component : components) {
            list.add(toLegacy(component));
        }
        return list;
    }

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

    public static String clear(String msg) {
        msg = msg.replaceAll("&[0-9kabcdefklmnor]", "");
        msg = msg.replaceAll("§[0-9kabcdefklmnor]", "");
        msg = msg.replaceAll("&#[0-9a-fA-F]{6}", "");
        return PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(msg));
    }

    public static String clear(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
}
