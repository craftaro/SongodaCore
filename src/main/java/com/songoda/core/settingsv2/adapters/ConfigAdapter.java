package com.songoda.core.settingsv2.adapters;

import com.songoda.core.settingsv2.Config;
import java.util.List;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigAdapter extends YamlConfigurationOptions {

    public ConfigAdapter(Config configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public Config configuration() {
        return (Config) super.configuration();
    }

    @NotNull
    @Override
    public YamlConfigurationOptions copyDefaults(boolean value) {
        // we always copy new values
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions pathSeparator(char value) {
        ((Config) super.configuration()).setPathSeparator(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions header(@Nullable String value) {
        if (value == null) {
            ((Config) super.configuration()).setHeader((List) null);
        } else {
            ((Config) super.configuration()).setHeader(value.split("\n"));
        }
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions copyHeader(boolean value) {
        if (!value) {
            ((Config) super.configuration()).setHeader((List) null);
        }
        return this;
    }

    @Override
    public int indent() {
        return ((Config) super.configuration()).getIndent();
    }

    @NotNull
    @Override
    public YamlConfigurationOptions indent(int value) {
        ((Config) super.configuration()).setIndent(value);
        return this;
    }
}
