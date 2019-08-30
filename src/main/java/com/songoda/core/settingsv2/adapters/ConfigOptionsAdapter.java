package com.songoda.core.settingsv2.adapters;

import com.songoda.core.settingsv2.Config;
import com.songoda.core.settingsv2.SongodaConfigurationSection;
import java.util.List;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigOptionsAdapter extends MemoryConfigurationOptions {

    public ConfigOptionsAdapter(SongodaConfigurationSection root) {
        super(root);
    }

    @NotNull
    @Override
    public Config configuration() {
        return (Config) super.configuration();
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
        ((Config) super.configuration()).setPathSeparator(value);
        return this;
    }

    @NotNull
    public ConfigOptionsAdapter header(@Nullable String value) {
        if (value == null) {
            ((Config) super.configuration()).setHeader((List) null);
        } else {
            ((Config) super.configuration()).setHeader(value.split("\n"));
        }
        return this;
    }

    @NotNull
    public ConfigOptionsAdapter copyHeader(boolean value) {
        if (!value) {
            ((Config) super.configuration()).setHeader((List) null);
        }
        return this;
    }

    public int indent() {
        return ((Config) super.configuration()).getIndent();
    }

    @NotNull
    public ConfigOptionsAdapter indent(int value) {
        ((Config) super.configuration()).setIndent(value);
        return this;
    }
}
