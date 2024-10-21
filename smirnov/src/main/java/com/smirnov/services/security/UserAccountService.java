package com.smirnov.services.security;

import com.nimbusds.jose.JOSEException;
import com.smirnov.dto.get.Token;
import com.smirnov.dto.get.UserDetailsCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserAccountService {
    private final JwtSecurityService jwtSecurityService;
    private final AuthenticationManager authenticationManager;

    /**
     * Авторизует пользователя по логину и паролю
     *
     * @param login    Логин
     * @param password Пароль
     * @return Токен
     */
    public Token loginAccount(String login, String password) throws AccountNotFoundException {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login, password));
        getContext().setAuthentication(authentication);
        Token token = new Token();
        try {
            token.setAccessToken(jwtSecurityService.generateToken((UserDetailsCustom) authentication.getPrincipal()));
        } catch (JOSEException e) {
            log.error("Не правильно введены логин или пароль");
            throw new AccountNotFoundException("Не правильно введены логин или пароль");
        }
        token.setRefreshToken(jwtSecurityService.generateRefreshToken());
        return token;
    }
}
