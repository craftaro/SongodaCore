package com.songoda.core.library.settings;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Category {

    private final Config config;

    private final String key;
    private final String[] comments;

    private final Map<String, Setting> settings = new HashMap<>();

    public Category(Config config, String key, String... comments) {
        this.config = config;
        this.key = key;
        this.comments = comments;
    }

    public Category(Config config, String key) {
        this(config, key, null);
    }

    public Category addSetting(String key, Object defaultValue, String... comments) {
        this.settings.put(key, new Setting(this, key, defaultValue, comments));
        return this;
    }

    public Set<Setting> getSettings() {
        return new HashSet<>(settings.values());
    }

    public Setting getSetting(String setting) {
        for (String string : settings.keySet())
            if (string.equalsIgnoreCase(setting))
                return settings.get(string);
        return null;
    }

    public String getKey() {
        return key;
    }


    public String[] getComments() {
        return comments;
    }

    public Config getConfig() {
        return config;
    }
}
