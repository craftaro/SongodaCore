package com.songoda.core.library.settings;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Setting {

    private final Category category;
    private final JavaPlugin plugin;

    private final String key;
    private final Object defaultValue;
    private final String comments[];

    public Setting(Category category, String key, Object defaultValue, String... comments) {
        this.category = category;
        this.plugin = category.getPlugin();
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
        return plugin.getConfig().getIntegerList(getCompleteKey());
    }

    public List<String> getStringList() {
        return plugin.getConfig().getStringList(getCompleteKey());
    }

    public boolean getBoolean() {
        return plugin.getConfig().getBoolean(getCompleteKey());
    }

    public int getInt() {
        return plugin.getConfig().getInt(getCompleteKey());
    }

    public long getLong() {
        return plugin.getConfig().getLong(getCompleteKey());
    }

    public String getString() {
        return plugin.getConfig().getString(getCompleteKey());
    }

    public char getChar() {
        return plugin.getConfig().getString(getCompleteKey()).charAt(0);
    }

    public double getDouble() {
        return plugin.getConfig().getDouble(getCompleteKey());
    }

    public Material getMaterial() {
        String materialStr = plugin.getConfig().getString(getCompleteKey());
        Material material = Material.getMaterial(materialStr);

        if (material == null) {
            System.out.println(String.format("Config value \"%s\" has an invalid material name: \"%s\"", getCompleteKey(), materialStr));
        }

        return material;
    }
}
