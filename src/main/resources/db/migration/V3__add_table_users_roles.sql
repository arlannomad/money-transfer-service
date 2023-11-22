DROP TABLE IF EXISTS users_roles CASCADE;

CREATE TABLE users_roles(
    user_id BIGSERIAL,
    role_id BIGSERIAL
);