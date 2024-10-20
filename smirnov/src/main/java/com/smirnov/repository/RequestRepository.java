package com.smirnov.repository;

import com.smirnov.entity.Request;
import com.smirnov.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Возвращает заявку по идентификатору, статусу заявки и идентификатору пользователя
     *
     * @param id     Идентификатор заявки
     * @param status Статус заявки
     * @param userId Идентификатор пользователя
     * @return Заявка
     */
    Optional<Request> findByIdAndStatusAndUser_Id(Long id, RequestStatus status, Integer userId);

    /**
     * Возвращает заявку по идентификатору и статусу заявки
     *
     * @param id     Идентификатор заявки
     * @param status Статус заявки
     * @return Заявка
     */
    Optional<Request> findByIdAndStatus(Long id, RequestStatus status);
}
