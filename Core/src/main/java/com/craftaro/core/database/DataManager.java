package com.craftaro.core.database;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.craftaro.core.SongodaPlugin;
import com.craftaro.core.configuration.Config;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DataManager {
    protected final SongodaPlugin plugin;
    protected final Config databaseConfig;
    private final List<DataMigration> migrations;
    protected DatabaseConnector databaseConnector;
    protected DatabaseType type;
    private final Map<String, AtomicInteger> autoIncrementCache = new HashMap<>();

    protected final ExecutorService asyncPool = new ThreadPoolExecutor(1, 5, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat(getClass().getSimpleName()+"-Database-Async-%d").build());

    @Deprecated
    private static final Map<String, LinkedList<Runnable>> queues = new HashMap<>();

    public DataManager(SongodaPlugin plugin, List<DataMigration> migrations) {
        this.plugin = plugin;
        this.migrations = migrations;
        this.databaseConfig = plugin.getDatabaseConfig();
        load(null);
    }

    public DataManager(SongodaPlugin plugin, List<DataMigration> migrations, DatabaseType forcedType) {
        this.plugin = plugin;
        this.migrations = migrations;
        this.databaseConfig = plugin.getDatabaseConfig();
        load(forcedType);
    }

    private void load(DatabaseType forcedType) {
        try {
            String databaseType = databaseConfig.getString("Connection Settings.Type").toUpperCase();
            if (forcedType != null) {
                databaseType = forcedType.name();
            }
            switch (databaseType) {
                case "MYSQL": {
                    this.databaseConnector = new MySQLConnector(plugin, databaseConfig);
                    break;
                }
                case "MARIADB": {
                    this.databaseConnector = new MariaDBConnector(plugin, databaseConfig);
                    break;
                }
                case "SQLITE": {
                    //Lets check if we have the sqlite file in the plugin folder
                    File databaseFile = new File(plugin.getDataFolder(), plugin.getName().toLowerCase()+".db");
                    if (!databaseFile.exists()) {
                        //Lets start SQLite and it will be converted to H2
                        this.databaseConnector = new SQLiteConnector(plugin);
                    } else {
                        //No need for conversion, lets use H2 instead
                        this.databaseConnector = new H2Connector(plugin);
                    }
                    break;
                }
                default: {
                    //H2
                    this.databaseConnector = new H2Connector(plugin);
                    break;
                }
            }
            this.type = databaseConnector.getType();
            this.plugin.getLogger().info("Data handler connected using " + databaseConnector.getType().name() + ".");
        } catch (Exception ex) {
            this.plugin.getLogger().severe("Fatal error trying to connect to database. Please make sure all your connection settings are correct and try again. Plugin has been disabled.");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this.plugin);
        }

        runMigrations();
    }

    /**
     * @return the database connector
     */
    public DatabaseConnector getDatabaseConnector() {
        return databaseConnector;
    }

    /**
     * @return the prefix to be used by all table names
     */
    public String getTablePrefix() {
        return this.plugin.getDescription().getName().toLowerCase() + '_';
    }

    /**
     * Runs any needed data migrations
     */
    public void runMigrations() {
        try (Connection connection = this.databaseConnector.getConnection()) {
            int currentMigration = -1;
            boolean migrationsExist;

            DatabaseMetaData meta = connection.getMetaData();
            ResultSet res = meta.getTables(null, null, this.getMigrationsTableName(), new String[] {"TABLE"});
            migrationsExist = res.next();

            if (!migrationsExist) {
                // No migration table exists, create one
                String createTable = "CREATE TABLE " + this.getMigrationsTableName() + " (migration_version INT NOT NULL)";
                try (PreparedStatement statement = connection.prepareStatement(createTable)) {
                    statement.execute();
                }

                // Insert primary row into migration table
                String insertRow = "INSERT INTO " + this.getMigrationsTableName() + " VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
                    statement.setInt(1, -1);
                    statement.execute();
                }
            } else {
                // Grab the current migration version
                String selectVersion = "SELECT migration_version FROM " + this.getMigrationsTableName();
                try (PreparedStatement statement = connection.prepareStatement(selectVersion)) {
                    ResultSet result = statement.executeQuery();
                    result.next();
                    currentMigration = result.getInt("migration_version");
                }
            }

            // Grab required migrations
            int finalCurrentMigration = currentMigration;
            List<DataMigration> requiredMigrations = this.migrations.stream()
                    .filter(x -> x.getRevision() > finalCurrentMigration)
                    .sorted(Comparator.comparingInt(DataMigration::getRevision))
                    .collect(Collectors.toList());

            // Nothing to migrate, abort
            if (requiredMigrations.isEmpty()) {
                return;
            }

            // Migrate the data
            for (DataMigration dataMigration : requiredMigrations) {
                dataMigration.migrate(databaseConnector, getTablePrefix());
            }

            // Set the new current migration to be the highest migrated to
            currentMigration = requiredMigrations.stream()
                    .map(DataMigration::getRevision)
                    .max(Integer::compareTo)
                    .orElse(-1);

            String updateVersion = "UPDATE " + this.getMigrationsTableName() + " SET migration_version = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateVersion)) {
                statement.setInt(1, currentMigration);
                statement.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @return the name of the migrations table
     */
    private String getMigrationsTableName() {
        return getTablePrefix() + "migrations";
    }

    /**
     * @return The next auto increment value for the given table
     */
    public synchronized int getNextId(String table) {
        String prefixedTable = getTablePrefix() + table;
        if (!this.autoIncrementCache.containsKey(prefixedTable)) {
            databaseConnector.connectDSL(context -> {
//                context.select(DSL.max(DSL.field("id"))).from(prefixedTable).fetchOptional().ifPresentOrElse(record -> {
//                    if (record.get(0, Integer.class) == null) {
//                        this.autoIncrementCache.put(prefixedTable, new AtomicInteger(1));
//                        return;
//                    }
//                    this.autoIncrementCache.put(prefixedTable, new AtomicInteger(record.get(0, Integer.class)));
//                }, () -> this.autoIncrementCache.put(prefixedTable, new AtomicInteger(1)));
//
                //recreate upper method using java 8 syntax
                Optional<Integer> max = context.select(DSL.max(DSL.field("id"))).from(prefixedTable).fetchOptional().map(record -> record.get(0, Integer.class));
                this.autoIncrementCache.put(prefixedTable, new AtomicInteger(max.orElse(1)));
            });
        }
        return this.autoIncrementCache.get(prefixedTable).incrementAndGet();
    }

    /**
     * Saves the data to the database
     */
    public void save(Data data) {
        asyncPool.execute(() -> {
            databaseConnector.connectDSL(context -> {
                context.insertInto(DSL.table(getTablePrefix() + data.getTableName()))
                        .set(data.serialize())
                        .onConflict(DSL.field("id")).doUpdate()
                        .set(data.serialize())
                        .where(data.getId() != -1 ? DSL.field("id").eq(data.getId()) : DSL.field("uuid").eq(data.getUniqueId().toString()))
                        .execute();
            });
        });
    }

    /**
     * Saves the data to the database synchronously
     */
    public void saveSync(Data data) {
        databaseConnector.connectDSL(context -> {
            context.insertInto(DSL.table(getTablePrefix() + data.getTableName()))
                    .set(data.serialize())
                    .onConflict(DSL.field("id")).doUpdate()
                    .set(data.serialize())
                    .where(data.getId() != -1 ? DSL.field("id").eq(data.getId()) : DSL.field("uuid").eq(data.getUniqueId().toString()))
                    .execute();
        });
    }

    /**
     * Saves the data in batch to the database
     */
    public void saveBatch(Collection<Data> dataBatch) {
        asyncPool.execute(() -> {
            databaseConnector.connectDSL(context -> {
                List<Query> queries = new ArrayList<>();
                for (Data data : dataBatch) {
                    queries.add(context.insertInto(DSL.table(getTablePrefix() + data.getTableName()))
                            .set(data.serialize())
                            .onConflict(DSL.field("id")).doUpdate()
                            .set(data.serialize())
                            .where(data.getId() != -1 ? DSL.field("id").eq(data.getId()) : DSL.field("uuid").eq(data.getUniqueId().toString())));
                }

                context.batch(queries).execute();
            });
        });
    }

    /**
     * Saves the data in batch to the database
     */
    public void saveBatchSync(Collection<Data> dataBatch) {
        databaseConnector.connectDSL(context -> {
            List<Query> queries = new ArrayList<>();
            for (Data data : dataBatch) {
                queries.add(context.insertInto(DSL.table(getTablePrefix() + data.getTableName()))
                        .set(data.serialize())
                        .onConflict(DSL.field("id")).doUpdate()
                        .set(data.serialize())
                        .where(data.getId() != -1 ? DSL.field("id").eq(data.getId()) : DSL.field("uuid").eq(data.getUniqueId().toString())));
            }

            context.batch(queries).execute();
        });
    }

    /**
     * Deletes the data from the database
     */
    public void delete(Data data) {
        asyncPool.execute(() -> {
            databaseConnector.connectDSL(context -> {
                context.delete(DSL.table(getTablePrefix() + data.getTableName()))
                        .where(data.getId() != -1 ? DSL.field("id").eq(data.getId()) : DSL.field("uuid").eq(data.getUniqueId().toString()))
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
            AtomicBoolean found = new AtomicBoolean(false);
            databaseConnector.connectDSL(context -> {
                try {
                    data.set((Data) clazz.getDeclaredConstructor().newInstance());
                    data.get().deserialize(Objects.requireNonNull(context.select()
                                    .from(DSL.table(getTablePrefix() + table))
                                    .where(DSL.field("uuid").eq(uuid.toString()))
                                    .fetchOne())
                            .intoMap());
                    found.set(true);
                } catch (NullPointerException ignored) {

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            if (found.get()) {
                return (T) data.get();
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Loads the data in batch from the database
     * @return The loaded data
     */
    @SuppressWarnings("unchecked")
    public <T extends Data> List<T> loadBatch(Class<?> clazz, String table) {
        try {
            List<Data> dataList = Collections.synchronizedList(new ArrayList<>());
            databaseConnector.connectDSL(context -> {
                try {
                    for (@NotNull Record record : Objects.requireNonNull(context.select()
                            .from(DSL.table(getTablePrefix() + table))
                            .fetchArray())) {
                        Data data = (Data)clazz.getDeclaredConstructor().newInstance();
                        data.deserialize(record.intoMap());
                        dataList.add(data);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            return (List<T>) dataList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Close the database and shutdown the async pool
     */
    public void shutdown() {
        asyncPool.shutdown();
        databaseConnector.closeConnection();
    }

    /**
     * Force shutdown the async pool and close the database
     * @return Tasks that didn't finish in the async pool
     */
    public List<Runnable> shutdownNow() {
        databaseConnector.closeConnection();
        return asyncPool.shutdownNow();
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
