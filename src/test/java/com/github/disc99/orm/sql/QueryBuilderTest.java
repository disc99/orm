package com.github.disc99.orm.sql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Test;

import com.github.disc99.orm.EntityTable;
import com.github.disc99.orm.derby.DerbyQueryBuilder;

public class QueryBuilderTest {

    @Test
    public void testCreateSequence() {
        String actual = DerbyQueryBuilder.INSTANCE.createSequence(new EntityTable<>(Car.class));
        assertThat(actual, is("CREATE SEQUENCE CAR_SEQ AS BIGINT START WITH 1"));
    }

    @Test
    public void testUpdate() {
        String actual = DerbyQueryBuilder.INSTANCE.update(new EntityTable<>(Car.class));
        assertThat(actual, is("UPDATE CAR SET ENGINE = ?, TIRE_NUM = ? WHERE ID = ?"));
    }

    @Test
    public void testSelect() {
        String actual = DerbyQueryBuilder.INSTANCE.selectId(new EntityTable<>(Car.class));
        assertThat(actual, is("SELECT ID, ENGINE, TIRE_NUM FROM CAR WHERE ID = ?"));
    }

}

@Entity
class Car {
    @Id
    private Long id;
    private String engine;
    private int tireNum;

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

    public int getTireNum() {
        return tireNum;
    }

    public void setTireNum(int tireNum) {
        this.tireNum = tireNum;
    }

}