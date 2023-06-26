package com.craftaro.core;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import com.craftaro.core.actions.ActionManager;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.database.DataManager;
import com.craftaro.core.database.DataMigration;
import com.craftaro.core.database.DatabaseType;
import com.craftaro.core.hooks.economy.EconomyManager;
import com.craftaro.core.hooks.economy.IEconomy;
import com.craftaro.core.hooks.holograms.AbstractHologram;
import com.craftaro.core.hooks.holograms.HologramManager;
import com.craftaro.core.hooks.protection.ProtectionManager;
import com.craftaro.core.hooks.protection.ProtectionSet;
import com.craftaro.core.placeholder.IPlaceholderResolver;
import com.craftaro.core.placeholder.NoPluginResolver;
import com.craftaro.core.placeholder.PlaceholderAPIResolver;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public abstract class CraftaroPlugin extends JavaPlugin {

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
    private IEconomy economyHook;
    private AbstractHologram hologramHook;
    private ProtectionSet protectionHooks;

    public abstract void onPluginEnable();
    public abstract void onPluginDisable();

    protected abstract int getPluginId();
    protected abstract String getPluginIcon();

    @Override
    public final void onEnable() {
        CraftaroCore.getInstance().registerPlugin(this, getPluginId(), getPluginIcon());

        this.commandManager = BukkitCommandHandler.create(this);
        this.adventure = BukkitAudiences.create(this);
        this.actionManager = new ActionManager(this);
        this.taskChainFactory = BukkitTaskChainFactory.create(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.placeholderResolver = new PlaceholderAPIResolver();
        }else{
            this.placeholderResolver = new NoPluginResolver();
        }

        Config hooksConfig = getHooksConfig();
        this.economyHook = new EconomyManager(this).getHookByName(hooksConfig.getString("Default Economy Hook"));
        this.hologramHook = new HologramManager(this).getHookByName(hooksConfig.getString("Default Hologram Hook"));
        this.protectionHooks = new ProtectionManager(this).getHooksByName(hooksConfig.getStringList("Default Protection Hook"));

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
     * Create a configuration file that automatically updates.
     * @param file File to create.
     * @return The configuration file created.
     */
    public Config createUpdatingConfig(File file) {
        return new Config(this, file);
    }

    public Config getDatabaseConfig() {
        return new Config(this, new File(getDataFolder(), "database.yml"));
    }

    public Config getHooksConfig() {
        return new Config(this, new File(getDataFolder(), "hooks.yml"));
    }

    @Override
    public final void onDisable() {
        onPluginDisable();
        if (dataManager != null) {
            dataManager.shutdown();
        }
    }

    /**
     * Use {@link Config} instead.
     */
    @Deprecated
    @Override
    public @NotNull FileConfiguration getConfig() {
        return super.getConfig();
    }

    /**
     * Use {@link Config} instead.
     */
    @Deprecated
    @Override
    public void reloadConfig() {
    }

    /**
     * Use {@link Config} instead.
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

    public IEconomy getEconomyHook() {
        return economyHook;
    }

    public AbstractHologram getHologramHook() {
        return hologramHook;
    }

    public ProtectionSet getProtectionHook() {
        return protectionHooks;
    }
}
