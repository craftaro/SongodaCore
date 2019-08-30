package com.songoda.core.settingsv2.adapters;

import com.songoda.core.settingsv2.SongodaConfigurationSection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ConfigDefaultsAdapter implements Configuration {

    final SongodaConfigurationSection root;
    final SongodaConfigurationSection current;

    public ConfigDefaultsAdapter(SongodaConfigurationSection config, SongodaConfigurationSection current) {
        this.root = config;
        this.current = current;
    }

    @Override
    public void addDefault(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addDefaults(Map<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addDefaults(Configuration c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDefaults(Configuration c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Configuration getDefaults() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConfigurationOptions options() {
        return new ConfigOptionsAdapter(root);
    }

    @Override
    public Set<String> getKeys(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Object> getValues(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contains(String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSet(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCurrentPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Configuration getRoot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConfigurationSection getParent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void set(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConfigurationSection createSection(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConfigurationSection createSection(String string, Map<?, ?> map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getString(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getString(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isString(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt(String string, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInt(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getBoolean(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getBoolean(String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBoolean(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getDouble(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getDouble(String string, double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDouble(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong(String string, long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isLong(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<?> getList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<?> getList(String string, List<?> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getStringList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Integer> getIntegerList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Boolean> getBooleanList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Double> getDoubleList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Float> getFloatList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Long> getLongList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Byte> getByteList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Character> getCharacterList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Short> getShortList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Map<?, ?>> getMapList(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getObject(String string, Class<T> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T getObject(String string, Class<T> type, T t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(String string, Class<T> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(String string, Class<T> type, T t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector getVector(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector getVector(String string, Vector vector) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isVector(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String string, OfflinePlayer op) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOfflinePlayer(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ItemStack getItemStack(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ItemStack getItemStack(String string, ItemStack is) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isItemStack(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getColor(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color getColor(String string, Color color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isColor(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConfigurationSection getConfigurationSection(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isConfigurationSection(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ConfigurationSection getDefaultSection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
