package com.smirnov.services;

import com.smirnov.dto.response.UserDTO;
import com.smirnov.entity.Role;
import com.smirnov.entity.User;
import com.smirnov.exception.EntityNotFoundException;
import com.smirnov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.smirnov.enums.RolesUser.ROLE_OPERATOR;

/**
 * Сервисный слой для работы с пользователями.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    /**
     * Репозиторий пользователей.
     */
    private final UserRepository userRepository;

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserDTO.builder().login(user.getLogin())
                        .rolesUser(user.getRolesUser().stream()
                                .map(Role::toString)
                                .collect(Collectors.toSet()))
                        .build()
                ).collect(Collectors.toList());
    }

    /**
     * Выдает права оператора пользователю.
     * Права доступа - ADMIN.
     *
     * @param id идентификатор пользователя
     */
    public void updateUserBeforeOperator(Integer id) {
        Role role = new Role();
        role.setRolesUser(ROLE_OPERATOR);
        User user = getUserById(id);
        if (user.getRolesUser().contains(role)) {
            throw new IllegalArgumentException("Пользователь уже имеет роль оператора");
        }
        user.getRolesUser().add(role);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }
}
