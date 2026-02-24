CREATE TABLE token (
    id SERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    date_expiration TIMESTAMP NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_token_value ON token(token);