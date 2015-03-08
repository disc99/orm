package com.github.disc99.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.disc99.orm.sql.Database;
import com.github.disc99.orm.sql.Derby;

public final class ConnectionHandler {

    private static final ThreadLocal<Connection> resource = new ThreadLocal<>();

    private static Database db = new Derby();

    private ConnectionHandler() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = resource.get();
        if (conn == null) {
            conn = db.getDataSource().getConnection();
            resource.set(conn);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        Connection conn = resource.get();
        if (conn != null) {
            if (!conn.getAutoCommit()) {
                return;
            }
            conn.close();
            resource.set(null);
        }
    }

    public static void begin() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        getConnection().commit();
    }

    public static void rollback() throws SQLException {
        getConnection().rollback();
    }
}
