package com.github.disc99.orm;

import java.lang.reflect.Field;

public class H2 implements Database {

    public enum Type {
        IDENTITY(Integer.class),
        INT(Integer.class),
        LONG(Integer.class),
        VARCHAR(String.class),
        BOOLEAN(Boolean.class), ;
        public Class<?> clazz;

        private Type(Class<?> clazz) {
            this.clazz = clazz;
        }
    }

    @Override
    public String getType(Field field) {
        // TODO Auto-generated method stub
        return null;
    }

}