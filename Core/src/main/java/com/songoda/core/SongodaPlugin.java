package com.songoda.core;

import com.songoda.core.configuration.songoda.SongodaYamlConfig;
import com.songoda.core.database.DataManagerAbstract;
import com.songoda.core.locale.Locale;
import com.songoda.core.utils.Metrics;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * REMINDER: When converting plugins to use this, REMOVE METRICS <br>
 * Must not have two instances of Metrics enabled!
 */
public abstract class SongodaPlugin extends JavaPlugin {
    protected Locale locale;
    protected long dataLoadDelay = 20L;

    private boolean emergencyStop = false;

    static {
        /* NBT-API */
        MinecraftVersion.getLogger().setLevel(Level.WARNING);
        MinecraftVersion.disableUpdateCheck();
    }

    public abstract void onPluginLoad();

    public abstract void onPluginEnable();

    public abstract void onPluginDisable();

    public abstract void onDataLoad();

    /**
     * All the configuration files belonging to the plugin.<br>
     * This might for example be used for the ingame config editor.<br>
     * <br>
     * Do not include *storage* files here or anything similar that does not intend external modification and access.<br>
     * <br>
     * Do not include language files if you are using the Core's localization system.
     */
    public abstract @NotNull List<SongodaYamlConfig> getConfigs();

    @Override
    public final void onLoad() {
        try {
            onPluginLoad();
        } catch (Throwable th) {
            criticalErrorOnPluginStartup(th);
        }
    }

    @Override
    public final void onEnable() {
        if (this.emergencyStop) {
            setEnabled(false);

            return;
        }

        CommandSender console = Bukkit.getConsoleSender();

        console.sendMessage(" "); // blank line to separate chatter
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sSongoda <3!", ChatColor.GRAY,
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY,
                ChatColor.GREEN, "Enabling", ChatColor.GRAY));

        try {
            this.locale = Locale.loadDefaultLocale(this, "en_US");

            // plugin setup
            onPluginEnable();

            if (this.emergencyStop) {
                return;
            }

            // Load Data.
            Bukkit.getScheduler().runTaskLater(this, this::onDataLoad, this.dataLoadDelay);

            if (this.emergencyStop) {
                console.sendMessage(ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                console.sendMessage(" ");
                return;
            }

            // Start Metrics
            Metrics.start(this);
        } catch (Throwable th) {
            criticalErrorOnPluginStartup(th);

            console.sendMessage(ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            console.sendMessage(" ");

            return;
        }

        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(" "); // blank line to separate chatter
    }

    @Override
    public final void onDisable() {
        if (this.emergencyStop) {
            return;
        }

        CommandSender console = Bukkit.getConsoleSender();

        console.sendMessage(" "); // blank line to separate chatter
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sSongoda <3!", ChatColor.GRAY,
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY,
                ChatColor.RED, "Disabling", ChatColor.GRAY));

        onPluginDisable();

        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(" "); // blank line to separate chatter
    }

    public Locale getLocale() {
        return this.locale;
    }

    /**
     * Set the plugin's locale to a specific language
     *
     * @param localeName locale to use, eg "en_US"
     * @param reload     optionally reload the loaded locale if the locale didn't
     *                   change
     *
     * @return true if the locale exists and was loaded successfully
     */
    public boolean setLocale(String localeName, boolean reload) {
        if (this.locale != null && this.locale.getName().equals(localeName)) {
            return !reload || this.locale.reloadMessages();
        }

        Locale l = Locale.loadLocale(this, localeName);
        if (l != null) {
            this.locale = l;
            return true;
        }

        return false;
    }

    protected void shutdownDataManager(DataManagerAbstract dataManager) {
        // 3 minutes is overkill, but we just want to make sure
        shutdownDataManager(dataManager, 15, TimeUnit.MINUTES.toSeconds(3));
    }

    protected void shutdownDataManager(DataManagerAbstract dataManager, int reportInterval, long secondsUntilForceShutdown) {
        dataManager.shutdownTaskQueue();

        while (!dataManager.isTaskQueueTerminated() && secondsUntilForceShutdown > 0) {
            long secondsToWait = Math.min(reportInterval, secondsUntilForceShutdown);

            try {
                if (dataManager.waitForShutdown(secondsToWait, TimeUnit.SECONDS)) {
                    break;
                }

                getLogger().info(String.format("A DataManager is currently working on %d tasks... " +
                                "We are giving him another %d seconds until we forcefully shut him down " +
                                "(continuing to report in %d second intervals)",
                        dataManager.getTaskQueueSize(), secondsUntilForceShutdown, reportInterval));
            } catch (InterruptedException ignore) {
            } finally {
                secondsUntilForceShutdown -= secondsToWait;
            }
        }

        if (!dataManager.isTaskQueueTerminated()) {
            int unfinishedTasks = dataManager.forceShutdownTaskQueue().size();

            if (unfinishedTasks > 0) {
                getLogger().log(Level.WARNING,
                        String.format("A DataManager has been forcefully terminated with %d unfinished tasks - " +
                                "This can be a serious problem, please report it to us (Songoda)!", unfinishedTasks));
            }
        }
    }

    protected void emergencyStop() {
        this.emergencyStop = true;

        Bukkit.getPluginManager().disablePlugin(this);
    }

    /**
     * Logs one or multiple errors that occurred during plugin startup and calls {@link #emergencyStop()} afterwards
     *
     * @param th The error(s) that occurred
     */
    protected void criticalErrorOnPluginStartup(Throwable th) {
        Bukkit.getLogger().log(Level.SEVERE,
                String.format(
                        "Unexpected error while loading %s v%s (core v%s): Disabling plugin!",
                        getDescription().getName(),
                        getDescription().getVersion(),
                        SongodaCore.getCoreLibraryVersion()
                ), th);

        emergencyStop();
    }

    /**
     * Use {@link SongodaYamlConfig} instead.
     */
    @Deprecated
    @Override
    public @NotNull FileConfiguration getConfig() {
        return super.getConfig();
    }

    /**
     * Use {@link SongodaYamlConfig} instead.
     */
    @Deprecated
    @Override
    public void reloadConfig() {
    }

    /**
     * Use {@link SongodaYamlConfig} instead.
     */
    @Deprecated
    @Override
    public void saveConfig() {
    }
}
