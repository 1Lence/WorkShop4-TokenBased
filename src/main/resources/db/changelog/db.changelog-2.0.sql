--liquibase formatted sql

--changeset petrov:1
ALTER TYPE user_role RENAME VALUE 'PATIENT' TO 'USER';
ALTER TYPE user_role RENAME VALUE 'DOCTOR' TO 'USER_VIP';

--changeset petrov:2
create table banned_token
(
    id    BIGSERIAL primary key not null,
    token varchar(512)          not null
);