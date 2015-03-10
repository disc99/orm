package com.github.disc99.orm.derby;

import static com.github.disc99.util.Throwables.uncheckCall;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.github.disc99.orm.ResultSetGetter;

public class DerbyResultSetGetter implements ResultSetGetter {
    private final Map<Class<?>, StringGetter> mapping;
    {
        mapping = new HashMap<>();
        mapping.put(int.class, (columnName) -> uncheckCall(() -> this.rs.getInt(columnName)));
        mapping.put(Integer.class, (columnName) -> uncheckCall(() -> this.rs.getInt(columnName)));
        mapping.put(Long.class, (columnName) -> uncheckCall(() -> (Long) this.rs.getLong(columnName)));
        mapping.put(String.class, (columnName) -> uncheckCall(() -> this.rs.getString(columnName)));
    }
    private ResultSet rs;

    @Override
    public void setResultSet(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public StringGetter type(Class<?> clazz) {
        return mapping.get(clazz);
    }
}
