package com.github.disc99.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Locale;
import java.util.function.Consumer;

import org.junit.Test;

import com.github.disc99.orm.sql.QueryExecuter;
import com.github.disc99.transaction.JdbcTransactionManager;

public class EntityManagerImplTest {

    @Test
    public void t2() throws Exception {

        Locale.setDefault(Locale.US);

        PersonTest p = new PersonTest();
        p.setId(10L);
        p.setSimpleName("tom");

        QueryExecuter executer = new QueryExecuter();

        JdbcTransactionManager txManager = new JdbcTransactionManager();

        txManager.begin();

        executer.create(PersonTest.class);

        executer.insert(p);
        p.setId(11L);
        executer.insert(p);
        // executer.update(p);

        System.out.println(executer.selectId(p));
        System.out.println(executer.selectAll(PersonTest.class));

        executer.drop(PersonTest.class);

        txManager.rollback();

    }

    void execute(Connection conn, String sql, Consumer<PreparedStatement> query) {
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            query.accept(ps);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
