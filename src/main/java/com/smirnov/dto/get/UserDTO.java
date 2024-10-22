package com.smirnov.dto.get;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * DTO для отправки информации о пользователе.
 */
@Getter
@Setter
@Builder
public class UserDTO {

    /**
     * Логин.
     */
    private String login;

    /**
     * Имя.
     */
    private String name;

    /**
     * Роли пользователя
     */
    private Set<String> rolesUser;

}
