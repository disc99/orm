package com.github.disc99.orm.sql;

import static com.github.disc99.util.Throwables.uncheckRun;
import static java.util.Objects.requireNonNull;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * PreparedStatement setter.
 * 
 * <p>
 * ex) ps.set(1,10);<br>
 * {@code
 * PreparedStatementSetter pss = new PreparedStatementSetter(rs);
 * pss.of(Interger.class).set(1 ,10);
 * }
 * 
 * @author daisuke
 *
 */
public class PreparedStatementSetter {
    private Map<Class<?>, IntSetter> mapping;
    {
        mapping = new HashMap<>();
        mapping.put(int.class, (num, obj) -> uncheckRun(() -> this.ps.setInt(num, obj == null ? 0 : (int) obj)));
        mapping.put(Integer.class, (num, obj) -> uncheckRun(() -> this.ps.setInt(num, obj == null ? 0 : (int) obj)));
        mapping.put(Long.class, (num, obj) -> uncheckRun(() -> this.ps.setLong(num, obj == null ? 0 : (Long) obj)));
        mapping.put(String.class, (num, obj) -> uncheckRun(() -> this.ps.setString(num, (String) obj)));
    }
    private PreparedStatement ps;

    public PreparedStatementSetter(PreparedStatement ps) {
        requireNonNull(ps);
        this.ps = ps;
    }

    public IntSetter of(Class<?> clazz) {
        return mapping.get(clazz);
    }

    @FunctionalInterface
    public interface IntSetter {
        public void set(int num, Object obj);
    }
}
