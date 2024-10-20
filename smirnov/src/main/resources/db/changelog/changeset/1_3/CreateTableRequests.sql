--liquibase formatted sql

--changeset Dmitry Smirnov:1_3
--comment: Создание таблицы заявок

--Заявки

CREATE TABLE requests (
    id BIGSERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    user_id INT NOT NULL,
    status VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);