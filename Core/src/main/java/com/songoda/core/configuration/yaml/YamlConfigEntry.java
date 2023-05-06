package com.songoda.core.configuration.yaml;

import com.songoda.core.configuration.ConfigEntry;
import com.songoda.core.configuration.IConfiguration;
import com.songoda.core.configuration.WriteableConfigEntry;
import com.songoda.core.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class YamlConfigEntry implements WriteableConfigEntry {
    protected final @NotNull YamlConfiguration config;
    protected final @NotNull String key;
    protected @Nullable Object defaultValue;

    protected @Nullable Map<Integer, Pair<@Nullable String, @Nullable Function<@Nullable Object, @Nullable Object>>> upgradeSteps;

    public YamlConfigEntry(@NotNull YamlConfiguration config, @NotNull String key, @Nullable Object defaultValue) {
        this.config = config;
        this.key = key;
        this.defaultValue = defaultValue;
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
    public @Nullable Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public @Nullable Map<Integer, Pair<@Nullable String, @Nullable Function<@Nullable Object, @Nullable Object>>> getUpgradeSteps() {
        return this.upgradeSteps;
    }

    @Override
    public void setDefaultValue(@Nullable Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public ConfigEntry withDefaultValue(@Nullable Object defaultValue) {
        this.setDefaultValue(defaultValue);
        return this;
    }

    @Override
    public ConfigEntry withUpgradeStep(int version, @Nullable String keyInGivenVersion, @Nullable Function<@Nullable Object, @Nullable Object> valueConverter) {
        if (keyInGivenVersion == null && valueConverter == null) {
            throw new IllegalArgumentException("You must provide either a key or a value converter");
        }

        if (this.upgradeSteps == null) {
            this.upgradeSteps = new HashMap<>(1);
        }

        this.upgradeSteps.put(version, new Pair<>(keyInGivenVersion, valueConverter));

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YamlConfigEntry that = (YamlConfigEntry) o;
        return this.config.equals(that.config) &&
                this.key.equals(that.key) &&
                Objects.equals(this.defaultValue, that.defaultValue) &&
                Objects.equals(this.upgradeSteps, that.upgradeSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.config, this.key, this.defaultValue, this.upgradeSteps);
    }
}
