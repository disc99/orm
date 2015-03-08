package com.github.disc99.orm.sql;

import static com.github.disc99.util.Databases.camelToSnake;
import static java.util.Objects.requireNonNull;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.Id;

import com.github.disc99.orm.DataAccessException;

class EntityColumn {

    private Field entityProperty;
    private Database db = new Derby(); // TODO inject or factory

    public EntityColumn(Field entityProperty) {
        requireNonNull(entityProperty);
        this.entityProperty = entityProperty;
    }

    public String getName() {
        return camelToSnake(entityProperty.getName());
    }

    public Class<?> getClassType() {
        return entityProperty.getType();
    }

    public String getDefinetion() {
        return db.getType(entityProperty);
    }

    public <T> Object getValue(T entity) {
        try {
            return createProperty(entity).getReadMethod().invoke(entity, (Object[]) null);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new DataAccessException("Fail getter invoke.", e);
        }
    }

    public <T> void setValue(T entity, Object value) {
        try {
            createProperty(entity).getWriteMethod().invoke(entity, value);
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new DataAccessException("Fail getter invoke.", e);
        }
    }

    private <T> PropertyDescriptor createProperty(T entity) throws IntrospectionException {
        return new PropertyDescriptor(entityProperty.getName(), entity.getClass());
    }

    public boolean isIdColumn() {
        return entityProperty.isAnnotationPresent(Id.class);
    }

    public boolean isNotIdColumn() {
        return !isIdColumn();
    }
}
