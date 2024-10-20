package com.smirnov.dto.create;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public final class RequestCreateDTO {

    /**
     * Текст обращения пользователя.
     */
    @NotBlank(message = "message не должен быть null и не должен быть пустым")
    private String message;

    /**
     * Пользователь.
     */

    @NotNull(message = "userId не должен быть null")
    private Integer userId;
}
