package com.craftaro.core.database;

import com.craftaro.core.SongodaCore;
import com.craftaro.core.SongodaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DataMigration {
    private final int revision;

    public DataMigration(int revision) {
        this.revision = revision;
    }

    public abstract void migrate(DatabaseConnector connector, String tablePrefix) throws SQLException;

    /**
     * @return the revision number of this migration
     */
    public int getRevision() {
        return this.revision;
    }

    /**
     * @param plugin The plugin to convert data for
     * @param toType The new database type
     * @return The new data manager instance
     */
    public static DataManager convert(SongodaPlugin plugin, DatabaseType toType) throws Exception {
        DataManager from = plugin.getDataManager();
        if (from.getDatabaseConnector().getType() == toType) {
            plugin.getLogger().severe("Cannot convert to the same database type!");
            return null;
        }
        DataManager to = new DataManager(plugin, Collections.emptyList(), toType);
        if (!to.getDatabaseConnector().isInitialized()) {
            plugin.getLogger().severe("Invalid database configuration for " + toType.name() +"! Please check your "+plugin.getName()+"/database.yml file.");
            return null;
        }

        plugin.getLogger().info("Converting data from " + from.getDatabaseConnector().getType().name() + " to " + toType.name() + "...");
        DatabaseConnector fromConnector = from.getDatabaseConnector();
        DatabaseConnector toConnector = to.getDatabaseConnector();

        Connection fromConnection = fromConnector.getConnection();
        Connection toConnection = toConnector.getConnection();

        try {

            // Export schema
            DatabaseMetaData meta = fromConnection.getMetaData();
            ResultSet tables = meta.getTables(null, null, null, new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                Statement stmt = fromConnection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

                ResultSetMetaData metaRs = rs.getMetaData();
                int columnCount = metaRs.getColumnCount();

                StringBuilder createTableQuery = new StringBuilder();
                createTableQuery.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaRs.getColumnName(i);
                    String columnType = metaRs.getColumnTypeName(i);
                    int columnSize = metaRs.getColumnDisplaySize(i);

                    createTableQuery.append(columnName).append(" ").append(columnType).append("(").append(columnSize).append(")");

                    if (i < columnCount) {
                        createTableQuery.append(", ");
                    }
                }

                createTableQuery.append(")");

                toConnection.createStatement().execute(createTableQuery.toString());

                while (rs.next()) {
                    StringBuilder insertQuery = new StringBuilder();
                    insertQuery.append("INSERT INTO ").append(tableName).append(" VALUES (");

                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);

                        if (value == null) {
                            insertQuery.append("NULL");
                        } else if (value instanceof String || value instanceof Timestamp) {
                            insertQuery.append("'").append(value).append("'");
                        } else {
                            insertQuery.append(value);
                        }

                        if (i < columnCount) {
                            insertQuery.append(", ");
                        }
                    }

                    insertQuery.append(")");
                    toConnection.createStatement().execute(insertQuery.toString());
                }
            }

            toConnection.commit();
            plugin.getLogger().info("Successfully migrated data from " + from.getDatabaseConnector().getType() + " to " + to.getDatabaseConnector().getType());
        } catch (Exception e) {
            if (toConnection != null) {
                try {
                    toConnection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    plugin.getLogger().severe("Failed to rollback data for the new database");
                }
            }
            e.printStackTrace();
            plugin.getLogger().severe("Failed to migrate data from " + from.getDatabaseConnector().getType() + " to " + to.getDatabaseConnector().getType());
            return null;
        }
        fromConnector.closeConnection();
        //Get rid of the old SQLite database file if it exists and create a backup
        File databaseFile = new File(plugin.getDataFolder(), plugin.getName().toLowerCase()+".db");
        if (databaseFile.exists()) {

            //rename it to .old
            databaseFile.renameTo(new File(plugin.getDataFolder(), plugin.getName().toLowerCase() + ".db.old"));
            plugin.getLogger().info("Old database file renamed to " + plugin.getName().toLowerCase() + ".db.old");
        }
        return to;
    }

    private String getTableColumns(Connection connection, String tableName) {
        StringBuilder columns = new StringBuilder();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getColumns(null, null, tableName, null);

            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");

                columns.append(columnName).append(" ").append(columnType).append(", ");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        columns.setLength(columns.length() - 2);
        return columns.toString();
    }
}
