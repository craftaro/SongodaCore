package com.craftaro.core.database;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.CraftaroPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public static DataManager convert(CraftaroPlugin plugin, DatabaseType toType) {
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

        DatabaseConnector fromConnector = from.getDatabaseConnector();
        DatabaseConnector toConnector = to.getDatabaseConnector();

        Connection fromConnection;
        Connection toConnection = null;

        try {
            fromConnection = fromConnector.getConnection();
            toConnection = toConnector.getConnection();
            toConnection.setAutoCommit(false);

            // Retrieve the list of tables from the old database
            List<String> tableNames = new ArrayList<>();
            try (ResultSet rs = fromConnection.getMetaData().getTables(null, null, null, new String[] {"TABLE"})) {
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    tableNames.add(tableName);
                }
            }

            // Transfer the data from the old database to the new database
            for (String tableName : tableNames) {
                try (
                        PreparedStatement fromStmt = fromConnection.prepareStatement("SELECT * FROM " + tableName);
                        ResultSet rs = fromStmt.executeQuery();
                        PreparedStatement toStmt = toConnection.prepareStatement("INSERT INTO " + tableName + " VALUES (" + String.join(",", Collections.nCopies(rs.getMetaData().getColumnCount(), "?")) + ")")
                ) {
                    while (rs.next()) {
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            toStmt.setObject(i, rs.getObject(i));
                        }
                        toStmt.executeUpdate();
                    }
                }
            }

            toConnection.commit();
        } catch (Exception e) {
            if (toConnection != null)
                try {
                    toConnection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    CraftaroCore.getInstance().getLogger().severe("Failed to rollback data for the new database");
                }
            e.printStackTrace();
            CraftaroCore.getInstance().getLogger().severe("Failed to migrate data from " + from.getDatabaseConnector().getType() + " to " + to.getDatabaseConnector().getType());
            return null;
        } finally {
            CraftaroCore.getInstance().getLogger().info("Successfully migrated data from " + from.getDatabaseConnector().getType() + " to " + to.getDatabaseConnector().getType());
        }
        return to;
    }
}
