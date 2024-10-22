package com.smirnov.controller;


import com.smirnov.exception.DuplicateRoleException;
import com.smirnov.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

import static java.lang.String.format;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ResponseBody
    @ExceptionHandler({EntityNotFoundException.class, HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundExceptionException(RuntimeException e) {
        return responseServer(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notValidException(MethodArgumentNotValidException e) {
        String errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n", "\n", ""));
        return responseServer(HttpStatus.BAD_REQUEST, errorMessages);
    }

    @ResponseBody
    @ExceptionHandler(DuplicateRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestException(RuntimeException e) {
        return responseServer(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private String responseServer(HttpStatus httpStatus, String message) {
        log.error("{}. {}", httpStatus, message);
        return format("%s%n%s", httpStatus, message);
    }
}
