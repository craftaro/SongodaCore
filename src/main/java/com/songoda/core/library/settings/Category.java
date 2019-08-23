package com.songoda.core.library.settings;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Category {

    private final JavaPlugin plugin;

    private final String key;
    private final String[] comments;

    private final Map<String, Setting> settings = new HashMap<>();

    public Category(JavaPlugin plugin, String key, String... comments) {
        this.plugin = plugin;
        this.key = key;
        this.comments = comments;
    }

    public Category(JavaPlugin plugin, String key) {
        this(plugin, key, null);
    }

    public Category addSetting(String key, Object defaultValue, String... comments) {
        this.settings.put(key, new Setting(this, key, defaultValue, comments));
        return this;
    }

    public Set<Setting> getSettings() {
        return new HashSet<>(settings.values());
    }

    public Setting getSetting(String setting) {
        return settings.get(setting);
    }

    public String getKey() {
        return key;
    }


    public String[] getComments() {
        return comments;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
