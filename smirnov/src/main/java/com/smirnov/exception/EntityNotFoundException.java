package com.smirnov.exception;


import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {

    private static final String MESSAGE = "%s with id %s not found";

    public EntityNotFoundException(Class<?> entityClass, Number id) {
        super(format(MESSAGE, entityClass.getSimpleName(), id));
    }
}
