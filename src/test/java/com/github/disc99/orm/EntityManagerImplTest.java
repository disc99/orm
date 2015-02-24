package com.github.disc99.orm;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.junit.Test;

public class EntityManagerImplTest {

    @Test
    public void test() {
        Person p = new Person();
        p.setName("tom");

        EntityManager em = new EntityManagerImpl();
        em.persist(p);

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