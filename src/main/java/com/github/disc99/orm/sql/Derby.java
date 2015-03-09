package com.github.disc99.orm.sql;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;

import com.github.disc99.orm.Database;
import com.github.disc99.orm.PersistenceConfig;

public class Derby implements Database {

    private static final Map<Class<?>, String> typeMapping;
    static {
        typeMapping = new HashMap<>();
        typeMapping.put(Integer.class, "INTEGER");
        typeMapping.put(int.class, "INTEGER");
        typeMapping.put(Long.class, "BIGINT");
        typeMapping.put(String.class, "VARCHAR(32672)");
    }

    @Override
    public String getType(Field field) {
        // TODO IDENTITY and more...
        return typeMapping.get(field.getType());
    }

    @Override
    public DataSource getDataSource() {
        DataSource ds = new EmbeddedDataSource();
        ((EmbeddedDataSource) ds).setUser(PersistenceConfig.INSTANCE.getUser());
        ((EmbeddedDataSource) ds).setPassword(PersistenceConfig.INSTANCE.getPassword());
        ((EmbeddedDataSource) ds).setDatabaseName("db/testdb");
        ((EmbeddedDataSource) ds).setCreateDatabase("create");
        return ds;
    }
}