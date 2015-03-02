package com.github.disc99.util;

import java.util.function.Consumer;

import com.github.disc99.orm.DataAccessException;

@FunctionalInterface
public interface UncheckedConsumer<T> extends Consumer<T> {
    public void func(T arg) throws Exception;

    public default void accept(T t) {
        try {
            func(t);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    };
}
