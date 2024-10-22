package com.smirnov.dto.response;

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
     * Статус заявки.
     */
    private String status;

    /**
     * Дата и время создания заявки.
     */
    private LocalDateTime createdAt;
}
