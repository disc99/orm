package com.github.disc99.orm;

import java.lang.reflect.Field;

import javax.sql.DataSource;

public interface Database {
    public String getType(Field field);

    public DataSource getDataSource();
}
