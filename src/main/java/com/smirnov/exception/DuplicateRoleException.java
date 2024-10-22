package com.smirnov.exception;

import static java.lang.String.format;

public class DuplicateRoleException extends RuntimeException {
    private static final String MESSAGE = "Пользователь c id %s уже имеет роль оператора.";

    public DuplicateRoleException(Number id) {
        super(format(MESSAGE, id));
    }
}
