package com.github.disc99.orm;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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

    // public List<Column> getColumns() {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // public String getName() {
    // return cls.getSimpleName();
    // }
    //
    // public List<String> getColumnNames() {
    // return Stream.of(cls.getDeclaredFields())
    // .map(f -> f.getName())
    // .collect(toList());
    // }
    //
    // public long getColumnCount() {
    // return Stream.of(cls.getDeclaredFields())
    // .map(f -> f.getName())
    // .count();
    // }
    //
    // public List<Getter<T>> getGetters() {
    //
    // List<Getter<T>> getters = new ArrayList<>();
    //
    // for (Field field : cls.getDeclaredFields()) {
    // try {
    // getters.add(new Getter<>(cls, field.getName()));
    // } catch (Exception e) {
    // // Ignore getter not found property
    // }
    // }
    //
    // return getters;
    // }

    public String getColumnName(int i) {
        // TODO snake case
        return getPropertyDescriptor(i).getDisplayName();
    }
}
