package com.smirnov.services;

import com.smirnov.dto.get.UserDTO;
import com.smirnov.entity.UserRole;
import com.smirnov.entity.User;
import com.smirnov.exception.DuplicateRoleException;
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
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUser)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей
     */
    public List<UserDTO> getUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name).stream()
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
        UserRole userRole = new UserRole();
        userRole.setUserRight(ROLE_OPERATOR);
        User user = getUserById(id);
        if (user.getRolesUser().contains(userRole)) {
            throw new DuplicateRoleException(id);
        }
        user.getRolesUser().add(userRole);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    public User getUserByLogin(String username){
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDTO mapUser(User user){
        return UserDTO.builder()
                .login(user.getLogin())
                .name(user.getName())
                .rolesUser(user.getRolesUser().stream()
                        .map(UserRole::toString)
                        .collect(Collectors.toSet()))
                .build();
    }
}
