package com.github.disc99.orm;

import static com.github.disc99.util.Throwables.uncheckCall;

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

import com.github.disc99.orm.derby.DerbyQueryBuilder;
import com.github.disc99.transaction.JdbcTransactionManager;

public enum QueryExecuter {
    INSTANCE;

    private static final Logger logger = Logger.getLogger(QueryExecuter.class.getName());

    public <T> void create(Class<T> clazz) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sqlTable = DerbyQueryBuilder.INSTANCE.createTable(table);
        execute(sqlTable, ps -> uncheckCall(() -> ps.executeUpdate()));
        String sqlSeq = DerbyQueryBuilder.INSTANCE.createSequence(table);
        execute(sqlSeq, ps -> uncheckCall(() -> ps.executeUpdate()));
    }

    public <T> void drop(Class<T> clazz) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sql = DerbyQueryBuilder.INSTANCE.drop(table);
        execute(sql, ps -> uncheckCall(() -> ps.executeUpdate()));
    }

    public <T> void insert(T entity) {
        EntityTable<T> table = new EntityTable<>(entity.getClass());
        String sql = DerbyQueryBuilder.INSTANCE.insert(table);
        execute(sql, ps -> {
            setPreparedStatement(entity, table.getNotIdColumns(), PreparedStatementSetterFactory.create(ps));
            uncheckCall(() -> ps.executeUpdate());
        });
    }

    public <T> void update(T entity) {
        EntityTable<T> table = new EntityTable<>(entity.getClass());
        String sql = DerbyQueryBuilder.INSTANCE.update(table);
        execute(sql, ps -> {
            List<EntityColumn> columns = table.getNotIdColumns();
            columns.add(table.getIdColumn());
            setPreparedStatement(entity, columns, PreparedStatementSetterFactory.create(ps));
            uncheckCall(() -> ps.executeUpdate());
        });
    }

    private <T> void setPreparedStatement(T entity, List<EntityColumn> columns, PreparedStatementSetter psSetter) {
        IntStream.range(0, columns.size())
                .forEach(i -> {
                    EntityColumn column = columns.get(i);
                    System.out.println(column);
                    psSetter.type(column.getClassType()).set(i + 1, column.getValue(entity));
                });
    }

    public <T> Optional<List<T>> selectAll(Class<T> clazz) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sql = DerbyQueryBuilder.INSTANCE.selectAll(table);

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

    public <T> Optional<T> selectId(Class<T> clazz, Object primaryKey) {
        EntityTable<T> table = new EntityTable<>(clazz);
        String sql = DerbyQueryBuilder.INSTANCE.selectId(table);

        try (PreparedStatement ps = new JdbcTransactionManager().getConnection().prepareStatement(sql);) {
            logger.info(sql);
            EntityColumn column = table.getIdColumn();
            PreparedStatementSetterFactory.create(ps).type(column.getClassType()).set(1, primaryKey);

            ResultSet rs = ps.executeQuery();
            List<EntityColumn> columns = table.getColumns();

            while (rs.next()) {
                T instance = (T) createInstance(clazz, columns, rs);
                return Optional.of(instance);
            }
            return Optional.empty();
        } catch (SystemException | SQLException | InstantiationException | IllegalAccessException e) {
            throw new DataAccessException(e);
        }

    }

    public void delete(Class<PersonInfo> class1, int i) {
        // TODO Auto-generated method stub

    }

    private <T> T createInstance(Class<T> clazz, List<EntityColumn> columns, ResultSet rs)
            throws InstantiationException, IllegalAccessException {
        ResultSetGetter rsGetter = ResultSetGetterFactory.create(rs);
        T instance = clazz.newInstance();
        IntStream.range(0, columns.size())
                .forEach(i -> {
                    EntityColumn column = columns.get(i);
                    Object value = rsGetter.type(column.getClassType()).get(column.getName());
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
