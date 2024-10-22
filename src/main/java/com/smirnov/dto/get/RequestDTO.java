package com.smirnov.dto.get;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RequestDTO {
    /**
     * Текст обращения пользователя.
     */
    private String message;

    /**
     * Пользователь.
     */
    private String userLogin;

    /**
     * Имя пользователя.
     */
    private String userName;

    /**
     * Статус заявки.
     */
    private String status;

    /**
     * Дата и время создания заявки.
     */
    private LocalDateTime createdAt;
}