package com.smirnov.entity;

import com.smirnov.enums.RequestStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@FieldNameConstants
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Текст обращения пользователя.
     */
    @NotNull
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    /**
     * Пользователь.
     */
    @NotNull(message = "Пользователь не должен быть null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Статус заявки.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    /**
     * Дата и время создания заявки.
     */
    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
