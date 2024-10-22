package com.smirnov.dto.get;

import com.smirnov.enums.RolesUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserDetailsCustom extends User {

    @EqualsAndHashCode.Include
    private final Integer id;
    private final Set<RolesUser> rolesUser;

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     *
     * @param username    Логин пользователя
     * @param password    Пароль пользователя
     * @param authorities Права доступа
     */
    public UserDetailsCustom(String username, String password, Collection<? extends GrantedAuthority> authorities, Integer id) {
        super(username, password, authorities);
        if (authorities.isEmpty()){
            throw new IllegalArgumentException("Список ролей не может быть пустым");
        }
        this.rolesUser = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(RolesUser::valueOf)
                .collect(Collectors.toSet());
        this.id = id;
    }
}