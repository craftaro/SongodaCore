package com.songoda.core.database;

import org.jooq.SQLDialect;

public enum DatabaseType {

    MARIADB,
    MYSQL,
    SQLITE;

    public SQLDialect getDialect() {
        switch (this) {
            case MARIADB:
                return SQLDialect.MARIADB;
            case MYSQL:
                return SQLDialect.MYSQL;
            case SQLITE:
                return SQLDialect.SQLITE;
            default:
                return SQLDialect.DEFAULT;
        }
    }
}
