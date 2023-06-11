package com.craftaro.core.database;

import com.craftaro.core.SongodaCore;
import org.bukkit.plugin.Plugin;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Deprecated
public class SQLiteConnector implements DatabaseConnector {
    private final Plugin plugin;
    private final String connectionString;
    private Connection connection;

    public SQLiteConnector(Plugin plugin) {
        this.plugin = plugin;
        this.connectionString = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + plugin.getDescription().getName().toLowerCase() + ".db";

        try {
            Class.forName("org.sqlite.JDBC"); // This is required to put here for Spigot 1.10 and below to force class load
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isInitialized() {
        return true; // Always available
    }

    @Override
    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred closing the SQLite database connection: " + ex.getMessage());
        }
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try {
            callback.accept(getConnection());
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing an SQLite query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public OptionalResult connectOptional(ConnectionOptionalCallback callback) {
        try (Connection connection = getConnection()) {
            return callback.accept(connection);
        } catch (Exception ex) {
            SongodaCore.getInstance().getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
        return OptionalResult.empty();
    }

    @Override
    public void connectDSL(DSLContextCallback callback) {
        try (Connection connection = getConnection()){
            callback.accept(DSL.using(connection, SQLDialect.SQLITE));
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public OptionalResult connectDSLOptional(DSLContextOptionalCallback callback) {
        try (Connection connection = getConnection()) {
            return callback.accept(DSL.using(connection, SQLDialect.SQLITE));
        } catch (Exception ex) {
            SongodaCore.getInstance().getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
        return OptionalResult.empty();
    }

    @Override
    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(2)) {
                try {
                    this.connection = DriverManager.getConnection(this.connectionString);
                } catch (SQLException ex) {
                    this.plugin.getLogger().severe("An error occurred retrieving the SQLite database connection: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.connection;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.SQLITE;
    }
}
