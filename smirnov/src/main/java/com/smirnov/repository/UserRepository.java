package com.smirnov.repository;

import com.smirnov.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();

    /**
     * Возвращает пользователя по идентификатору и роли
     * @param id Идентификатор пользователя
     * @param role роль пользователя
     * @return Пользователь
     */
/*    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.id = :userId AND r.id = :role")
    Optional<User> findByIdAndRole(@Param("userId") Integer userId, @Param("roleName") RolesUser role);*/
}
