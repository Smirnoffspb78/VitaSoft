--liquibase formatted sql

--changeset Dmitry Smirnov:1_2
--comment: Создание таблицы пользователей-ролей

--Пользователи-роли

CREATE TABLE user_role (
    user_id INT NOT NULL,
    role_name VARCHAR(250) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_name) REFERENCES roles(name)
);