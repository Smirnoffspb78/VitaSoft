package com.smirnov.controller;


import com.smirnov.exception.DuplicateRoleException;
import com.smirnov.exception.EntityNotFoundException;
import com.smirnov.exception.ExtractCredentialsException;
import com.smirnov.exception.JWTValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Обработчик исключений.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler({EntityNotFoundException.class, HttpRequestMethodNotSupportedException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundExceptionException(RuntimeException e) {
        return responseServer(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(JWTValidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleJWTValidException(JWTValidException e, HttpServletResponse response) throws IOException {
        response.getWriter().write(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notValidException(MethodArgumentNotValidException e) {
        String errorMessages = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n", "\n", ""));
        return responseServer(HttpStatus.BAD_REQUEST, errorMessages);
    }

    @ExceptionHandler({DuplicateRoleException.class, ExtractCredentialsException.class, IllegalArgumentException.class,
            NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badRequestException(RuntimeException e) {
        return responseServer(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private String responseServer(HttpStatus httpStatus, String message) {
        log.error("{}. {}", httpStatus, message);
        return message;
    }


}
