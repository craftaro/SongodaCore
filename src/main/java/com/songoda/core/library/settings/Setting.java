package com.songoda.core.library.settings;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Setting {

    private final Category category;

    private final String key;
    private final Object defaultValue;
    private final String comments[];

    public Setting(Category category, String key, Object defaultValue, String... comments) {
        this.category = category;
        this.key = key;
        this.defaultValue = defaultValue;
        this.comments = comments;
    }

    public String getKey() {
        return key;
    }

    public String getCompleteKey() {
        return category.getKey() + "." + key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public String[] getComments() {
        return comments;
    }

    public List<Integer> getIntegerList() {
        return getConfig().getIntegerList(getCompleteKey());
    }

    public List<String> getStringList() {
        return getConfig().getStringList(getCompleteKey());
    }

    public boolean getBoolean() {
        return getConfig().getBoolean(getCompleteKey());
    }

    public int getInt() {
        return getConfig().getInt(getCompleteKey());
    }

    public long getLong() {
        return getConfig().getLong(getCompleteKey());
    }

    public String getString() {
        return getConfig().getString(getCompleteKey());
    }

    public char getChar() {
        return getConfig().getString(getCompleteKey()).charAt(0);
    }

    public double getDouble() {
        return getConfig().getDouble(getCompleteKey());
    }

    public Material getMaterial() {
        String materialStr = getConfig().getString(getCompleteKey());
        Material material = Material.getMaterial(materialStr);

        if (material == null) {
            System.out.println(String.format("Config value \"%s\" has an invalid material name: \"%s\"", getCompleteKey(), materialStr));
        }

        return material;
    }

    public FileConfiguration getConfig() {
        return category.getConfig().getFileConfiguration();
    }
}
