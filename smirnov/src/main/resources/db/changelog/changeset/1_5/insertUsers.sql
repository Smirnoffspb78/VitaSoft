--liquibase formatted sql

--changeset Dmitry Smirnov:1_5
--comment: Добавление пользователей

--Пользователи
insert into users (login, password, name)
values ('ivan', '$2a$12$Zz0lfws3bwmMFsa/mrpiEuwkJLD/yTpnWt8byL3eaeJrHgvqZCSRa','Ivan'),
       ('petr', '$2a$12$IsCi37whu8cqGjZbUfTpKu1u9TWDCiGyQpabh4hiLXiPhwtKKTbf2', 'Petr'),
       ('alex', '$2a$12$aH52755JHiMJKVQ8LRaw.eJEy/o549psZq8VGgAHeQfn2oh9qO5me', 'Alex'),
       ('sidr', '$2a$12$u7lsIqsCxrqxXYuv3QanlO5mn0PvL1RM1AbOhV6ezLbNEyYYQquce', 'Sidr'),
       ('dmitry', '$2a$12$Tz9XZOkRsUOH.mU9iq0vPOo/5VBGinvW9z0QeeCmWHWeGVG0yHo/q', 'Dmitry'),
       ('admin', '$2a$12$ldSbmPy0bBnD55oeztFkuuslHLZq5Iy3aRc8N2mw0QvYEezDW.6oa', 'Admin'),
       ('sergey', '$2a$12$fXVh6iN5FINErr9CmiRI7e8kVVZBCnwdlc7xYAxTAKeUKvSumEhEi', 'Sergey');