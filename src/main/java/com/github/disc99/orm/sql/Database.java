package com.github.disc99.orm.sql;

import java.lang.reflect.Field;

public interface Database {
    public String getType(Field field);
}
