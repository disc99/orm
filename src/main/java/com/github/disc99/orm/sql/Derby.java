package com.github.disc99.orm.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;

public class Derby implements Database {

    private static final Map<Class<?>, String> typeMapping;
    static {
        typeMapping = new HashMap<>();
        typeMapping.put(Integer.class, "INTEGER");
        typeMapping.put(int.class, "INTEGER");
        typeMapping.put(Long.class, "BIGINT");
        typeMapping.put(String.class, "VARCHAR(32672)");
    }

    public static final String URL = "jdbc:h2:file:~/test";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    @Override
    public String getType(Field field) {
        // TODO IDENTITY and more...
        return typeMapping.get(field.getType());
    }

    @Override
    public DataSource getDataSource() {
        DataSource ds = new EmbeddedDataSource();
        ((EmbeddedDataSource) ds).setUser("");
        ((EmbeddedDataSource) ds).setPassword("");
        ((EmbeddedDataSource) ds).setDatabaseName("data/testdb");
        ((EmbeddedDataSource) ds).setCreateDatabase("create");
        return ds;
    }
}