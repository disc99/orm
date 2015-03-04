package com.github.disc99.orm;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.LongStream.range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public enum QueryBuilder {
    INSTANCE;

    private static final Collector<CharSequence, ?, String> joinComma = joining(", ");

    private enum Query {
        CREATE("CREATE TABLE %s(%s)"),
        INSERT("INSERT INTO %s VALUES (%s)"),
        SELECT("SELECT %s FROM %s"),
        SELECT_ID("SELECT %s FROM %s WHERE ID = ?"),
        UPDATE("UPDATE %s SET %s WHERE ID = ?"),
        DELETE("DELETE FROM %s WHERE ID = ?"),
        DROP("DROP TABLE %s"), ;
        String template;

        private Query(String template) {
            this.template = template;
        }
    }

    public <T> String create(TableEntity<T> table) {
        return format(Query.CREATE.template, table.getName(), createColumnDefinitions(table));
    }

    public <T> String drop(TableEntity<T> table) {
        return format(Query.DROP.template, table.getName());
    }

    public <T> String insert(TableEntity<T> table) {
        return format(Query.INSERT.template, table.getName(), createQuestions(table.getColumnSize()));
    }

    public <T> String update(TableEntity<T> table) {
        return format(Query.UPDATE.template, table.getName(), createSetter(table.getNotIdColumnNames()));
    }

    public <T> String delete(TableEntity<T> table) {
        return format(Query.DELETE.template, table.getName());
    }

    public <T> String selectId(TableEntity<T> table) {
        return format(Query.SELECT_ID.template, createColumns(table.getColumnNames()), table.getName());
    }

    private <T> String createColumnDefinitions(TableEntity<T> table) {
        List<String> definitions = new ArrayList<>();
        for (int i = 0; i < table.getColumnSize(); i++) {
            definitions.add(table.getColumnName(i) + " " + table.getColumnType(i));
        }
        return definitions.stream().collect(joinComma);
    }

    private String createQuestions(long num) {
        return range(0, num).mapToObj(l -> "?").collect(joinComma);
    }

    private String createSetter(List<String> columns) {
        return columns.stream().collect(joining(" = ?, ", "", " = ?"));
    }

    private Object createColumns(List<String> columns) {
        return columns.stream().collect(joinComma);
    }
}
