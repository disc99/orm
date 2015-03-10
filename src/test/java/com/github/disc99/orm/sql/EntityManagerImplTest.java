package com.github.disc99.orm.sql;

import java.util.Locale;

import org.junit.Test;

import com.github.disc99.orm.PersistenceConfig;
import com.github.disc99.transaction.JdbcTransactionManager;

public class EntityManagerImplTest {

    @Test
    public void test() throws Exception {

        init();

        PersonTest p = new PersonTest();
        p.setSimpleName("tom");

        JdbcTransactionManager txManager = new JdbcTransactionManager();

        txManager.begin();

        QueryExecuter.INSTANCE.create(PersonTest.class);

        QueryExecuter.INSTANCE.insert(p);
        QueryExecuter.INSTANCE.insert(p);
        QueryExecuter.INSTANCE.insert(p);

        p.setId(2L);
        p.setSimpleName("john");
        QueryExecuter.INSTANCE.update(p);

        System.out.println(QueryExecuter.INSTANCE.selectId(p.getClass(), p.getId()));
        System.out.println(QueryExecuter.INSTANCE.selectAll(PersonTest.class));

        QueryExecuter.INSTANCE.drop(PersonTest.class);

        txManager.rollback();

    }

    private void init() {
        Locale.setDefault(Locale.US);
        PersistenceConfig.INSTANCE.setDb(new Derby());
        PersistenceConfig.INSTANCE.setUser("sa");
        PersistenceConfig.INSTANCE.setPassword("");
    }
}
