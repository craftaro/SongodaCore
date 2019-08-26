package com.songoda.core.utils;

import org.bukkit.ChatColor;

public class TextUtils {

    public static String formatText(String text) {
        return formatText(text, false);
    }

    public static String formatText(String text, boolean capitalize) {
        if (text == null || text.equals(""))
            return "";
        if (capitalize)
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
