package com.smirnov.services.security;

import com.smirnov.dto.get.UserDetailsCustom;
import com.smirnov.entity.User;
import com.smirnov.entity.UserRole;
import com.smirnov.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticatedService implements UserDetailsService {

    private final UserService userService;
    /**
     * Возвращает пользователя по логину.
     *
     * @param username логин пользователя
     * @return пользователь в контексте Spring Security
     */
    @Override
    public UserDetailsCustom loadUserByUsername(String username) {
        User user = userService.getUserByLogin(username);
        Set<GrantedAuthority> grantedAuthorities = user.getRolesUser().stream()
                .map(UserRole::getUserRight)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        log.info("Аутентифицирован user с login: {}. Роль: {}", user.getLogin(), grantedAuthorities);
        return new UserDetailsCustom(username, user.getPassword(), grantedAuthorities, user.getId());
    }
}
