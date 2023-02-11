package com.songoda.core.database;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Future;

public interface DatabaseConnector {
    /**
     * Checks if the connection to the database has been created
     *
     * @return true if the connection is created, otherwise false
     */
    boolean isInitialized();

    /**
     * Closes all open connections to the database
     */
    void closeConnection();

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished
     *
     * @param callback The callback to execute once the connection is retrieved
     */
    void connect(ConnectionCallback callback);

    /**
     * Executes a callback with a DSLContext passed and automatically closes it when finished
     *
     * @param callback The callback to execute once the connection is retrieved
     */
    void connectDSL(DSLContextCallback callback);

    /**
     * Wraps a connection in a callback which will automagically handle catching sql errors
     */
    interface ConnectionCallback {
        void accept(Connection connection) throws SQLException;
    }

    /**
     * Wraps a connection in a callback which will automagically handle catching sql errors
     */
    interface DSLContextCallback {
        void accept(DSLContext context) throws SQLException;
    }

    /**
     * Gets a connection from the database
     * @return The connection
     */
    Connection getConnection() throws SQLException;

    /**
     * Gets the database type
     * @return The database type
     */
    DatabaseType getType();
}
