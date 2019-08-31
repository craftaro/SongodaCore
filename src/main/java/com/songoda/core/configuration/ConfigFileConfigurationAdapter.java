package com.songoda.core.configuration;

import java.util.stream.Collectors;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigFileConfigurationAdapter extends FileConfiguration {

    final Config config;

    public ConfigFileConfigurationAdapter(Config config) {
        super(config);
        this.config = config;
    }

    public Config getConfig() {
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
}
