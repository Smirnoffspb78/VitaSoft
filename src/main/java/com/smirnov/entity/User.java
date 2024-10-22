package com.smirnov.entity;

import com.smirnov.enums.UserRight;
import com.smirnov.exception.DuplicateRoleException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static com.smirnov.enums.UserRight.ROLE_OPERATOR;

/**
 * Пользователь.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Логин.
     */
    @Pattern(regexp = "[A-z\\d][A-z\\d.]{3,199}",
            message = "Логин должен содержать латинские буквы, цифры или символ \".\". Длина логины должна иметь хотя-бы три символ. Логин не может начинаться с \".\"")
    @Column(name = "login", updatable = false)
    private String login;

    /**
     * Пароль.
     */
    @Pattern(regexp = "[A-z\\d!#$*]{8,200}",
            message = "password может содержать латинские буквы, цифры и символы !#$*. Длина должна быть от 8 до 200 символов")
    @Column(name = "password")
    private String password;

    /**
     * Имя.
     */
    @Pattern(regexp = "[A-ZА-Я][A-zА-я-]{0,199}", message = "Имя должно начинаться с заглавной латинской или русской буквы. Имя может содержать символ \"-\"")
    @Column(name = "name")
    private String name;

    /**
     * Роли пользователя
     */
    @NotEmpty(message = "rolesUser не должен быть пустым")
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Set<UserRole> rolesUser = new HashSet<>();

    /**
     * Добавляет новую роль пользователю.
     *
     * @param userRight Роль пользователя
     */
    public void addRole(UserRight userRight) {
        UserRole userRole = new UserRole();
        userRole.setUserRight(userRight);
        if (rolesUser.contains(userRole)) {
            throw new DuplicateRoleException(id);
        }
        rolesUser.add(userRole);
    }
}
