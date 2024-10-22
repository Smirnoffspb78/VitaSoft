package com.smirnov.controller;

import com.smirnov.dto.get.UserDTO;
import com.smirnov.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для работы с пользователями.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Выводит список пользователей.
     * Уровень доступа:
     * - ADMIN
     * @return Список пользователей
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getUsers(@RequestParam(name = "name", required = false) String name){
        return userService.getUsers(name);
    }

    /**
     * Выдает пользователю права оператора.
     * Уровень доступа:
     * - ADMIN
     * @param id Идентификатор пользователя
     */
    @PutMapping("{id}/grant-operator-right")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateUserBeforeOperator(@PathVariable(name = "id") Integer id) {
        userService.addOperatorRight(id);
    }
}
