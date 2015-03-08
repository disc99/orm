package com.github.disc99.orm.sql;

import static com.github.disc99.util.Throwables.uncheck;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.transaction.SystemException;

import com.github.disc99.orm.DataAccessException;
import com.github.disc99.transaction.JdbcTransactionManager;

public class QueryExecuter {

    private static final Logger logger = Logger.getLogger(QueryExecuter.class.getName());

    public <T> void create(Class<T> clazz) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sqlTable = QueryBuilder.INSTANCE.createTable(table);
        execute(sqlTable, uncheck(ps -> ps.executeUpdate()));
        String sqlSeq = QueryBuilder.INSTANCE.createSequence(table);
        execute(sqlSeq, uncheck(ps -> ps.executeUpdate()));
    }

    public <T> void drop(Class<T> clazz) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sql = QueryBuilder.INSTANCE.drop(table);
        execute(sql, uncheck(ps -> ps.executeUpdate()));
    }

    public <T> void insert(T entity) {
        EntityTable<T> table = new EntityTable<>(entity.getClass());
        String sql = QueryBuilder.INSTANCE.insert(table);
        execute(sql, uncheck(ps -> {
            setPreparedStatement(entity, table.getNotIdColumns(), new PreparedStatementSetter(ps));
            ps.executeUpdate();
        }));
    }

    public <T> void update(T entity) {
        EntityTable<T> table = new EntityTable<>(entity.getClass());
        String sql = QueryBuilder.INSTANCE.update(table);
        execute(sql, uncheck(ps -> {
            List<EntityColumn> columns = table.getNotIdColumns();
            columns.add(table.getIdColumn());
            setPreparedStatement(entity, columns, new PreparedStatementSetter(ps));
            ps.executeUpdate();
        }));
    }

    private <T> void setPreparedStatement(T entity, List<EntityColumn> columns, PreparedStatementSetter psSetter) {
        IntStream.range(0, columns.size())
                .forEach(i -> {
                    EntityColumn column = columns.get(i);
                    psSetter.of(column.getClassType()).set(i + 1, column.getValue(entity));
                });
    }

    public <T> Optional<List<T>> selectAll(Class<T> clazz) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sql = QueryBuilder.INSTANCE.selectAll(table);

        try (PreparedStatement ps = new JdbcTransactionManager().getConnection().prepareStatement(sql);) {
            logger.info(sql);
            ResultSet rs = ps.executeQuery();
            List<EntityColumn> columns = table.getColumns();

            List<T> result = new ArrayList<>();
            while (rs.next()) {
                T instance = (T) createInstance(clazz, columns, rs);
                result.add(instance);
            }
            return result.isEmpty() ? Optional.empty() : Optional.of(result);
        } catch (SystemException | SQLException | InstantiationException | IllegalAccessException e) {
            throw new DataAccessException(e);
        }
    }

    public <T> Optional<T> selectId(T entity) {
        EntityTable<T> table = new EntityTable<>(entity.getClass());
        String sql = QueryBuilder.INSTANCE.selectId(table);

        try (PreparedStatement ps = new JdbcTransactionManager().getConnection().prepareStatement(sql);) {
            logger.info(sql);
            EntityColumn column = table.getIdColumn();
            new PreparedStatementSetter(ps).of(column.getClassType()).set(1, column.getValue(entity));

            ResultSet rs = ps.executeQuery();
            List<EntityColumn> columns = table.getColumns();

            while (rs.next()) {
                @SuppressWarnings("unchecked")
                T instance = (T) createInstance(entity.getClass(), columns, rs);
                return Optional.of(instance);
            }
            return Optional.empty();
        } catch (SystemException | SQLException | InstantiationException | IllegalAccessException e) {
            throw new DataAccessException(e);
        }

    }

    private <T> T createInstance(Class<T> clazz, List<EntityColumn> columns, ResultSet rs)
            throws InstantiationException, IllegalAccessException {
        ResultSetGetter rsGetter = new ResultSetGetter(rs);
        T instance = clazz.newInstance();
        IntStream.range(0, columns.size())
                .forEach(i -> {
                    EntityColumn column = columns.get(i);
                    Object value = rsGetter.of(column.getClassType()).get(column.getName());
                    column.setValue(instance, value);
                });
        return instance;
    }

    private void execute(String sql, Consumer<PreparedStatement> func) {
        logger.info(sql);
        try (PreparedStatement ps = new JdbcTransactionManager().getConnection().prepareStatement(sql);) {
            func.accept(ps);
        } catch (SystemException | SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
