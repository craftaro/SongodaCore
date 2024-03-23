package com.craftaro.core.data;

import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.data.connector.H2Connector;
import com.craftaro.core.data.connector.MariaDBConnector;
import com.craftaro.core.data.connector.MySQLConnector;
import com.craftaro.core.data.connector.SQLiteConnector;
import com.craftaro.core.thread.MonitoredThread;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DatabaseManager {

    private static DatabaseManager INSTANCE;

    private final MonitoredThread thread;
    private DatabaseConnector connector;

    private final Config databaseConfig;

    public DatabaseManager(SongodaPlugin plugin) {
        INSTANCE = this;
        thread = new MonitoredThread(plugin.getName().toLowerCase() + "-sql-thread", 15, TimeUnit.SECONDS);
        databaseConfig = new Config(plugin, "database.yml");

        if (!new File(plugin.getDataFolder(), "database.yml").exists())
            plugin.saveResource("database.yml", false);
        databaseConfig.load();

        String type = databaseConfig.getString("type", "H2").toUpperCase();
        String host = databaseConfig.getString("host", "localhost");
        int port = databaseConfig.getInt("port", 3306);
        String database = databaseConfig.getString("database", "plugin");
        String username = databaseConfig.getString("username", "root");
        String password = databaseConfig.getString("password", "");
        int poolSize = databaseConfig.getInt("poolSize", 10);
        boolean useSSL = databaseConfig.getBoolean("useSSL", false);
        boolean autoReconnect = databaseConfig.getBoolean("autoReconnect", true);

        String dataPath = plugin.getDataFolder().getPath().replaceAll("\\\\", "/") + "/";

        String dbFile = "./" + dataPath + databaseConfig.getString("file", "data");

        switch (DatabaseType.valueOf(type)) {
            case H2:
                connector = new H2Connector(dbFile, poolSize);
                break;
            case MYSQL:
                connector = new MySQLConnector(host, port, database, username, password, useSSL, autoReconnect, poolSize);
                break;
            case SQLITE:
                connector = new SQLiteConnector(dbFile, poolSize);
                break;
            case MARIADB:
                connector = new MariaDBConnector(host, port, database, username, password, useSSL, autoReconnect, poolSize);
                break;
            default:
                throw new IllegalArgumentException("Invalid database type: " + type);
        }
    }

    public void execute(Runnable runnable) {
        execute(runnable, false);
    }

    public void execute(Runnable runnable, boolean nonDisruptable) {
        thread.execute(runnable, nonDisruptable);
    }

    public void load(String name, Runnable load) {
        load.run();
        System.out.println("Loaded " + name);
    }

    public DatabaseConnector getDatabaseConnector() {
        return connector;
    }

    public Config getConfig() {
        return databaseConfig;
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }
}
