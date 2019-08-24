package com.songoda.core.library.settings;

import java.util.*;

public class Category extends Narrow {

    private final Config config;

    private final String key;

    protected final Map<String, FoundSetting> defaultSettings = new LinkedHashMap<>();

    private final List<String> comments = new ArrayList<>();

    public Category(Config config, String key, String... comments) {
        this.config = config;
        this.key = key;
        if (comments != null)
            this.comments.addAll(Arrays.asList(comments));
    }

    public Category(Config config, String key) {
        this(config, key, null);
    }

    public Category addAll(Category category) {
        addSettings(category);
        if (comments.size() == 0)
            addComments(category.getComments());
        return this;
    }

    public Category addSetting(String key, Object defaultValue, String... comments) {
        addSetting(new FoundSetting(this, key, defaultValue, comments));
        return this;
    }

    public Category addDefaultSetting(String key, Object defaultValue, String... comments) {
        addDefaultSetting(new FoundSetting(this, key, defaultValue, comments));
        return this;
    }

    public Category addDefaultSetting(FoundSetting setting) {
        this.defaultSettings.put(setting.getKey(), setting);
        return this;
    }

    public Category addSettings(FoundSetting... settings) {
        for (FoundSetting setting : settings)
            this.settings.put(setting.getKey(), setting);
        return this;
    }

    public Category addSettings(Category category) {
        for (FoundSetting setting : category.getSettings())
            this.settings.put(setting.getKey(), setting);
        return this;
    }

    public Setting getDefaultSetting(String setting) {
        for (String string : defaultSettings.keySet())
            if (string.equalsIgnoreCase(setting))
                return defaultSettings.get(string);
        return new Setting();
    }

    public List<FoundSetting> getDefaultSettings() {
        return new ArrayList<>(defaultSettings.values());
    }


    public String getKey() {
        return key;
    }

    public void addComments(List<String> commments) {
        this.comments.addAll(commments);
    }

    public List<String> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public Config getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return key;
    }
}
