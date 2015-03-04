package com.github.disc99.orm;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

public class QueryBuilderTest {

    @Test
    public void testUpdate() {
        String actual = QueryBuilder.INSTANCE.update(new TableEntity<>(Car.class));
        assertThat(actual, is("UPDATE Car SET engine = ?, tire = ? WHERE ID = ?"));
    }

    @Test
    public void testSelect() {
        String actual = QueryBuilder.INSTANCE.selectId(new TableEntity<>(Car.class));
        assertThat(actual, is("SELECT id, engine, tire FROM Car WHERE ID = ?"));
    }

}

@Entity
class Car {
    @Id
    private Long id;
    private String engine;
    private int tire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getTire() {
        return tire;
    }

    public void setTire(int tire) {
        this.tire = tire;
    }
}