package com.smirnov.controller;


import com.smirnov.dto.get.Token;
import com.smirnov.services.security.AuthService;
import com.smirnov.services.security.BasicAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * Контроллер для работы с аккаунтом.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    /**
     * Сервисный слой аккаунта.
     */
    private final AuthService authService;

    private final BasicAuth basicAuth;


    /**
     * Авторизует пользователя.
     *
     * @return Токен пользователя
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Token loginAccount(@RequestHeader("Authorization") String authHeader) {
        String[] credentials = basicAuth.extractCredentials(authHeader);
        String login = credentials[0];
        String password = credentials[1];
        Token token = authService.loginAccount(login, password);
        log.info("POST: /auth/login.\n Аутентифицирован user с login: {}", login);
        return token;
    }
}
