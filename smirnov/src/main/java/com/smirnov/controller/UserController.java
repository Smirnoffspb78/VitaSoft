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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<UserDTO> getAllUsers(){
        log.info("GET: /users");
        List <UserDTO> users = userService.getAllUsers();
        log.info("{}. Получен список всех пользователей", HttpStatus.OK);
        return users;
    }

    /**
     * Выводит список пользователей.
     * Уровень доступа:
     * - ADMIN
     * @return Список пользователей
     */
    @GetMapping("{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDTO> getUsersByName(@PathVariable(name = "name") String name){
        log.info("GET: /users");
        List <UserDTO> users = userService.getUsersByName(name);
        log.info("{}. Получен список всех пользователей", HttpStatus.OK);
        return users;
    }

    @PutMapping("{id}/issue-operator-right")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateUserBeforeOperator(@PathVariable(name = "id") Integer id) {
        log.info("PUT: /users/{}/issue-operator-right", id);
        userService.addOperatorRight(id);
        log.info("{}. Пользователю с id {} выданы права оператора", HttpStatus.NO_CONTENT, id);
    }
}
