package com.smirnov.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserDTO {

    /**
     * Логин.
     */
    private String login;

    /**
     * Роли пользователя
     */
    private Set<String> rolesUser;

}
