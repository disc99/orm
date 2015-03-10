package com.github.disc99.orm;

import java.sql.ResultSet;

public final class ResultSetGetterFactory {

    public static ResultSetGetter create(ResultSet rs) {
        ResultSetGetter rsGetter = PersistenceConfig.INSTANCE.getDb().getResultSetGetter();
        rsGetter.setResultSet(rs);
        return rsGetter;
    }
}
