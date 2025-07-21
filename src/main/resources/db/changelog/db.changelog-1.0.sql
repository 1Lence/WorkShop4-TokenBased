--liquibase formatted sql

--changeset petrov:1
create type user_role as enum (
    'PATIENT',
    'DOCTOR',
    'ADMIN'
    );

create table users
(
    id BIGSERIAL primary key not null,
    password varchar(256) not null,
    login varchar(128) not null unique,
    email varchar(128) not null unique,
    roles user_role
);