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
import java.util.concurrent.TimeUnit;

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

    /**
     * Deprecated because it is often times not accurate to its use case.
     */
    @Deprecated
    protected int lastInsertedId(Connection connection) {
        return lastInsertedId(connection, null);
    }

    protected int lastInsertedId(Connection connection, String table) {
        String select = "SELECT * FROM " + this.getTablePrefix() + table + " ORDER BY id DESC LIMIT 1";
        String query;
        if (this.databaseConnector instanceof SQLiteConnector) {
            query = table == null ? "SELECT last_insert_rowid()" : select;
        } else {
            query = table == null ? "SELECT LAST_INSERT_ID()" : select;
        }

        int id = -1;
        try (Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            result.next();
            id = result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
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

    /**
     * Terminate thread once all tasks have been completed.
     *
     * @param threadKey the thread key to terminate.
     */
    public static void terminateThread(String threadKey) {
        ScheduledExecutorService service = threads.get(threadKey);
        if (service != null) {
            threads.remove(threadKey);
            try {
                service.awaitTermination(0, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Terminate all active threads.
     */
    public static void terminateAllThreads() {
        for (ScheduledExecutorService service : threads.values()) {
            try {
                service.awaitTermination(0, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
