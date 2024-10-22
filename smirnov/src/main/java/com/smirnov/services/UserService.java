package com.smirnov.services;

import com.smirnov.dto.get.UserDTO;
import com.smirnov.dto.get.UserDetailsCustom;
import com.smirnov.entity.Role;
import com.smirnov.entity.User;
import com.smirnov.exception.DuplicateRoleException;
import com.smirnov.exception.EntityNotFoundException;
import com.smirnov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.smirnov.enums.RolesUser.ROLE_OPERATOR;

/**
 * Сервисный слой для работы с пользователями.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

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
                .map(user -> UserDTO.builder()
                        .login(user.getLogin())
                        .name(user.getName())
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
    public void addOperatorRight(Integer id) {
        Role role = new Role();
        role.setRolesUser(ROLE_OPERATOR);
        User user = getUserById(id);
        if (user.getRolesUser().contains(role)) {
            throw new DuplicateRoleException(id);
        }
        user.getRolesUser().add(role);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));
    }

    /**
     * Возвращает пользователя по логину.
     *
     * @param username логин пользователя
     * @return пользователь в контексте Spring Security
     */
    @Override
    public UserDetailsCustom loadUserByUsername(String username) {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Set<GrantedAuthority> grantedAuthorities = user.getRolesUser().stream()
                .map(Role::getRolesUser)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        log.info("Аутентифицирован user с login: {}. Роль: {}", user.getLogin(), grantedAuthorities);
        return new UserDetailsCustom(username, user.getPassword(), grantedAuthorities, user.getId());
    }
}
