package com.smirnov.controller;


import com.smirnov.dto.get.LoginPasswordDTO;
import com.smirnov.dto.get.Token;
import com.smirnov.services.security.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;


/**
 * Контроллер аккаунта.
 */
@RestController
@RequestMapping("/user-account")
@RequiredArgsConstructor
@Slf4j
public class UserAccountController {

    /**
     * Сервисный слой аккаунта.
     */
    private final UserAccountService userAccountService;


    /**
     * Авторизует пользователя.
     * @param loginPassword Содержит логин и пароль
     * @return Токен пользователя
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Token loginAccount(@RequestBody LoginPasswordDTO loginPassword) {
        try {
            log.info("POST: /user-account/login");
            return userAccountService.loginAccount(loginPassword.getLogin(), loginPassword.getPassword());
        } catch (AccountNotFoundException e) {
            log.error("Введены неверные логин или пароль");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
