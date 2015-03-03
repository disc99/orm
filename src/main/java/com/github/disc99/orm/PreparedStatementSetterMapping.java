package com.github.disc99.orm;

import java.util.HashMap;
import java.util.Map;

/**
 * PreparedStatement mapping.
 * 
 * <p>
 * map.get(Integer.class).set(ps, 1, 10) = ps.setInt(1, 10);
 * 
 * @author daisuke
 *
 */
public enum PreparedStatementSetterMapping {
    INSTANCE;
    private static final Map<Class<?>, PreparedStatementSetter> mapping;
    static {
        mapping = new HashMap<>();
        mapping.put(int.class, (ps, num, obj) -> ps.setInt(num, obj == null ? 0 : (int) obj));
        mapping.put(Integer.class, (ps, num, obj) -> ps.setInt(num, obj == null ? 0 : (int) obj));
        mapping.put(Long.class, (ps, num, obj) -> ps.setLong(num, obj == null ? 0 : (Long) obj));
        mapping.put(String.class, (ps, num, obj) -> ps.setString(num, (String) obj));
    }

    public PreparedStatementSetter getSetter(Class<?> clazz) {
        return mapping.get(clazz);
    }
}
