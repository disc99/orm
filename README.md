# orm
orm is minimal ORMapper.

## How to use

```java
@Entity
public class PersonInfo {
    @Id
    private Long id;
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }
}

public class QueryExecuterTest {

    @Test
    public void test() throws Exception {

        PersistenceConfig.INSTANCE.setDb(new Derby());
        PersistenceConfig.INSTANCE.setUser("sa");
        PersistenceConfig.INSTANCE.setPassword("");

		JdbcTransactionManager txManager = new JdbcTransactionManager();
		txManager.begin();

		// CREATE TABLE PERSON_INFO(ID BIGINT, FULL_NAME VARCHAR(32672))
		// CREATE SEQUENCE %s_SEQ AS PERSON_INFO START WITH 1
		QueryExecuter.INSTANCE.create(PersonInfo.class);

		PersonInfo pi = new PersonInfo();
		pi.setFullName("Tom Broun");

		// INSERT INTO PERSON_INFO VALUES (NEXT VALUE FOR PERSON_INFO_SEQ, 'Tom Broun')
		QueryExecuter.INSTANCE.insert(pi);

		pi.setId(1L);
		pi.setFullName("Tom Brown");
		// UPDATE PERSON_INFO SET FULL_NAME = 'Tom Brown' WHERE ID = 1
		QueryExecuter.INSTANCE.update(pi);

		// SELECT ID, FULL_NAME FROM PERSON_INFO WHERE ID = 1
		Optional<PersonInfo> piOne = QueryExecuter.INSTANCE.selectId(PersonInfo.class, 1L);

		// SELECT ID, FULL_NAME FROM PERSON_INFO
		Optional<List<PersonInfo>> piAll = QueryExecuter.INSTANCE.selectAll(PersonInfo.class);

		// DELETE FROM PERSON_INFO WHERE ID = 1
		QueryExecuter.INSTANCE.delete(PersonInfo.class, 1L);

		// DROP TABLE PERSON_INFO
		// DROP SEQUENCE PERSON_INFO_SEQ RESTRICT
		QueryExecuter.INSTANCE.drop(PersonInfo.class);

		txManager.rollback();
	}
}
```


## Requirements

* JDK 8 +
