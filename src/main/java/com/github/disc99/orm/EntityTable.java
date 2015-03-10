package com.github.disc99.orm;

import static com.github.disc99.util.Databases.camelToSnake;
import static java.util.stream.Collectors.toList;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityTable<T> {
    private Class<?> clazz;

    public EntityTable(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return camelToSnake(clazz.getSimpleName());
    }

    public List<EntityColumn> getColumns() {
        return getFields().stream()
                .map(EntityColumn::new)
                .collect(toList());
    }

    public EntityColumn getIdColumn() {
        return getColumns().stream()
                .filter(EntityColumn::isIdColumn)
                .findFirst()
                .get();
    }

    public List<EntityColumn> getNotIdColumns() {
        return getColumns().stream()
                .filter(EntityColumn::isNotIdColumn)
                .collect(toList());
    }

    public List<String> getColumnNames() {
        return getColumns().stream()
                .map(EntityColumn::getName)
                .collect(toList());
    }

    public List<String> getNotIdColumnNames() {
        return getNotIdColumns().stream()
                .map(EntityColumn::getName)
                .collect(toList());
    }

    private List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                new PropertyDescriptor(field.getName(), clazz);
                fields.add(field);
            } catch (Exception e) {
                // ignore nothing property
            }
        }
        return fields;

    }
}
