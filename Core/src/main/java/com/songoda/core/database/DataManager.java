package com.songoda.core.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.plugin.Plugin;
import org.jooq.impl.DSL;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DataManager {
    protected final DatabaseConnector databaseConnector;
    protected final Plugin plugin;
    protected final DatabaseType type;
    private final Map<String, Integer> autoIncrementCache = new HashMap<>();

    protected final ExecutorService asyncPool = new ThreadPoolExecutor(1, 5, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat(getClass().getSimpleName()+"-Database-Async-%d").build());

    @Deprecated
    private static final Map<String, LinkedList<Runnable>> queues = new HashMap<>();

    public DataManager(DatabaseConnector databaseConnector, Plugin plugin) {
        this.databaseConnector = databaseConnector;
        this.plugin = plugin;
        this.type = databaseConnector.getType();
    }

    /**
     * @return the prefix to be used by all table names
     */
    public String getTablePrefix() {
        return this.plugin.getDescription().getName().toLowerCase() + '_';
    }

    /**
     * @return The next auto increment value for the given table
     */
    public synchronized int getNextId(String table) {
        if (!this.autoIncrementCache.containsKey(table)) {
            databaseConnector.connectDSL(dsl -> {
                try (ResultSet rs = dsl.select(DSL.max(DSL.field("id"))).from(table).fetchResultSet()) {
                    if (rs.next()) {
                        this.autoIncrementCache.put(table, rs.getInt(1));
                    } else {
                        this.autoIncrementCache.put(table, 1);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        int id = this.autoIncrementCache.get(table);
        id++;
        this.autoIncrementCache.put(table, id);
        return id;
    }

    /**
     * Saves the data to the database
     */
    public void save(Data data) {
        asyncPool.execute(() -> {
            databaseConnector.connectDSL(context -> {
                context.insertInto(DSL.table(getTablePrefix() + data.getTableName()))
                        .set(data.serialize())
                        .onDuplicateKeyUpdate()
                        .set(data.serialize())
                        .execute();
            });
        });
    }

    /**
     * Deletes the data from the database
     */
    public void delete(Data data) {
        asyncPool.execute(() -> {
            databaseConnector.connectDSL(context -> {
                context.delete(DSL.table(getTablePrefix() + data.getTableName()))
                        .where(DSL.field("id").eq(data.getId()))
                        .execute();
            });
        });
    }

    /**
     * Loads the data from the database
     * @param id The id of the data
     * @return The loaded data
     */
    @SuppressWarnings("unchecked")
    public <T extends Data> T load(int id, Class<?> clazz, String table) {
        try {
            AtomicReference<Data> data = new AtomicReference<>((Data) clazz.getConstructor().newInstance());
            databaseConnector.connectDSL(context -> {
                try {
                    data.set((Data) clazz.getDeclaredConstructor().newInstance());
                    data.get().deserialize(Objects.requireNonNull(context.select()
                                    .from(DSL.table(getTablePrefix() + table))
                                    .where(DSL.field("id").eq(id))
                                    .fetchOne())
                            .intoMap());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            return (T) data.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Loads the data from the database
     * @param uuid The uuid of the data
     * @return The loaded data
     */
    @SuppressWarnings("unchecked")
    public <T extends Data> T load(UUID uuid, Class<?> clazz, String table) {
        try {
            AtomicReference<Data> data = new AtomicReference<>((Data) clazz.getConstructor().newInstance());
            databaseConnector.connectDSL(context -> {
                try {
                    data.set((Data) clazz.getDeclaredConstructor().newInstance());
                    data.get().deserialize(Objects.requireNonNull(context.select()
                                    .from(DSL.table(getTablePrefix() + table))
                                    .where(DSL.field("uuid").eq(uuid.toString()))
                                    .fetchOne())
                            .intoMap());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            return (T) data.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void shutdownTaskQueue() {
        this.asyncPool.shutdown();
    }

    public List<Runnable> forceShutdownTaskQueue() {
        return this.asyncPool.shutdownNow();
    }

    public boolean isTaskQueueTerminated() {
        return this.asyncPool.isTerminated();
    }

    public long getTaskQueueSize() {
        if (this.asyncPool instanceof ThreadPoolExecutor) {
            return ((ThreadPoolExecutor) this.asyncPool).getTaskCount();
        }

        return -1;
    }

    /**
     * @see ExecutorService#awaitTermination(long, TimeUnit)
     */
    public boolean waitForShutdown(long timeout, TimeUnit unit) throws InterruptedException {
        return this.asyncPool.awaitTermination(timeout, unit);
    }

    public String getSyntax(String string, DatabaseType type) {
        if (this.type == type) {
            return string;
        }
        return "";
    }
}
