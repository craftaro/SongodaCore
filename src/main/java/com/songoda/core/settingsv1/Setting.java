package com.songoda.core.settingsv1;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Setting {

    public String getKey() {
        return "";
    }

    public String getCompleteKey() {
        return "";
    }

    public List<Integer> getIntegerList() {
        if (getConfig() == null) return new ArrayList<>();
        return getConfig().getIntegerList(getCompleteKey());
    }

    public List<String> getStringList() {
        if (getConfig() == null) return new ArrayList<>();
        return getConfig().getStringList(getCompleteKey());
    }

    public boolean getBoolean() {
        return getBoolean(false);
    }

    public boolean getBoolean(boolean def) {
        if (getConfig() == null) return def;
        return getConfig().getBoolean(getCompleteKey(), def);
    }

    public int getInt() {
        return getInt(0);
    }

    public int getInt(int def) {
        if (getConfig() == null) return def;
        return getConfig().getInt(getCompleteKey(), def);
    }

    public long getLong() {
        return getLong(0L);
    }

    public long getLong(long def) {
        if (getConfig() == null) return def;
        return getConfig().getLong(getCompleteKey(), def);
    }

    public double getDouble() {
        return getDouble(0D);
    }

    public double getDouble(double def) {
        if (getConfig() == null) return def;
        return getConfig().getDouble(getCompleteKey(), def);
    }

    public String getString() {
        return getString(null);
    }

    public String getString(String def) {
        if (getConfig() == null) return def;
        return getConfig().getString(getCompleteKey());
    }

    public Object getObject() {
        return getString(null);
    }

    public Object getObject(Object def) {
        return getConfig().get(getCompleteKey());
    }

    public char getChar() {
        return getChar('0');
    }

    public char getChar(char def) {
        if (getConfig() == null) return def;
        return getConfig().getString(getCompleteKey()).charAt(def);
    }

    public Material getMaterial() {
        return getMaterial(Material.STONE);
    }

    public Material getMaterial(Material def) {
        String materialStr = getConfig().getString(getCompleteKey());
        Material material = Material.getMaterial(materialStr);

        if (material == null) {
            System.out.println(String.format("Config value \"%s\" has an invalid material name: \"%s\"", getCompleteKey(), materialStr));
        }

        return material != null ? material : def;
    }

    public FileConfiguration getConfig() {
        return null;
    }
}
