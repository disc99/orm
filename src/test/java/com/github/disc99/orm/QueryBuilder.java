package com.github.disc99.orm;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public enum QueryBuilder {
    INSTANCE;

    private enum Query {
        CREATE("CREATE TABLE %s(%s)"),
        INSERT("INSERT INTO %s VALUES (%s)"),
        SELECT("SELECT %s FROM %s"),
        UPDATE(""),
        DELETE(""),
        DROP("DROP TABLE %s"), ;
        String template;

        private Query(String template) {
            this.template = template;
        }
    }

    public <T> String create(TableEntity<T> table) {
        return String.format(Query.INSERT.template, table.getName(), createColumnDefinitions(table));
    }

    public <T> String insert(TableEntity<T> table) {
        return String.format(Query.INSERT.template, table.getName(), createQuestions(table.getColumnSize()));
    }

    private <T> String createColumnDefinitions(TableEntity<T> table) {
        List<String> definitions = new ArrayList<>();
        for (int i = 0; i < table.getColumnSize(); i++) {
            definitions.add(table.getColumnType(i) + " " + table.getColumnName(i));
        }
        return definitions.stream().collect(Collectors.joining(", "));
    }

    private String createQuestions(long num) {
        return LongStream.range(0, num).mapToObj(l -> "? ").collect(joining(","));
    }

}
