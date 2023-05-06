package com.songoda.core.configuration.songoda;

import com.songoda.core.configuration.ConfigEntry;
import com.songoda.core.configuration.ReadOnlyConfigEntry;
import com.songoda.core.configuration.yaml.YamlConfigEntry;
import com.songoda.core.configuration.yaml.YamlConfiguration;
import com.songoda.core.utils.Pair;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: replace all config related exceptions with custom exceptions
// TODO: Allow registering load-Listeners
// TODO: Provide method to only save if changed
public class SongodaYamlConfig extends YamlConfiguration {
    protected static final String CANNOT_CREATE_BACKUP_COPY_EXCEPTION_PREFIX = "Unable to create backup copy of config file: ";

    public final @NotNull File file;
    protected final @NotNull Logger logger;

    private int targetVersion;
    private ConfigEntry versionEntry;

    protected final Map<String, ConfigEntry> configEntries = new LinkedHashMap<>(0);

    public SongodaYamlConfig(@NotNull JavaPlugin plugin, @NotNull File file) {
        this(file, plugin.getLogger());
    }

    public SongodaYamlConfig(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        this(new File(plugin.getDataFolder(), fileName), plugin.getLogger());
    }

    public SongodaYamlConfig(@NotNull File file) {
        this(file, null);
    }

    public SongodaYamlConfig(@NotNull File file, @Nullable Logger logger) {
        super();

        this.file = Objects.requireNonNull(file);
        this.logger = logger != null ? logger : Logger.getLogger(getClass().getName());
    }

    /**
     * Calls {@link #load()} and then {@link #save()}.<br>
     * <br>
     * As this is intended to keep the {@link org.bukkit.plugin.java.JavaPlugin#onEnable()} method clean,
     * it catches all exceptions and logs them instead.<br>
     * <br>
     * If this method returns false, the plugins should be disabled.
     *
     * @return true if the load and save were successful, false if an exception was thrown.
     *
     * @see #save()
     * @see #load()
     */
    public boolean init() {
        try {
            this.load();
            this.save();

            return true;
        } catch (IOException ex) {
            this.logger.log(Level.SEVERE, "Failed to load config file: " + this.file.getPath(), ex);
        }

        return false;
    }

    public ReadOnlyConfigEntry getReadEntry(@NotNull String key) {
        return new ReadOnlyConfigEntry(this, key);
    }

    public ConfigEntry createEntry(@NotNull String key) {
        return createEntry(key, null);
    }

    public ConfigEntry createEntry(@NotNull String key, @Nullable Object defaultValue) {
        ConfigEntry entry = new YamlConfigEntry(this, key, defaultValue);

        if (this.configEntries.putIfAbsent(key, entry) != null) {
            throw new IllegalArgumentException("Entry already exists for key: " + key);
        }

        if (entry.get() == null) {
            entry.set(defaultValue);
        }

        return entry;
    }

    public SongodaYamlConfig withVersion(int version) {
        return withVersion("version", version, () -> "Don't touch this – it's used to track the version of the config.");
    }

    public SongodaYamlConfig withVersion(@NotNull String key, int version, @Nullable Supplier<String> comment) {
        if (version < 0) {
            throw new IllegalArgumentException("Version must be positive");
        }

        if (this.versionEntry != null) {
            this.versionEntry.set(null);
        }

        this.targetVersion = version;

        this.versionEntry = new YamlConfigEntry(this, key, 0);
        this.versionEntry.withComment(comment);
        this.versionEntry.set(this.targetVersion);

        return this;
    }

    public void load() throws IOException {
        try (Reader reader = Files.newBufferedReader(this.file.toPath(), StandardCharsets.UTF_8)) {
            load(reader);
        } catch (FileNotFoundException ignore) {
        } catch (IOException ex) {
            throw new IOException("Unable to load '" + this.file.getPath() + "'", ex);
        }
    }

    public void save() throws IOException {
        Files.createDirectories(this.file.toPath().getParent());


        try (Writer writer = Files.newBufferedWriter(this.file.toPath(), StandardCharsets.UTF_8)) {
            super.save(writer);
        } catch (IOException ex) {
            throw new IOException("Unable to save '" + this.file.getPath() + "'", ex);
        }
    }

    @Override
    public void load(Reader reader) throws IOException {
        super.load(reader);

        upgradeOldConfigVersion();

        for (ConfigEntry entry : this.configEntries.values()) {
            if (entry.get() == null && entry.getDefaultValue() != null) {
                entry.set(entry.getDefaultValue());
            }
        }
    }

    /**
     * @return false, if no config version is set or no upgrade is needed
     */
    protected boolean upgradeOldConfigVersion() throws IOException {
        if (this.versionEntry == null) {
            return false;
        }

        if (this.versionEntry.getInt() > this.targetVersion) {
            throw new IllegalStateException("Cannot upgrade a config version that is higher than the target version");
        }
        if (this.versionEntry.getInt() == this.targetVersion) {
            return false;
        }

        createBackupCopyFile();

        while (this.versionEntry.getInt() < this.targetVersion) {
            upgradeOldConfigVersionByOne();
        }

        cleanValuesMap(this.values);

        return true;
    }

    protected void upgradeOldConfigVersionByOne() {
        int currentVersion = this.versionEntry.getInt();
        int targetVersion = currentVersion + 1;

        if (targetVersion > this.targetVersion) {
            throw new IllegalStateException("Cannot upgrade a config version that is higher than the target version");
        }

        for (ConfigEntry entry : this.configEntries.values()) {
            if (entry.getUpgradeSteps() == null) {
                continue;
            }

            Pair<@Nullable String, @Nullable Function<Object, Object>> upgradeStep = entry.getUpgradeSteps().get(currentVersion);
            if (upgradeStep == null) {
                continue;
            }

            String oldEntryKey = upgradeStep.getFirst();
            if (oldEntryKey == null) {
                oldEntryKey = entry.getKey();
            }

            Object newValue = get(oldEntryKey);
            if (upgradeStep.getSecond() != null) {
                newValue = upgradeStep.getSecond().apply(newValue);
            }

            set(oldEntryKey, null);
            entry.set(newValue);
        }

        this.versionEntry.set(targetVersion);
    }

    protected void createBackupCopyFile() throws IOException {
        if (!this.file.exists()) {
            return;
        }

        try {
            Path targetPath = this.file.toPath().resolveSibling(this.file.getPath() + ".backup-" + System.currentTimeMillis());

            Files.copy(
                    this.file.toPath(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            this.logger.info("Created backup copy of config file '" + this.file.getPath() + "' to '" + targetPath + "'");
        } catch (IOException ex) {
            throw new IOException(CANNOT_CREATE_BACKUP_COPY_EXCEPTION_PREFIX + this.file.getPath(), ex);
        }
    }
}
