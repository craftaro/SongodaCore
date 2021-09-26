package com.songoda.core.configuration;

import org.bukkit.configuration.file.FileConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigOptionsAdapter extends FileConfigurationOptions {
    final ConfigSection config;

    public ConfigOptionsAdapter(ConfigSection config) {
        super(config);
        this.config = config;
    }

    public Config getConfig() {
        return (Config) config.root;
    }

    @NotNull
    @Override
    public ConfigFileConfigurationAdapter configuration() {
        return new ConfigFileConfigurationAdapter((Config) config.root);
    }

    @NotNull
    @Override
    public ConfigOptionsAdapter copyDefaults(boolean value) {
        // we always copy new values
        return this;
    }

    @NotNull
    @Override
    public ConfigOptionsAdapter pathSeparator(char value) {
        (config.root).setPathSeparator(value);
        return this;
    }

    @NotNull
    @Override
    public ConfigOptionsAdapter header(@Nullable String value) {
        if (value == null) {
            ((Config) config.root).setHeader((List) null);
        } else {
            ((Config) config.root).setHeader(value.split("\n"));
        }

        return this;
    }

    @NotNull
    @Override
    public ConfigOptionsAdapter copyHeader(boolean value) {
        if (!value) {
            ((Config) config.root).setHeader((List) null);
        }

        return this;
    }

    public int indent() {
        return config.root.getIndent();
    }

    @NotNull
    public ConfigOptionsAdapter indent(int value) {
        config.root.setIndent(value);
        return this;
    }
}
