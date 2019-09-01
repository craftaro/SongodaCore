package com.songoda.core.configuration;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

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
        return "#" + config.getHeader().stream().collect(Collectors.joining("\n#"));
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

}
