package com.github.disc99.orm;

import java.sql.ResultSet;

public interface ResultSetGetter {

    public StringGetter type(Class<?> clazz);

    public void setResultSet(ResultSet rs);

    @FunctionalInterface
    public interface StringGetter {
        public Object get(String key);
    }

}
