package com.smirnov.repository;

import com.smirnov.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    /**
     * Возвращает пользователя по логину, если он не удален.
     * @param login Логин
     * @return Пользователь
     */
    Optional<User> findByLogin(String login);

    /**
     * Возвращает список имен по переданному имени/части имени
     *
     * @param name имя/части имени
     * @return Список пользователей, чьи имена содержат переданное значение.
     */
    List<User> findByNameContainingIgnoreCase(String name);
}
