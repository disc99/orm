package com.github.disc99.orm;

import org.junit.Test;

public class QueryBuilderTest {

    @Test
    public void test() {
        String sql = QueryBuilder.INSTANCE.update(new TableEntity<>(Person.class));
        System.out.println(sql);
    }

}
