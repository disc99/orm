package com.github.disc99.util;

import java.util.function.Function;

import com.github.disc99.orm.DataAccessException;

@FunctionalInterface
public interface UncheckedFunction<T, R> extends Function<T, R> {
    public R func(T arg) throws Exception;

    public default R apply(T t) {
        try {
            return func(t);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    };
}
