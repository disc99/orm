package com.github.disc99.orm.sql;

import static com.github.disc99.util.Throwables.uncheckCall;
import static java.util.Objects.requireNonNull;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ResultSetGetter {
    private final Map<Class<?>, StringGetter> mapping;
    {
        mapping = new HashMap<>();
        mapping.put(int.class, (columnName) -> uncheckCall(() -> this.rs.getInt(columnName)));
        mapping.put(Integer.class, (columnName) -> uncheckCall(() -> this.rs.getInt(columnName)));
        mapping.put(Long.class, (columnName) -> uncheckCall(() -> (Long) this.rs.getLong(columnName)));
        mapping.put(String.class, (columnName) -> uncheckCall(() -> this.rs.getString(columnName)));
    }
    private ResultSet rs;

    public ResultSetGetter(ResultSet rs) {
        requireNonNull(rs);
        this.rs = rs;
    }

    public StringGetter of(Class<?> clazz) {
        return mapping.get(clazz);
    }

    @FunctionalInterface
    public interface StringGetter {
        public Object get(String key);
    }
}
