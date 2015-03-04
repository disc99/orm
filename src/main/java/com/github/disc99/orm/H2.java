package com.github.disc99.orm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class H2 implements Database {

    // public enum Type {
    // IDENTITY(Integer.class),
    // INT(Integer.class),
    // LONG(Integer.class),
    // VARCHAR(String.class),
    // BOOLEAN(Boolean.class), ;
    // public Class<?> clazz;
    //
    // private Type(Class<?> clazz) {
    // this.clazz = clazz;
    // }
    // }

    private static final Map<Class<?>, String> typeMapping;
    static {
        typeMapping = new HashMap<>();
        typeMapping.put(Integer.class, "INT");
        typeMapping.put(int.class, "INT");
        typeMapping.put(Long.class, "LONG");
        typeMapping.put(String.class, "VARCHAR");
    }

    public static final String URL = "jdbc:h2:file:~/test";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    @Override
    public String getType(Field field) {
        // TODO IDENTITY and more...
        return typeMapping.get(field.getType());
    }

}