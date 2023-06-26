package com.craftaro.core.database.connectors;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.CraftaroPlugin;
import com.craftaro.core.configuration.Config;
import com.craftaro.core.database.DatabaseConnector;
import com.craftaro.core.database.DatabaseType;
import com.craftaro.core.database.OptionalResult;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.simpleyaml.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;

public class MariaDBConnector implements DatabaseConnector {

    private final CraftaroPlugin plugin;
    private HikariDataSource hikari;
    private boolean initializedSuccessfully;

    public MariaDBConnector(CraftaroPlugin plugin, Config databaseConfig) {
        this.plugin = plugin;

        ConfigurationSection section = databaseConfig.getConfigurationSection("Connection Settings");

        String hostname = section.getString("Hostname");
        int port = section.getInt("Port");
        String database = section.getString("Database");
        String username = section.getString("Username");
        String password = section.getString("Password");
        boolean useSSL = section.getBoolean("Use SSL");
        int poolSize = section.getInt("Pool Size");

        try {
            Class.forName("com.craftaro.core.third_party.org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);

        try {
            this.hikari = new HikariDataSource(config);
            this.initializedSuccessfully = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.initializedSuccessfully = false;
        }
    }

    @Override
    public boolean isInitialized() {
        return this.initializedSuccessfully;
    }

    @Override
    public void closeConnection() {
        this.hikari.close();
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try (Connection connection = this.hikari.getConnection()) {
            callback.accept(connection);
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public OptionalResult connectOptional(ConnectionOptionalCallback callback) {
        try (Connection connection = getConnection()) {
            return callback.accept(connection);
        } catch (Exception ex) {
            CraftaroCore.getInstance().getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
        return OptionalResult.empty();
    }

    @Override
    public void connectDSL(DSLContextCallback callback) {
        try (Connection connection = getConnection()){
            callback.accept(DSL.using(connection, SQLDialect.MARIADB));
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public OptionalResult connectDSLOptional(DSLContextOptionalCallback callback) {
        try (Connection connection = getConnection()) {
            return callback.accept(DSL.using(connection, SQLDialect.MARIADB));
        } catch (Exception ex) {
            CraftaroCore.getInstance().getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
        return OptionalResult.empty();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.hikari.getConnection();
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.MARIADB;
    }
}
