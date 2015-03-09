package com.github.disc99.orm;

public enum DatabaseFactory {
    INSTANCE;

    public Database getDatabase(PersistenceConfig config) {
        return config.getDb();
    }
}
