package com.github.disc99.orm;

import java.sql.PreparedStatement;

public interface PreparedStatementSetter {

    public IntSetter type(Class<?> clazz);

    public void setPreparedStatement(PreparedStatement ps);

    @FunctionalInterface
    public interface IntSetter {
        public void set(int num, Object obj);
    }
}
