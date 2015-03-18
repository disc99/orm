package com.github.disc99.orm.derby;

import static com.github.disc99.util.Throwables.uncheckRun;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import com.github.disc99.orm.PreparedStatementSetter;

/**
 * {@link PreparedStatement} setter.
 * 
 * <p>
 * ex) ps.setInt(1,10);<br>
 * {@code
 * PreparedStatementSetter pss = new PreparedStatementSetter(rs);
 * pss.of(Interger.class).set(1 ,10);
 * }
 * 
 * @author daisuke
 *
 */
public class DerbyPreparedStatementSetter implements PreparedStatementSetter {
    private Map<Class<?>, IntSetter> mapping;
    {
        mapping = new HashMap<>();
        mapping.put(int.class, (num, obj) -> uncheckRun(() -> this.ps.setInt(num, obj == null ? 0 : (int) obj)));
        mapping.put(Integer.class, (num, obj) -> uncheckRun(() -> this.ps.setInt(num, obj == null ? 0 : (int) obj)));
        mapping.put(Long.class, (num, obj) -> uncheckRun(() -> this.ps.setLong(num, obj == null ? 0 : (Long) obj)));
        mapping.put(String.class, (num, obj) -> uncheckRun(() -> this.ps.setString(num, (String) obj)));
    }
    private PreparedStatement ps;

    @Override
    public void setPreparedStatement(PreparedStatement ps) {
        this.ps = ps;
    }

    @Override
    public IntSetter type(Class<?> clazz) {
        IntSetter setter = mapping.get(clazz);
        return setter;
    }
}
