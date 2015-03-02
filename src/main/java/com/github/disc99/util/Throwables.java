package com.github.disc99.util;

import java.util.function.Consumer;

public final class Throwables {
    private Throwables() {
    }

    public static <T> Consumer<T> uncheck(UncheckedConsumer<T> func) {
        return func;
    }

    // public static <T, R> Function<T, R> uncheck(UncheckedFunction<T, R> func)
    // {
    // return func;
    // }

    // public static boolean isThrow(Runnable func) {
    // try {
    // func.run();
    // return false;
    // } catch (Throwable e) {
    // return true;
    // }
    // }
}
