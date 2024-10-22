package com.smirnov.services;

import com.smirnov.dto.get.UserDTO;
import com.smirnov.entity.UserRole;
import com.smirnov.entity.User;
import com.smirnov.exception.EntityNotFoundException;
import com.smirnov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.smirnov.enums.UserRight.ROLE_OPERATOR;

/**
 * Сервисный слой для работы с пользователями.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(String name) {
        List<User> users = name == null ? userRepository.findAll() : userRepository.findByNameContainingIgnoreCase(name);
        return users.stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    /**
     * Выдает права оператора пользователю.
     * Права доступа - ADMIN.
     *
     * @param id идентификатор пользователя
     */
    public void addOperatorRight(Integer id) {
        getUserById(id).addRole(ROLE_OPERATOR);
    }


    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    @Transactional(readOnly = true)
    public User getUserByLogin(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDTO mapUser(User user) {
        return UserDTO.builder()
                .login(user.getLogin())
                .name(user.getName())
                .rolesUser(user.getRolesUser().stream()
                        .map(userRole -> userRole.getUserRight().toString())
                        .collect(Collectors.toSet()))
                .build();
    }
}
