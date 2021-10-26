package com.songoda.core;

import com.songoda.core.configuration.Config;
import com.songoda.core.database.DataManagerAbstract;
import com.songoda.core.locale.Locale;
import com.songoda.core.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * REMINDER: When converting plugins to use this, REMOVE METRICS <br>
 * Must not have two instances of Metrics enabled!
 *
 * @author jascotty2
 */
public abstract class SongodaPlugin extends JavaPlugin {
    protected Locale locale;
    protected Config config = new Config(this);
    protected long dataLoadDelay = 20L;

    protected ConsoleCommandSender console = Bukkit.getConsoleSender();
    private boolean emergencyStop = false;

    public abstract void onPluginLoad();

    public abstract void onPluginEnable();

    public abstract void onPluginDisable();

    public abstract void onDataLoad();

    /**
     * Called after reloadConfig() is called
     */
    public abstract void onConfigReload();

    /**
     * Any other plugin configuration files used by the plugin.
     *
     * @return a list of Configs that are used in addition to the main config.
     */
    public abstract List<Config> getExtraConfig();

    @Override
    public FileConfiguration getConfig() {
        return config.getFileConfig();
    }

    public Config getCoreConfig() {
        return config;
    }

    @Override
    public void reloadConfig() {
        config.load();
        onConfigReload();
    }

    @Override
    public void saveConfig() {
        config.save();
    }

    @Override
    public final void onLoad() {
        try {
            onPluginLoad();
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE,
                    "Unexpected error while loading " + getDescription().getName()
                            + " v" + getDescription().getVersion()
                            + " c" + SongodaCore.getCoreLibraryVersion()
                            + ": Disabling plugin!", t);
            emergencyStop = true;
        }
    }

    @Override
    public final void onEnable() {
        if (emergencyStop) {
            setEnabled(false);
            return;
        }

        console.sendMessage(" "); // blank line to separate chatter
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sSongoda <3!", ChatColor.GRAY.toString(),
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE.toString()));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY.toString(),
                ChatColor.GREEN.toString(), "Enabling", ChatColor.GRAY.toString()));

        try {
            locale = Locale.loadDefaultLocale(this, "en_US");
            // plugin setup
            onPluginEnable();
            // Load Data.
            Bukkit.getScheduler().runTaskLater(this, this::onDataLoad, dataLoadDelay);
            if (emergencyStop) {
                console.sendMessage(ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                console.sendMessage(" ");
                return;
            }
            // Start Metrics
            Metrics.start(this);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE,
                    "Unexpected error while loading " + getDescription().getName()
                            + " v" + getDescription().getVersion()
                            + " c" + SongodaCore.getCoreLibraryVersion()
                            + ": Disabling plugin!", t);
            emergencyStop();
            console.sendMessage(ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            console.sendMessage(" ");
            return;
        }

        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(" "); // blank line to separate chatter
    }

    protected void emergencyStop() {
        emergencyStop = true;
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public final void onDisable() {
        if (emergencyStop) {
            return;
        }
        console.sendMessage(" "); // blank line to speparate chatter
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sSongoda <3!", ChatColor.GRAY.toString(),
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE.toString()));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY.toString(),
                ChatColor.RED.toString(), "Disabling", ChatColor.GRAY.toString()));
        onPluginDisable();
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(" "); // blank line to speparate chatter
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }

    public Locale getLocale() {
        return locale;
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
        if (locale != null && locale.getName().equals(localeName)) {
            return !reload || locale.reloadMessages();
        } else {
            Locale l = Locale.loadLocale(this, localeName);
            if (l != null) {
                locale = l;
                return true;
            } else {
                return false;
            }
        }
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
}
