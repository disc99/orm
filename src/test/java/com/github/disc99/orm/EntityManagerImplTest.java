package com.github.disc99.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.junit.Test;

public class EntityManagerImplTest {

    private static final String password = "";

    private static final String user = "sa";

    private static final String url = "jdbc:h2:file:~/test";

    private String driver = "org.h2.Driver";

    private final String createPerson = "CREATE TABLE PERSON(ID INT, ADDR VARCHAR(40))";
    private final String insertPerson = "INSERT INTO PERSON VALUES (?, ?)";
    private final String selectPerson = "SELECT ID, ADDR FROM PERSON ORDER BY ID";
    private static final String dropPersion = "DROP TABLE PERSON";

    void execute(Connection conn, String sql, Consumer<PreparedStatement> query) {

        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            query.accept(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Person p = new Person();
        p.setName("tom");

        EntityManager em = new EntityManagerImpl();
        em.persist(p);

        try (Connection conn = DriverManager.getConnection(url, user, password);) {
            Class.forName(driver);

            conn.setAutoCommit(false);

            execute(conn, createPerson, ps -> {
                try {
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            execute(conn, insertPerson, ps -> {
                try {
                    ps.setInt(1, 1956);
                    ps.setString(2, "Webster St.");
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            execute(conn, selectPerson, ps -> {
                try {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        System.out.println("ID=" + rs.getInt("ID") + " ADDR=" + rs.getString("ADDR"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            execute(conn, dropPersion, ps -> {
                try {
                    ps.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Entity
class Person {
    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}