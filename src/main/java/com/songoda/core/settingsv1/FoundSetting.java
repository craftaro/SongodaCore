package com.songoda.core.settingsv1;

import org.bukkit.configuration.file.FileConfiguration;

public class FoundSetting extends Setting {

    private final Category category;

    private final String key;
    private final Object defaultValue;
    private final String[] comments;

    public FoundSetting(Category category, String key, Object defaultValue, String... comments) {
        this.category = category;
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getCompleteKey() {
        return category.getKey() + "." + key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String[] getComments() {
        if (comments.length == 0 && category.getDefaultSetting(key) != null
                && category.getDefaultSetting(key) instanceof FoundSetting)
            return ((FoundSetting)category.getDefaultSetting(key)).comments;
        return comments;
    }

    @Override
    public FileConfiguration getConfig() {
        return category.getConfig().getFileConfiguration();
    }

    public Category getCategory() {
        return category;
    }
}
