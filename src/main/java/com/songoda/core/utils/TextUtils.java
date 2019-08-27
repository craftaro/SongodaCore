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

    public static String convertToInvisibleString(String s) {
        if (s == null || s.equals(""))
            return "";
        StringBuilder hidden = new StringBuilder();
        for (char c : s.toCharArray()) hidden.append(ChatColor.COLOR_CHAR + "").append(c);
        return hidden.toString();
    }

}
