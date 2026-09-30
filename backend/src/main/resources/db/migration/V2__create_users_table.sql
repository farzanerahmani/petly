CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    created_at   timestamptz NOT NULL,
    update_at    timestamptz NOT NULL
);
