package com.github.disc99.orm.sql;

import static com.github.disc99.util.Throwables.uncheckRun;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * PreparedStatement mapping.
 * 
 * <p>
 * PSSetter pss = new PreparedStatementSetter(ps)
 * pss.get(Integer.class).set(1,19) map.get(Integer.class).set(ps, 1, 10) =
 * ps.setInt(1, 10);
 * 
 * @author daisuke
 *
 */
public enum SetterMapping {
    INSTANCE;
    private static final Map<Class<?>, IntSetter<PreparedStatement>> psMapping;
    static {
        psMapping = new HashMap<>();
        psMapping.put(int.class, (ps, num, obj) -> uncheckRun(() -> ps.setInt(num, obj == null ? 0 : (int) obj)));
        psMapping.put(Integer.class, (ps, num, obj) -> uncheckRun(() -> ps.setInt(num, obj == null ? 0 : (int) obj)));
        psMapping.put(Long.class, (ps, num, obj) -> uncheckRun(() -> ps.setLong(num, obj == null ? 0 : (Long) obj)));
        psMapping.put(String.class, (ps, num, obj) -> uncheckRun(() -> ps.setString(num, (String) obj)));
    }

    public IntSetter<PreparedStatement> getPsSetter(Class<?> clazz) {
        return psMapping.get(clazz);
    }

    @FunctionalInterface
    public interface IntSetter<T> {
        public void set(T ps, int num, Object obj);
    }
}
