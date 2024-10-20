--liquibase formatted sql

--changeset Dmitry Smirnov:1_7
--comment: Добавление заявок в таблицу

--Заявки

insert into requests(message, user_id, status, created_at)
values ('Помоги мне', 1, 'SENT','2024-09-17 12:00:00'),
       ('Помоги мне опять', 1, 'DRAFT','2024-09-17 12:20:00'),
       ('Сломался компьютер', 2, 'DRAFT','2024-09-17 11:55:00'),
       ('Сломался принтер', 2, 'ACCEPTED','2024-09-17 11:35:00'),
       ('Сломался ноутбук', 3, 'REJECTED','2024-09-17 11:45:00'),
       ('Сломался микрофон', 3, 'ACCEPTED','2024-09-17 11:52:00');

