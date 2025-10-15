CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    age INTEGER DEFAULT 18,
    email VARCHAR(255),
    password VARCHAR(255)
);
