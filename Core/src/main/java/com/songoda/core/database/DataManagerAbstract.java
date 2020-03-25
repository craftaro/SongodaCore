package com.songoda.core.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DataManagerAbstract {

    protected final DatabaseConnector databaseConnector;
    protected final Plugin plugin;

    private static Map<String, ScheduledExecutorService> threads = new HashMap<>();

    public DataManagerAbstract(DatabaseConnector databaseConnector, Plugin plugin) {
        this.databaseConnector = databaseConnector;
        this.plugin = plugin;
    }

    /**
     * @return the prefix to be used by all table names
     */
    public String getTablePrefix() {
        return this.plugin.getDescription().getName().toLowerCase() + '_';
    }

    protected int lastInsertedId(Connection connection) {
        String query;
        if (this.databaseConnector instanceof SQLiteConnector) {
            query = "SELECT last_insert_rowid()";
        } else {
            query = "SELECT LAST_INSERT_ID()";
        }

        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Queue a task to be run asynchronously. <br>
     * TODO: This needs to be separated from BukkitScheduler
     * 
     * @param runnable task to run
     */
    public void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    /**
     * Queue a task to be run synchronously.
     *
     * @param runnable task to run on the next server tick
     */
    public void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(this.plugin, runnable);
    }

    /**
     * Queue a task to be run synchronously on a new thread.
     *
     * @param runnable  task to run on the next server tick
     * @param threadKey the thread key to run on.
     */
    public void sync(Runnable runnable, String threadKey) {
        threads.computeIfAbsent(threadKey.toUpperCase(),
                t -> Executors.newSingleThreadScheduledExecutor()).execute(runnable);
    }
}
