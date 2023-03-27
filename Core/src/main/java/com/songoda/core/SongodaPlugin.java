package com.songoda.core;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import com.songoda.core.actions.ActionManager;
import com.songoda.core.builtin.SongodaCoreCommand;
import com.songoda.core.configuration.Config;
import com.songoda.core.database.DataManager;
import com.songoda.core.database.DataMigration;
import com.songoda.core.database.DatabaseType;
import com.songoda.core.placeholder.IPlaceholderResolver;
import com.songoda.core.placeholder.NoPluginResolver;
import com.songoda.core.placeholder.PlaceholderAPIResolver;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public abstract class SongodaPlugin extends JavaPlugin {

    static {
        /* NBT-API */
        MinecraftVersion.getLogger().setLevel(Level.WARNING);
        MinecraftVersion.disableUpdateCheck();
        // Disable tips and logo for Jooq
        System.setProperty("org.jooq.no-tips", "true");
        System.setProperty("org.jooq.no-logo", "true");
    }

    private BukkitCommandHandler commandManager;
    private BukkitAudiences adventure;
    private IPlaceholderResolver placeholderResolver;
    private ActionManager actionManager;
    private TaskChainFactory taskChainFactory;
    private DataManager dataManager;

    public abstract void onPluginEnable();
    public abstract void onPluginDisable();

    protected abstract int getPluginId();
    protected abstract String getPluginIcon();

    @Override
    public final void onEnable() {
        SongodaCore.getInstance().registerPlugin(this, getPluginId(), getPluginIcon());

        this.commandManager = BukkitCommandHandler.create(this);
        this.adventure = BukkitAudiences.create(this);
        this.actionManager = new ActionManager(this);
        this.taskChainFactory = BukkitTaskChainFactory.create(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderResolver = new PlaceholderAPIResolver();
        }else{
            this.placeholderResolver = new NoPluginResolver();
        }

        onPluginEnable();
    }
    /**
     * Get the DataManager for this plugin.
     * Note: Make sure to call initDatabase() in onPluginEnable() before using this.
     * @return DataManager for this plugin.
     */
    public DataManager getDataManager() {
        return dataManager;
    }

    /**
     * Initialize the DataManager for this plugin and convert from SQLite to H2 if needed.
     */
    protected void initDatabase() {
        this.dataManager = new DataManager(this, Collections.emptyList());
        if (dataManager.getDatabaseConnector().isInitialized()) {
            //Check if the type is SQLite
            if (dataManager.getDatabaseConnector().getType() == DatabaseType.SQLITE) {
                //Let's convert it to H2
                DataManager newDataManager = DataMigration.convert(this, DatabaseType.H2);
                if (newDataManager != null && newDataManager.getDatabaseConnector().isInitialized()) {
                    //Set the new data manager
                    setDataManager(newDataManager);
                }
            }
        }
    }

    /**
     * Initialize the DataManager for this plugin.
     * @param migrations List of migrations to run.
     */
    protected void initDatabase(List<DataMigration> migrations) {
        this.dataManager = new DataManager(this, migrations);
    }

    /**
     * Set the DataManager for this plugin.
     * Used for converting from one database to another.
     */
    public void setDataManager(DataManager dataManager) {
        if (dataManager == null) throw new IllegalArgumentException("DataManager cannot be null!");
        if (this.dataManager == dataManager) return;
        //Make sure to shut down the old data manager.
        if (this.dataManager != null) {
            dataManager.shutdown();
        }
        this.dataManager = dataManager;
    }

    /**
     * Create a configuration file that does NOT update.
     * @param file File to create.
     * @return The configuration file created.
     */
    public Config createConfig(File file) {
        return new Config(file);
    }

    /**
     * Create a configuration file that automatically updates. Requires config-version to be defined.
     * @param file File to create.
     * @return The configuration file created.
     */
    public Config createUpdatingConfig(File file) {
        return new Config(this, file);
    }

    public Config getDatabaseConfig() {
        return new Config(this, new File(getDataFolder(), "database.yml"));
    }

    @Override
    public final void onDisable() {
        onPluginDisable();
        if (dataManager != null) {
            dataManager.shutdown();
        }
    }

    /**
     * Use {@link com.songoda.core.configuration.Config} instead.
     */
    @Deprecated
    @Override
    public @NotNull FileConfiguration getConfig() {
        return super.getConfig();
    }

    /**
     * Use {@link com.songoda.core.configuration.Config} instead.
     */
    @Deprecated
    @Override
    public void reloadConfig() {
    }

    /**
     * Use {@link com.songoda.core.configuration.Config} instead.
     */
    @Deprecated
    @Override
    public void saveConfig() {
    }

    public BukkitCommandHandler getCommandManager() {
        return commandManager;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public IPlaceholderResolver getPlaceholderResolver() {
        return placeholderResolver;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }
}
