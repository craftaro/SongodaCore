package com.songoda.core.configuration;

import com.songoda.core.utils.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ReadOnlyConfigEntry implements ConfigEntry {
    protected final @NotNull IConfiguration config;
    protected final @NotNull String key;

    public ReadOnlyConfigEntry(@NotNull IConfiguration config, @NotNull String key) {
        this.config = config;
        this.key = key;
    }

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public @NotNull IConfiguration getConfig() {
        return this.config;
    }

    @Override
    @Contract(" -> null")
    public @Nullable Object getDefaultValue() {
        return null;
    }

    @Override
    @Contract("_ -> fail")
    public void setDefaultValue(@Nullable Object defaultValue) {
        throw new UnsupportedOperationException("Cannot set defaultValue on a read-only config entry");
    }

    @Override
    @Contract("_ -> fail")
    public ConfigEntry withDefaultValue(@Nullable Object defaultValue) {
        throw new UnsupportedOperationException("Cannot set defaultValue on a read-only config entry");
    }

    @Override
    @Contract("_ -> fail")
    public ConfigEntry withComment(Supplier<String> comment) {
        throw new UnsupportedOperationException("Cannot set comment on a read-only config entry");
    }

    @Override
    @Contract(" -> null")
    public Map<Integer, Pair<String, Function<Object, Object>>> getUpgradeSteps() {
        return null;
    }

    @Override
    @Contract("_, _, _ -> fail")
    public ConfigEntry withUpgradeStep(int version, @Nullable String keyInGivenVersion, @Nullable Function<Object, Object> valueConverter) {
        throw new UnsupportedOperationException("Cannot set upgrade step on a read-only config entry");
    }

    @Override
    @Contract("_ -> fail")
    public void set(@Nullable Object value) {
        throw new UnsupportedOperationException("Cannot set value on a read-only config entry");
    }
}
