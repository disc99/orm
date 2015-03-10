package com.github.disc99.orm;

public interface QueryBuilder {

    <T> String createTable(EntityTable<T> table);

    <T> String createSequence(EntityTable<T> table);

    <T> String drop(EntityTable<T> table);

    <T> String insert(EntityTable<T> table);

    <T> String update(EntityTable<T> table);

    <T> String delete(EntityTable<T> table);

    <T> String selectId(EntityTable<T> table);

    <T> String selectAll(EntityTable<T> table);
}
