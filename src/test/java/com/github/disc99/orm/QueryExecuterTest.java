package com.github.disc99.orm;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.junit.Test;

import com.github.disc99.orm.derby.Derby;

public class QueryExecuterTest {

    @Test
    public void test() {

        Locale.setDefault(Locale.US);
        PersistenceConfig.INSTANCE.setDb(new Derby());
        PersistenceConfig.INSTANCE.setUser("sa");
        PersistenceConfig.INSTANCE.setPassword("");

        // CREATE TABLE PERSON_INFO(ID BIGINT, FULL_NAME VARCHAR(32672))
        QueryExecuter.INSTANCE.create(PersonInfo.class);

        PersonInfo pi1 = new PersonInfo();
        pi1.setFullName("Tom Broun");

        // INSERT INTO PERSON_INFO VALUES (NEXT VALUE FOR PERSON_INFO_SEQ, ?)
        QueryExecuter.INSTANCE.insert(pi1);

        pi1.setFullName("Tom Brown");
        // UPDATE PERSON_INFO SET FULL_NAME = ? WHERE ID = ?
        QueryExecuter.INSTANCE.update(pi1);

        // SELECT ID, FULL_NAME FROM PERSON_INFO WHERE ID = ?
        QueryExecuter.INSTANCE.selectId(PersonInfo.class, 1);

        // SELECT ID, FULL_NAME FROM PERSON_INFO
        Optional<List<PersonInfo>> pi3 = QueryExecuter.INSTANCE.selectAll(PersonInfo.class);

        // DELETE FROM PERSON_INFO WHERE ID = ?
        QueryExecuter.INSTANCE.delete(PersonInfo.class, 1);

        // DROP TABLE PERSON_INFO
        QueryExecuter.INSTANCE.drop(PersonInfo.class);
    }

}
