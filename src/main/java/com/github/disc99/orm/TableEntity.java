package com.github.disc99.orm;

import static java.util.stream.Collectors.toList;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

public class TableEntity<T> {
    private Class<?> clazz;
    private Database db = new H2(); // TODO injection or factory

    public TableEntity(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        // TODO enable @Table
        return clazz.getSimpleName();
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

    private List<PropertyDescriptor> getPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                descriptors.add(new PropertyDescriptor(field.getName(), clazz));
            } catch (Exception e) {
                // ignore nothing property
            }
        }
        return descriptors;
    }

    public int getColumnSize() {
        return getPropertyDescriptors().size();
    }

    public Class<?> getColumnClass(int columnNum) {
        try {
            return getPropertyDescriptor(columnNum).getPropertyType();
        } catch (IllegalArgumentException e) {
            throw new DataAccessException("Fail get property typr", e);
        }
    }

    public String getColumnType(int columnNum) {
        return db.getType(getFields().get(columnNum));
    }

    public Object invokeGetter(T entity, int columsNum) {
        try {
            return getPropertyDescriptor(columsNum).getReadMethod().invoke(entity, (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new DataAccessException("Fail invoke getter", e);
        }
    }

    private PropertyDescriptor getPropertyDescriptor(int index) {
        return getPropertyDescriptors().get(index);
    }

    public String getColumnName(int i) {
        return getPropertyDescriptor(i).getDisplayName();
    }

    public List<String> getNotIdColumnNames() {
        return getFields().stream()
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .collect(toList());
    }

    public List<String> getColumnNames() {
        return getFields().stream()
                .map(Field::getName)
                .collect(toList());
    }

    public static String toSnake(String camel) {
        // TODO snake case
        return camel;
    }
}
