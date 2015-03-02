package com.github.disc99.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementSetter {
    public void set(PreparedStatement ps, int num, Object obj) throws SQLException;
}