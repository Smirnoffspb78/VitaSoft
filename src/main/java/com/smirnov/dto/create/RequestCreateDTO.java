package com.smirnov.dto.create;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO для создания заявки.
 */
@Getter
@Setter
public final class RequestCreateDTO {

    /**
     * Текст обращения пользователя.
     */
    @NotBlank(message = "message не должен быть null и не должен быть пустым")
    private String message;

    /**
     * Идентификатор пользователя.
     */
    @NotNull(message = "userId не должен быть null")
    private Integer userId;
}
