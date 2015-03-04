package com.github.disc99.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.disc99.orm.H2;

public final class ConnectionHandler {

    private static final ThreadLocal<Connection> resource = new ThreadLocal<>();

    private ConnectionHandler() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = resource.get();
        if (conn == null) {
            conn = DriverManager.getConnection(H2.URL, H2.USER, H2.PASSWORD);
            resource.set(conn);
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        Connection conn = resource.get();
        resource.set(null);
        if (conn != null) {
            conn.close();
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
