--liquibase formatted sql

--changeset Dmitry Smirnov:1_6
--comment: Добавление ролей пользователям

--Роли-Пользователи

insert into user_role (user_id, role_name)
values (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_OPERATOR'),
       (6, 'ROLE_ADMIN'),
       (7, 'ROLE_OPERATOR');