package com.songoda.core.database;

import com.songoda.core.SongodaCore;
import com.songoda.core.SongodaPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.plugin.Plugin;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

public class MariaDBConnector implements DatabaseConnector {

    private final SongodaPlugin plugin;
    private HikariDataSource hikari;
    private boolean initializedSuccessfully;

    public MariaDBConnector(SongodaPlugin plugin, YamlDocument databaseConfig) {
        this.plugin = plugin;

        Section section = databaseConfig.getSection("connection-settings");

        String hostname = section.getString("hostname");
        int port = section.getInt("port");
        String database = section.getString("databaseName");
        String username = section.getString("username");
        String password = section.getString("password");
        boolean useSSL = section.getBoolean("useSsl");
        int poolSize = section.getInt("poolSize");

        try {
            Class.forName("org.mariadb.jdbc.Driver");
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
    public void connectDSL(DSLContextCallback callback) {
        try (Connection connection = getConnection()){
            callback.accept(DSL.using(connection, SQLDialect.MARIADB));
        } catch (Exception ex) {
            this.plugin.getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
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
