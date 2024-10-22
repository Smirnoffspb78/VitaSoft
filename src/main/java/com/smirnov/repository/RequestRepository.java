package com.smirnov.repository;

import com.smirnov.entity.Request;
import com.smirnov.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий заявок.
 */
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Возвращает заявку по идентификатору, статусу заявки и идентификатору пользователя
     *
     * @param id     Идентификатор заявки
     * @param status Статус заявки
     * @param userId Идентификатор пользователя
     * @return Заявка
     */
    Optional<Request> findByIdAndStatusAndUser_id(Long id, RequestStatus status, Integer userId);

    /**
     * Возвращает заявку по идентификатору и статусу заявки
     *
     * @param id     Идентификатор заявки
     * @param status Статус заявки
     * @return Заявка
     */
    Optional<Request> findByIdAndStatus(Long id, RequestStatus status);

    /**
     * Возвращает страницу всех заявок, по статусу
     *
     * @param status   Статус заявки
     * @param pageable Страница
     * @return Страница с заявками
     */
    Page<Request> findByStatus(RequestStatus status, Pageable pageable);

    /**
     * Возвращает страницу всех заявок, по идентификатору пользователя
     *
     * @param userId   Идентификатор пользователя
     * @param pageable Страница
     * @return Страница с заявками
     */
    Page<Request> findByUser_id(Integer userId, Pageable pageable);

    /**
     * Возвращает страницу всех заявок, по статусу, имени/части имени пользователя.
     *
     * @param name     Имя/Часть имени
     * @param status   Статус заявки
     * @param pageable Страница
     * @return Страница с заявками
     */
    Page<Request> findByStatusAndUser_nameContainingIgnoreCase(RequestStatus status, String name, Pageable pageable);
}
