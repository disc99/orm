package com.github.disc99.orm;

import java.util.Locale;

import org.junit.Test;

import com.github.disc99.orm.PersistenceConfig;
import com.github.disc99.orm.QueryExecuter;
import com.github.disc99.orm.derby.Derby;
import com.github.disc99.transaction.JdbcTransactionManager;

public class EntityManagerImplTest {

    @Test
    public void test() throws Exception {

        init();

        PersonInfo p = new PersonInfo();
        p.setFullName("tom");

        JdbcTransactionManager txManager = new JdbcTransactionManager();

        txManager.begin();

        QueryExecuter.INSTANCE.create(PersonInfo.class);

        QueryExecuter.INSTANCE.insert(p);
        QueryExecuter.INSTANCE.insert(p);
        QueryExecuter.INSTANCE.insert(p);

        p.setId(2L);
        p.setFullName("john");
        QueryExecuter.INSTANCE.update(p);

        System.out.println(QueryExecuter.INSTANCE.selectId(p.getClass(), p.getId()));
        System.out.println(QueryExecuter.INSTANCE.selectAll(PersonInfo.class));

        QueryExecuter.INSTANCE.drop(PersonInfo.class);

        txManager.rollback();

    }

    private void init() {
        Locale.setDefault(Locale.US);
        PersistenceConfig.INSTANCE.setDb(new Derby());
        PersistenceConfig.INSTANCE.setUser("sa");
        PersistenceConfig.INSTANCE.setPassword("");
    }
}
