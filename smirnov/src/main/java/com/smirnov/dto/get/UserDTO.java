package com.smirnov.dto.get;

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
