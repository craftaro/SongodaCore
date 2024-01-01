package com.craftaro.core;

import com.craftaro.core.configuration.Config;
import com.craftaro.core.database.DataManager;
import com.craftaro.core.database.DataMigration;
import com.craftaro.core.database.DatabaseType;
import com.craftaro.core.dependency.Dependency;
import com.craftaro.core.dependency.DependencyLoader;
import com.craftaro.core.dependency.Relocation;
import com.craftaro.core.locale.Locale;
import com.craftaro.core.utils.Metrics;
import com.craftaro.core.verification.CraftaroProductVerification;
import com.craftaro.core.verification.ProductVerificationStatus;
import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public abstract class SongodaPlugin extends JavaPlugin {
    protected Locale locale;
    protected Config config;
    protected Config databaseConfig;
    protected DataManager dataManager;
    protected long dataLoadDelay = 20L;

    private boolean licensePreventedPluginLoad = false;
    private boolean emergencyStop = false;

    static {
        /* NBT-API */
        MinecraftVersion.getLogger().setLevel(Level.WARNING);
        // Disable tips and logo for Jooq
        System.setProperty("org.jooq.no-tips", "true");
        System.setProperty("org.jooq.no-logo", "true");
    }

    protected abstract Set<Dependency> getDependencies();

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
        return this.config.getFileConfig();
    }

    public Config getCoreConfig() {
        return this.config;
    }

    @Override
    public void reloadConfig() {
        this.config.load();
        onConfigReload();
    }

    @Override
    public void saveConfig() {
        this.config.save();
    }

    @Override
    public final void onLoad() {

        try {
            //Load Core dependencies
            DependencyLoader.initParentClassLoader(getClass().getClassLoader());
            Set<Dependency> dependencies = getDependencies();
            //Use ; instead of . so maven plugin won't relocate it
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "org;apache;commons", "commons-text", "1.9"));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "org;apache;commons", "commons-lang3", "3.12.0"));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "net;kyori", "adventure-platform-bukkit", "4.1.1"));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "net;kyori", "adventure-api", "4.11.0"));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "com;zaxxer", "HikariCP", "5.1.0"));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "org;reactivestreams", "reactive-streams", "1.0.2", false));
            //dependencies.add(new Dependency("https://repo1.maven.org/maven2", "io;r2dbc", "r2dbc-spi", "1.0.0.RELEASE", false));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "org;jooq", "jooq", "3.14.16")); //3.19.1
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "org;mariadb;jdbc", "mariadb-java-client", "3.2.0"));
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "com;h2database", "h2", "1.4.200",
                    new Relocation("org;h2", "com;craftaro;third_party;org;h2")) //Custom relocation if the package names not match with the groupdId
            );
            dependencies.add(new Dependency("https://repo1.maven.org/maven2", "com;github;cryptomorin", "XSeries", "9.8.0",
                    new Relocation("com;cryptomorin;xseries", "com;craftaro;third_party;com;cryptomorin;xseries")) //Custom relocation if the package names not match with the groupdId
            );

            //Load plugin dependencies
            DependencyLoader.loadDependencies(dependencies);

            this.config = new Config(this);
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

        // Check plugin access, don't load the plugin if the user doesn't have access
        if (CraftaroProductVerification.getOwnProductVerificationStatus() != ProductVerificationStatus.VERIFIED) {
            console.sendMessage("\n" +
                    ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                    ChatColor.RED + "You do not have access to the " + getDescription().getName() + " plugin.\n" +
                    ChatColor.YELLOW + "Please purchase a license at https://craftaro.com/\n" +
                    ChatColor.YELLOW + "or set up your license\n" +
                    ChatColor.YELLOW + "And setup it up:\n" +
                    ChatColor.YELLOW + "Run the command " + ChatColor.GOLD + "/craftaro license" + ChatColor.YELLOW + " and follow the instructions\n" +
                    ChatColor.RED + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            this.licensePreventedPluginLoad = true;
            SongodaCore.registerPlugin(this, CraftaroProductVerification.getProductId(), (XMaterial) null);

            getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                String pluginName = getDescription().getName();
                String pluginUrl = "https://craftaro.com/marketplace/product/" + CraftaroProductVerification.getProductId();
                Bukkit.broadcastMessage(ChatColor.RED + pluginName + " has not been activated. Please download " + pluginName + " here: " + pluginUrl);
            }, 5 * 20, 60 * 20);
            return;
        }

        console.sendMessage(" "); // blank line to separate chatter
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sCraftaro <3!", ChatColor.GRAY, getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY, ChatColor.GREEN, "Enabling", ChatColor.GRAY));

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
        if (this.emergencyStop || this.licensePreventedPluginLoad) {
            return;
        }

        CommandSender console = Bukkit.getConsoleSender();

        console.sendMessage(" "); // blank line to separate chatter
        console.sendMessage(ChatColor.GREEN + "=============================");
        console.sendMessage(String.format("%s%s %s by %sCraftaro <3!", ChatColor.GRAY,
                getDescription().getName(), getDescription().getVersion(), ChatColor.DARK_PURPLE));
        console.sendMessage(String.format("%sAction: %s%s%s...", ChatColor.GRAY,
                ChatColor.RED, "Disabling", ChatColor.GRAY));

        onPluginDisable();
        try (Connection connection = this.dataManager.getDatabaseConnector().getConnection()) {
            connection.close();
            this.dataManager.getDatabaseConnector().closeConnection();
        } catch (Exception ignored) {
        }

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
                        SongodaCore.getVersion()
                ), th);

        emergencyStop();
    }

    //New database stuff
    public Config getDatabaseConfig() {
        File databaseFile = new File(getDataFolder(), "database.yml");
        if (!databaseFile.exists()) {
            saveResource("database.yml", false);
        }
        if (this.databaseConfig == null) {
            this.databaseConfig = new Config(databaseFile);
            this.databaseConfig.load();
        }
        return this.databaseConfig;
    }

    /**
     * Get the DataManager for this plugin.
     * Note: Make sure to call initDatabase() in onPluginEnable() before using this.
     *
     * @return DataManager for this plugin.
     */
    public DataManager getDataManager() {
        return this.dataManager;
    }

    /**
     * Initialize the DataManager for this plugin and convert from SQLite to H2 if needed.
     */
    protected void initDatabase() {
        initDatabase(Collections.emptyList());
    }

    protected void initDatabase(DataMigration... migrations) {
        initDatabase(Arrays.asList(migrations));
    }

    /**
     * Initialize the DataManager for this plugin and convert from SQLite to H2 if needed.
     *
     * @param migrations List of migrations to run.
     */
    protected void initDatabase(List<DataMigration> migrations) {
        File databaseFile = new File(getDataFolder(), getName().toLowerCase() + ".db");
        boolean legacy = databaseFile.exists();

        if (legacy) {
            getLogger().warning("SQLite detected, converting to H2...");
            this.dataManager = new DataManager(this, migrations, DatabaseType.SQLITE);
        } else {
            this.dataManager = new DataManager(this, migrations);
        }

        if (this.dataManager.getDatabaseConnector().isInitialized()) {
            //Check if the type is SQLite
            if (this.dataManager.getDatabaseConnector().getType() == DatabaseType.SQLITE) {
                //Let's convert it to H2
                try {
                    DataManager newDataManager = DataMigration.convert(this, DatabaseType.H2);
                    if (newDataManager != null && newDataManager.getDatabaseConnector().isInitialized()) {
                        //Set the new data manager
                        setDataManager(newDataManager);
                    }
                } catch (Exception ex) {
                    // Throwing for keeping backwards compatible – Not a fan of just logging a potential critical error here
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    /**
     * Set the DataManager for this plugin.
     * Used for converting from one database to another.
     */
    public void setDataManager(DataManager dataManager) {
        if (dataManager == null) throw new IllegalArgumentException("DataManager cannot be null!");
        if (this.dataManager == dataManager) return;

        // Make sure to shut down the old data manager.
        if (this.dataManager != null) {
            this.dataManager.shutdown();
        }
        this.dataManager = dataManager;
    }
}
