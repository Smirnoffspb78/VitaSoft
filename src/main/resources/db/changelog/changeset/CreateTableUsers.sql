--liquibase formatted sql

--changeset Dmitry Smirnov:1_1
--comment: Создание таблицы пользователей

--Пользователи

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL
);