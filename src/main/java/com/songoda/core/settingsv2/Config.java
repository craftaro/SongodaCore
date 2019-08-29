package com.songoda.core.settingsv2;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public class Config extends YamlConfiguration {

    /*
    Serialization notes:
    // implements ConfigurationSerializable:
    //public Map<String, Object> serialize();
    
    // Class must contain one of:
    // public static Object deserialize(@NotNull Map<String, ?> args);
    // public static valueOf(Map<String, ?> args);
    // public new (Map<String, ?> args)
     */

    final File file;
    final DumperOptions yamlOptions = new DumperOptions();
    final Representer yamlRepresenter = new YamlRepresenter();
    final Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
    protected int indentation = 2; // between 2 and 9 (inclusive)
    protected char pathChar = '.';
    final HashMap<String, Comment> configComments = new HashMap();
    final HashMap<String, Class> strictKeys = new HashMap();
    final HashMap<String, Object> values = new HashMap();

    public Config(@NotNull File file) {
        this.file = file;
    }

    public Config(@NotNull Plugin plugin, @NotNull String file) {
        this.file = new File(plugin.getDataFolder(), file);
    }

    public Config(@NotNull Plugin plugin, @NotNull String directory, @NotNull String file) {
        this.file = new File(plugin.getDataFolder() + directory, file);
    }

    @NotNull
    public Config setHeader(@NotNull String... description) {
        if (description.length == 0) {
            configComments.remove(null);
        } else {
            configComments.put(null, new Comment(description));
        }
        return this;
    }

    @NotNull
    public Config setHeader(@Nullable List<String> description) {
        if (description == null || description.isEmpty()) {
            configComments.remove(null);
        } else {
            configComments.put(null, new Comment(description));
        }
        return this;
    }

    @NotNull
    public List<String> getHeader() {
        if (configComments.containsKey(null)) {
            return configComments.get(null).getLines();
        } else {
            return Collections.EMPTY_LIST;
        }
    }

    @NotNull
    @Override
    public String saveToString() {
        try {
            yamlOptions.setIndent(indentation);
            yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            StringWriter str = new StringWriter();
            Comment header = configComments.get(null);
            if (header != null) {
                header.writeComment(str, 0, ConfigFormattingRules.CommentStyle.SPACED);
            }
            String dump = yaml.dump(this.getValues(false));
            if (dump.equals(BLANK_CONFIG)) {
                dump = "";
            }
            return str.toString() + dump;
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public int getIndent() {
        return indentation;
    }

    public void setIndent(int indentation) {
        this.indentation = indentation;
    }

    public char getPathSeparator() {
        return pathChar;
    }

    public void setPathSeparator(char pathChar) {
        this.pathChar = pathChar;
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
    public YamlConfigurationOptions options() {
        throw new UnsupportedOperationException("Not supported yet.");
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
