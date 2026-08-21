--liquibase formatted sql

--changeset huongpc:002-add-password
ALTER TABLE users ADD COLUMN password_hash VARCHAR(255);
