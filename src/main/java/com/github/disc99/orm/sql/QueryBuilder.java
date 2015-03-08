package com.github.disc99.orm.sql;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.LongStream.range;

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

    public <T> String create(EntityTable<T> table) {
        return format(Query.CREATE.template, table.getName(), createColumnDefinitions(table));
    }

    public <T> String drop(EntityTable<T> table) {
        return format(Query.DROP.template, table.getName());
    }

    public <T> String insert(EntityTable<T> table) {
        return format(Query.INSERT.template, table.getName(), createQuestions(table.getColumns().size()));
    }

    public <T> String update(EntityTable<T> table) {
        return format(Query.UPDATE.template, table.getName(), createSetter(table.getNotIdColumnNames()));
    }

    public <T> String delete(EntityTable<T> table) {
        return format(Query.DELETE.template, table.getName());
    }

    public <T> String selectId(EntityTable<T> table) {
        return format(Query.SELECT_ID.template, createColumns(table.getColumnNames()), table.getName());
    }

    public <T> String selectAll(EntityTable<T> table) {
        return format(Query.SELECT.template, createColumns(table.getColumnNames()), table.getName());
    }

    private <T> String createColumnDefinitions(EntityTable<T> table) {
        return table.getColumns().stream()
                .map(c -> c.getName() + " " + c.getDefinetion())
                .collect(joinComma);
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
