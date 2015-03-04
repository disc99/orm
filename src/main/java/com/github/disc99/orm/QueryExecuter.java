package com.github.disc99.orm;

import static com.github.disc99.util.Throwables.uncheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.transaction.SystemException;

import com.github.disc99.transaction.JdbcTransactionManager;

public class QueryExecuter {
    private static final Logger logger = Logger.getLogger(QueryExecuter.class.getName());

    public <T> void create(Class<T> clazz) {
        TableEntity<T> table = new TableEntity<>(clazz);
        String sql = QueryBuilder.INSTANCE.create(table);
        logger.info(sql);
        execute(sql, uncheck(ps -> ps.executeUpdate()));
    }

    public <T> void drop(Class<T> clazz) {
        TableEntity<T> table = new TableEntity<>(clazz);
        String sql = QueryBuilder.INSTANCE.drop(table);
        logger.info(sql);
        execute(sql, uncheck(ps -> ps.executeUpdate()));
    }

    public <T> void insert(T entity) {

        TableEntity<T> table = new TableEntity<>(entity.getClass());
        String sql = QueryBuilder.INSTANCE.insert(table);
        logger.info(sql);
        execute(sql, uncheck(ps -> {
            for (int i = 0; i < table.getColumnSize(); i++) {
                // Set PreparedStatement parameter
                PreparedStatementSetterMapping.INSTANCE.getSetter(table.getColumnClass(i))
                        .set(ps, i + 1, table.invokeGetter(entity, i));
            }
            ps.executeUpdate();
        }));
    }

    public <T> void update(T entity) {
        TableEntity<T> table = new TableEntity<>(entity.getClass());
        String sql = QueryBuilder.INSTANCE.update(table);
        logger.info(sql);
        // TODO impl
        execute(sql, uncheck(ps -> {
            for (int i = 0; i < table.getColumnSize(); i++) {
                // Set PreparedStatement parameter
                PreparedStatementSetterMapping.INSTANCE.getSetter(table.getColumnClass(i))
                        .set(ps, i + 1, table.invokeGetter(entity, i));
            }
            ps.executeUpdate();
        }));
    }

    public <T> List<T> selectAll(Class<T> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T selectOne(T entity) {
        // TODO Auto-generated method stub
        return null;
    }

    public void execute(String sql, Consumer<PreparedStatement> func) {
        Connection conn;
        try {
            conn = new JdbcTransactionManager().getConnection();
        } catch (SystemException e) {
            throw new DataAccessException(e);
        }
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            func.accept(ps);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
