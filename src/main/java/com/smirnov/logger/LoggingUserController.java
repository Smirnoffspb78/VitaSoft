package com.smirnov.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Aspect
@Component
@Slf4j
public class LoggingUserController {

    @Before("execution(* com.smirnov.controller.UserController.getUsers(..)) && args(name,..)")
    public void logBeforeGetUsers(@RequestParam(name = "name", required = false) String name) {
        if (name != null) {
            log.info("GET: /users?name={}", name);
        } else {
            log.info("GET: /users");
        }
    }

    @AfterReturning("execution(* com.smirnov.controller.UserController.getUsers(..))")
    public void logAfterGetUsers() {
        log.info("{}. Получен список пользователей", HttpStatus.OK);
    }

    @Before("execution(* com.smirnov.controller.UserController.updateUserBeforeOperator(..)) && args(id,..)")
    public void logBeforeUpdateUserBeforeOperator(Integer id) {
        log.info("PUT: /users/{}/issue-operator-right", id);
    }

    @AfterReturning("execution(* com.smirnov.controller.UserController.updateUserBeforeOperator(..))")
    public void logAfterUpdateUserBeforeOperator(JoinPoint joinPoint) {
        Integer id = (Integer) joinPoint.getArgs()[0];
        log.info("{}. Пользователю с id {} выданы права оператора", HttpStatus.NO_CONTENT, id);
    }
}