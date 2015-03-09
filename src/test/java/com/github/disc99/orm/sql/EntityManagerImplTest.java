package com.github.disc99.orm.sql;

import java.util.Locale;

import org.junit.Test;

import com.github.disc99.transaction.JdbcTransactionManager;

public class EntityManagerImplTest {

    @Test
    public void test() throws Exception {

        Locale.setDefault(Locale.US);

        PersonTest p = new PersonTest();
        p.setSimpleName("tom");

        QueryExecuter executer = new QueryExecuter();

        JdbcTransactionManager txManager = new JdbcTransactionManager();

        txManager.begin();

        executer.create(PersonTest.class);

        executer.insert(p);
        executer.insert(p);
        executer.insert(p);

        p.setId(2L);
        p.setSimpleName("john");
        executer.update(p);

        System.out.println(executer.selectId(p));
        System.out.println(executer.selectAll(PersonTest.class));

        executer.drop(PersonTest.class);

        txManager.rollback();

    }
}
