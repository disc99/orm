package com.github.disc99.orm;

public class DataAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataAccessException(String massage) {
        super(massage);
    }

    public DataAccessException(String massage, Throwable e) {
        super(massage, e);
    }

    public DataAccessException(Throwable e) {
        super(e);
    }
}
