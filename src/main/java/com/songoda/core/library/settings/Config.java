package com.songoda.core.library.settings;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Config {

    private static final Map<String, Category> categories = new HashMap<>();

    public static Category addCategory(JavaPlugin plugin, String key, String... comments) {
        return categories.put(key, new Category(plugin, key, comments));
    }

    public static Category getCategory(String key) {
        return categories.get(key);
    }

    public static Setting getSetting(String key) {
        String[] split = key.split(".", 2);
        Category category = categories.get(split[0]);
        return category.getSetting(split[1]);
    }

    public static Set<Setting> getSettings() {
        Set<Setting> settings = new HashSet<>();
        for (Category category : categories.values()) {
            settings.addAll(category.getSettings());
        }
        return settings;
    }
}
