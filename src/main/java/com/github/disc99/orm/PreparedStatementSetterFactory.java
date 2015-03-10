package com.github.disc99.orm;

import java.sql.PreparedStatement;

public final class PreparedStatementSetterFactory {
    public static PreparedStatementSetter create(PreparedStatement ps) {
        PreparedStatementSetter psSetter = PersistenceConfig.INSTANCE.getDb().getPreparedStatementSetter();
        psSetter.setPreparedStatement(ps);
        return psSetter;
    }
}
