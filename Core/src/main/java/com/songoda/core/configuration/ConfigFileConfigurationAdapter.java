package com.songoda.core.configuration;

import com.songoda.core.compatibility.CompatibleMaterial;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigFileConfigurationAdapter extends FileConfiguration {
    final Config config;

    public ConfigFileConfigurationAdapter(Config config) {
        super(config);

        this.config = config;
    }

    public Config getCoreConfig() {
        return config;
    }

    @Override
    public String saveToString() {
        return config.saveToString();
    }

    @Override
    public void loadFromString(String string) throws InvalidConfigurationException {
        config.loadFromString(string);
    }

    @Override
    protected String buildHeader() {
        return "#" + String.join("\n#", config.getHeader());
    }

    @Override
    public ConfigOptionsAdapter options() {
        return new ConfigOptionsAdapter(config);
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    @Override
    public boolean contains(String path) {
        return config.contains(path);
    }

    @Override
    public boolean isSet(String path) {
        return config.isSet(path);
    }

    @Override
    public String getCurrentPath() {
        return config.getCurrentPath();
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public Configuration getRoot() {
        return config;
    }

    @Override
    public ConfigurationSection getParent() {
        return null;
    }

    @Override
    public void addDefault(String path, Object value) {
        config.addDefault(path, value);
    }

    @Override
    public ConfigurationSection getDefaultSection() {
        return config.getDefaultSection();
    }

    @Override
    public void set(String path, Object value) {
        config.set(path, value);
    }

    @Override
    public Object get(String path) {
        return config.get(path);
    }

    @Override
    public Object get(String path, Object def) {
        return config.get(path, def);
    }

    @Override
    public ConfigurationSection createSection(String path) {
        return config.createSection(path);
    }

    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        return config.createSection(path, map);
    }

    // Other non-FileConfiguration methods

    @NotNull
    public ConfigSection createDefaultSection(@NotNull String path) {
        return config.createDefaultSection(path);
    }

    @NotNull
    public ConfigSection createDefaultSection(@NotNull String path, String... comment) {
        return config.createDefaultSection(path, comment);
    }

    @NotNull
    public ConfigSection createDefaultSection(@NotNull String path, ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        return config.createDefaultSection(path, commentStyle, comment);
    }

    @NotNull
    public ConfigSection setComment(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, String... lines) {
        return config.setComment(path, commentStyle, lines);
    }

    @NotNull
    public ConfigSection setComment(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, @Nullable List<String> lines) {
        return config.setComment(path, commentStyle, lines);
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, String... lines) {
        return config.setDefaultComment(path, lines);
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, @Nullable List<String> lines) {
        return config.setDefaultComment(path, lines);
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, ConfigFormattingRules.CommentStyle commentStyle, String... lines) {
        return config.setDefaultComment(path, commentStyle, lines);
    }

    @NotNull
    public ConfigSection setDefaultComment(@NotNull String path, ConfigFormattingRules.CommentStyle commentStyle, @Nullable List<String> lines) {
        return config.setDefaultComment(path, commentStyle, lines);
    }

    @Nullable
    public Comment getComment(@NotNull String path) {
        return config.getComment(path);
    }

    @Nullable
    public String getCommentString(@NotNull String path) {
        return config.getCommentString(path);
    }

    @NotNull
    public List<ConfigSection> getSections(String path) {
        return config.getSections(path);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, String... comment) {
        return config.set(path, value, comment);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, List<String> comment) {
        return config.set(path, value, comment);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, @Nullable ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        return config.set(path, value, commentStyle, comment);
    }

    @NotNull
    public ConfigSection set(@NotNull String path, @Nullable Object value, @Nullable ConfigFormattingRules.CommentStyle commentStyle, List<String> comment) {
        return config.set(path, value, commentStyle, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value) {
        return config.setDefault(path, value);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, String... comment) {
        return config.setDefault(path, value, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, List<String> comment) {
        return config.setDefault(path, value, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        return config.setDefault(path, value, commentStyle, comment);
    }

    @NotNull
    public ConfigSection setDefault(@NotNull String path, @Nullable Object value, ConfigFormattingRules.CommentStyle commentStyle, List<String> comment) {
        return config.setDefault(path, value, commentStyle, comment);
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, String... comment) {
        return config.createSection(path, comment);
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, @Nullable List<String> comment) {
        return config.createSection(path, comment);
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, String... comment) {
        return config.createSection(path, commentStyle, comment);
    }

    @NotNull
    public ConfigSection createSection(@NotNull String path, @Nullable ConfigFormattingRules.CommentStyle commentStyle, @Nullable List<String> comment) {
        return config.createSection(path, commentStyle, comment);
    }

    public char getChar(@NotNull String path) {
        return config.getChar(path);
    }

    public char getChar(@NotNull String path, char def) {
        return config.getChar(path, def);
    }

    @Nullable
    public CompatibleMaterial getMaterial(@NotNull String path) {
        return config.getMaterial(path);
    }

    @Nullable
    public CompatibleMaterial getMaterial(@NotNull String path, @Nullable CompatibleMaterial def) {
        return config.getMaterial(path, def);
    }

    @NotNull
    public ConfigSection getOrCreateConfigurationSection(@NotNull String path) {
        return config.getOrCreateConfigurationSection(path);
    }
}
