package com.github.disc99.orm;

import static com.github.disc99.util.Throwables.uncheck;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.EntityManager;

import org.junit.Test;

public class EntityManagerImplTest {

    private static final String URL = "jdbc:h2:file:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static final String CREATE_PERSON = "CREATE TABLE PERSON(ID INT, NAME VARCHAR)";
    private static final String INSERT_PERSON = "INSERT INTO PERSON VALUES (?, ?)";
    private static final String SELECT_PERSON = "SELECT ID, NAME FROM PERSON ORDER BY ID";
    private static final String DROP_PERSION = "DROP TABLE PERSON";

    @Test
    public void test() {
        Person p = new Person();
        p.setName("tom");

        EntityManager em = new EntityManagerImpl();
        em.persist(p);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);) {

            conn.setAutoCommit(false);

            execute(conn, CREATE_PERSON, uncheck(ps -> ps.executeUpdate()));

            execute(conn, INSERT_PERSON, uncheck(ps -> {
                ps.setInt(1, 1956);
                ps.setString(2, "Webster St.");
                ps.executeUpdate();
            }));

            execute(conn, SELECT_PERSON, uncheck(ps -> {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.println("ID=" + rs.getInt("ID") + " NAME=" + rs.getString("NAME"));
                }
            }));

            execute(conn, DROP_PERSION, uncheck(ps -> ps.executeUpdate()));

            conn.rollback();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void t2() {
        Person p = new Person();
        p.setId(10L);
        p.setName("tom");

        QueryExecuter executer = new QueryExecuter();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);) {

            conn.setAutoCommit(false);

            executer.create(Person.class);

            executer.insert(p);

            execute(conn, SELECT_PERSON, uncheck(ps -> {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.println("ID=" + rs.getInt("ID") + " NAME=" + rs.getString("NAME"));
                }
            }));

            executer.drop(Person.class);
            // execute(conn, DROP_PERSION, uncheck(ps -> ps.executeUpdate()));

            conn.rollback();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void execute(Connection conn, String sql, Consumer<PreparedStatement> query) {
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            query.accept(ps);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Test
    public void testName() throws Exception {
        A a = new A();
        Field f = a.getClass().getDeclaredField("num");

        System.out.println(f.getType().getClass());
    }
}

class A {
    private String str;
    private int num;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}

class PreparedStatements {
    /**
     * Set params for {@link PreparedStatement}.
     * 
     * @param ps
     * @param fields
     */
    public void set(PreparedStatement ps, List<Field> fields) {
        // fields.stream()
        // .forEach(field -> {
        // setParam(ps, field.getType(), field.);
        // });

    }

    public static PreparedStatement createInsert(Connection conn, String sql, TableEntity<?> table) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        // table.getColumns().stream()
        // .forEach(colum -> {
        // set(ps, colum.getType(), num, getter);
        // });

        return ps;
    }

    // private static void set(PreparedStatement ps, Type type, int num,
    // Supplier<?> getter) {
    // switch (type) {
    // case IDENTITY:
    // ps.setInt(num, (int) getter.get());
    // break;
    // case INT:
    // ps.setInt(num, (int) getter.get());
    // break;
    // case LONG:
    // ps.setLong(num, (long) getter.get());
    // break;
    // case VARCHAR:
    // ps.setString(num, (String) getter.get());
    // break;
    // }
    // }
}
