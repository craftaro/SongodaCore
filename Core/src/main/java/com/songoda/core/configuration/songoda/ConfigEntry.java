package com.songoda.core.configuration.songoda;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.configuration.NodeCommentable;
import com.songoda.core.utils.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigEntry {
    public final @NotNull SongodaYamlConfig config;
    public final @NotNull String key;
    protected @Nullable Object defaultValue;

    protected @Nullable Map<Integer, Pair<@Nullable String, @Nullable Function<@Nullable Object, @Nullable Object>>> upgradeStepsForVersion;

    public ConfigEntry(@NotNull SongodaYamlConfig config, @NotNull String key) {
        this(config, key, null);
    }

    public ConfigEntry(@NotNull SongodaYamlConfig config, @NotNull String key, @Nullable Object defaultValue) {
        this.config = Objects.requireNonNull(config);
        this.key = Objects.requireNonNull(key);
        this.defaultValue = defaultValue;

        if (get() == null) {
            set(this.defaultValue);
        }

        this.config.registerConfigEntry(this);
    }

    public @Nullable Object getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(@Nullable Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @see #withComment(Supplier)
     */
    public ConfigEntry withComment(String comment) {
        return this.withComment(() -> comment);
    }

    /**
     * @see NodeCommentable#setNodeComment(String, Supplier)
     */
    public ConfigEntry withComment(Supplier<String> comment) {
        ((NodeCommentable) this.config).setNodeComment(this.key, comment);

        return this;
    }

    /**
     * @see #withUpgradeStep(int, String, Function)
     */
    public ConfigEntry withUpgradeStep(int version, @NotNull String keyInGivenVersion) {
        return withUpgradeStep(version, keyInGivenVersion, null);
    }

    /**
     * @param version           The version to upgrade from (e.g. 1 for the upgrade from 1 to 2)
     * @param keyInGivenVersion The old key in the given version or null if it didn't change
     * @param valueConverter    A function that converts the old version's value to a new one, or null if it didn't change
     */
    @Contract("_, null, null -> fail")
    public ConfigEntry withUpgradeStep(int version, @Nullable String keyInGivenVersion, @Nullable Function<Object, Object> valueConverter) {
        if (keyInGivenVersion == null && valueConverter == null) {
            throw new IllegalArgumentException("You must provide either a key or a value converter");
        }

        if (this.upgradeStepsForVersion == null) {
            this.upgradeStepsForVersion = new HashMap<>(1);
        }

        this.upgradeStepsForVersion.put(version, new Pair<>(keyInGivenVersion, valueConverter));

        return this;
    }

    /**
     * @see SongodaYamlConfig#has(String)
     */
    public boolean has() {
        return this.config.has(this.key);
    }

    /**
     * @see SongodaYamlConfig#set(String, Object)
     */
    public Object set(@Nullable Object value) {
        // TODO: Test what happens if the value is an enum (CompatibleMaterial)
        return this.config.set(this.key, value);
    }

    /**
     * @see SongodaYamlConfig#get(String)
     */
    public @Nullable Object get() {
        return this.config.get(this.key);
    }

    /**
     * @see SongodaYamlConfig#getOr(String, Object)
     */
    public @Nullable Object getOr(@Nullable Object fallbackValue) {
        return this.config.getOr(this.key, fallbackValue);
    }

    public @Nullable String getString() {
        Object value = get();

        return value != null ? value.toString() : null;
    }

    public @Nullable String getString(String fallbackValue) {
        Object value = get();

        return value == null ? fallbackValue : value.toString();
    }

    /**
     * @see #getInt(int)
     */
    public int getInt() {
        return getInt(0);
    }

    /**
     * Returns the values parsed as an integer.<br>
     * If it is a floating point number, it will be rounded down.
     *
     * @see Double#valueOf(String)
     */
    public int getInt(int fallbackValue) {
        String value = getString();

        if (value == null) {
            return fallbackValue;
        }

        return Double.valueOf(value).intValue();
    }

    /**
     * @see #getDouble(double)
     */
    public double getDouble() {
        return getDouble(0);
    }

    /**
     * Returns the values parsed as a double.
     *
     * @see Double#parseDouble(String)
     */
    public double getDouble(double fallbackValue) {
        String value = getString();

        if (value == null) {
            return fallbackValue;
        }

        return Double.parseDouble(value);
    }

    /**
     * @see #getBoolean(boolean)
     */
    public boolean getBoolean() {
        return getBoolean(false);
    }

    /**
     * Returns the values parsed as a boolean.
     *
     * @see Boolean#parseBoolean(String)
     */
    public boolean getBoolean(boolean fallbackValue) {
        String value = getString();

        if (value == null) {
            return fallbackValue;
        }

        return Boolean.parseBoolean(value);
    }

    public @Nullable List<String> getStringList() {
        return getStringList(null);
    }

    @Contract("!null -> !null")
    public @Nullable List<String> getStringList(List<String> fallbackValue) {
        Object value = get();

        if (value instanceof List) {
            //noinspection unchecked
            return (List<String>) value;
        }

        return fallbackValue;
    }

    /**
     * @see #getMaterial(CompatibleMaterial)
     */
    public CompatibleMaterial getMaterial() {
        return getMaterial(null);
    }

    /**
     * @see CompatibleMaterial#getMaterial(String)
     */
    public @Nullable CompatibleMaterial getMaterial(@Nullable CompatibleMaterial defaultValue) {
        String value = getString();

        if (value == null) {
            return defaultValue;
        }

        return CompatibleMaterial.getMaterial(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigEntry that = (ConfigEntry) o;

        return this.config.equals(that.config) &&
                this.key.equals(that.key) &&
                Objects.equals(this.defaultValue, that.defaultValue) &&
                Objects.equals(this.upgradeStepsForVersion, that.upgradeStepsForVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.config, this.key, this.defaultValue, this.upgradeStepsForVersion);
    }
}
