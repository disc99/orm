package com.github.disc99.transaction;

import java.sql.Connection;

public final class ConnectionHandler {
    private ConnectionHandler() {
    }

    public static Connection getConnection() {
        // TODO Auto-generated method stub
        return getInstance().conn;
    }

    private static ThreadLocal holder = new ThreadLocal() {
    };
    // この変数をクラス変数のように扱いたい
    private Connection conn = null;

    // 外部からインスタンス化すらさせたくない場合はgetInstance()をprivateに
    private static ConnectionHandler getInstance() {
        return holder.get();
    }

    public static void setConnection(Connection conn) {
        getInstance().conn = conn;
    }

    private static class ConnectionHolder {

    }
}
