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
        CREATE_TABLE("CREATE TABLE %s(%s)"),
        CREATE_SEQUENCE("CREATE SEQUENCE %s_SEQ AS %s START WITH 1"),
        INSERT("INSERT INTO %s VALUES (NEXT VALUE FOR %s_SEQ, %s)"),
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

    public <T> String createTable(EntityTable<T> table) {
        return format(Query.CREATE_TABLE.template, table.getName(), createColumnDefinitions(table));
    }

    public <T> String createSequence(EntityTable<T> table) {
        return format(Query.CREATE_SEQUENCE.template, table.getName(), table.getIdColumn().getDefinetion());
    }

    public <T> String drop(EntityTable<T> table) {
        return format(Query.DROP.template, table.getName());
    }

    public <T> String insert(EntityTable<T> table) {
        return format(Query.INSERT.template, table.getName(), table.getName(), createQuestions(table.getColumns()
                .size() - 1));
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
