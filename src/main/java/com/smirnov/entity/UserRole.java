package com.smirnov.entity;

import com.smirnov.enums.UserRight;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Роли.
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Immutable
public class UserRole {

    /**
     * Идентификатор роли.
     */
    @Id
    @Enumerated(value = EnumType.STRING)
    @Column(name = "name")
    private UserRight userRight;
}