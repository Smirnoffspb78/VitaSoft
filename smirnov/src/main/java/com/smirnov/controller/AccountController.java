package com.smirnov.controller;


import com.smirnov.dto.get.LoginPasswordDTO;
import com.smirnov.dto.get.Token;
import com.smirnov.services.security.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;


/**
 * Контроллер аккаунта.
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    /**
     * Сервисный слой аккаунта.
     */
    private final AccountService accountService;


    /**
     * Авторизует пользователя.
     * @param loginPassword Содержит логин и пароль
     * @return Токен пользователя
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Token loginAccount(@RequestBody LoginPasswordDTO loginPassword) {
        try {
            log.info("POST: /account/login");
            return accountService.loginAccount(loginPassword.getLogin(), loginPassword.getPassword());
        } catch (AccountNotFoundException e) {
            log.error("Введены неверные логин или пароль");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
