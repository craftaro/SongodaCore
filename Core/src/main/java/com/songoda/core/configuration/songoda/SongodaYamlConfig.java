package com.songoda.core.configuration.songoda;

import com.songoda.core.configuration.yaml.YamlConfiguration;
import com.songoda.core.utils.Pair;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

// TODO: Allow registering load-Listeners
public class SongodaYamlConfig extends YamlConfiguration {
    protected final String cannotCreateBackupCopyExceptionPrefix = "Unable to create backup copy of config file: ";

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
     * As this is intered to keep the {@link org.bukkit.plugin.java.JavaPlugin#onEnable()} method clean,
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
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failed to load config file: " + this.file.getPath(), e);
        }

        return false;
    }

    protected void registerConfigEntry(ConfigEntry entry) {
        this.configEntries.put(entry.key, entry);
    }

    public void unregisterConfigEntry(ConfigEntry entry) {
        unregisterConfigEntry(entry.key);
    }

    public void unregisterConfigEntry(String key) {
        this.configEntries.remove(key);
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

        this.versionEntry = new ConfigEntry(this, key, 0);
        this.versionEntry.withComment(comment);
        this.versionEntry.set(this.targetVersion);

        return this;
    }

    public void load() throws IOException {
        try (Reader reader = new FileReader(this.file)) {
            load(reader);
        } catch (FileNotFoundException ignore) {
        } catch (IOException e) {
            throw new IOException("Unable to load '" + this.file.getPath() + "'", e);
        }
    }

    public void save() throws IOException {
        Files.createDirectories(this.file.toPath().getParent());

        try (Writer writer = new FileWriter(this.file)) {
            super.save(writer);
        } catch (IOException e) {
            throw new IOException("Unable to save '" + this.file.getPath() + "'", e);
        }
    }

    @Override
    public void load(Reader reader) {
        super.load(reader);

        // The interface does not allow to throw an exception, so we log it instead.
        try {
            upgradeOldConfigVersion();
        } catch (IOException ex) {
            if (ex.getMessage().startsWith(this.cannotCreateBackupCopyExceptionPrefix)) {
                // Failed to create backup copy, but we can still continue.
                this.logger.log(Level.SEVERE, null, ex);
            } else {
                // This is a real unexpected exception, so we rethrow it.
                throw new IllegalStateException(ex);
            }
        }

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

        return true;
    }

    protected void upgradeOldConfigVersionByOne() {
        int currentVersion = this.versionEntry.getInt();
        int targetVersion = currentVersion + 1;

        if (targetVersion > this.targetVersion) {
            throw new IllegalStateException("Cannot upgrade a config version that is higher than the target version");
        }

        for (ConfigEntry entry : this.configEntries.values()) {
            if (entry.upgradeStepsForVersion == null) {
                continue;
            }

            Pair<@Nullable String, @Nullable Function<Object, Object>> upgradeStep = entry.upgradeStepsForVersion.get(currentVersion);
            if (upgradeStep == null) {
                continue;
            }

            String oldEntryKey = upgradeStep.getFirst();
            if (oldEntryKey == null) {
                oldEntryKey = entry.key;
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
            Path targetPath = this.file.toPath().resolveSibling(this.file.getPath() + ".backup" + System.currentTimeMillis());

            Files.copy(
                    this.file.toPath(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            this.logger.info("Created backup copy of config file '" + this.file.getPath() + "' to '" + targetPath + "'");
        } catch (IOException ex) {
            throw new IOException(this.cannotCreateBackupCopyExceptionPrefix + this.file.getPath(), ex);
        }
    }
}
