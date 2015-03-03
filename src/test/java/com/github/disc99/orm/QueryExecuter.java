package com.github.disc99.orm;

import static com.github.disc99.util.Throwables.uncheck;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public class QueryExecuter {

    private static final String URL = "jdbc:h2:file:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    <T> void create(Class<T> clazz) {
        TableEntity<T> table = new TableEntity<>(clazz);
        String sql = QueryBuilder.INSTANCE.create(table);
        System.out.println(sql);
        execute(sql, uncheck(ps -> ps.executeUpdate()));
    }

    <T> void drop(Class<T> clazz) {
        TableEntity<T> table = new TableEntity<>(clazz);
        String sql = QueryBuilder.INSTANCE.drop(table);
        System.out.println(sql);
        execute(sql, uncheck(ps -> ps.executeUpdate()));
    }

    <T> void insert(T entity) {

        TableEntity<T> table = new TableEntity<>(entity.getClass());
        String sql = QueryBuilder.INSTANCE.insert(table);

        execute(sql, uncheck(ps -> {
            for (int i = 0; i < table.getColumnSize(); i++) {
                // Set PreparedStatement parameter
                PreparedStatementSetterMapping.INSTANCE.getSetter(table.getColumnClass(i))
                        .set(ps, i + 1, table.invokeGetter(entity, i));
            }
            ps.executeUpdate();
        }));
    }

    public void execute(String sql, Consumer<PreparedStatement> func) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql);) {
            func.accept(ps);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
