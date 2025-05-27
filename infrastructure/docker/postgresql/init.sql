CREATE TABLE persons (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_persons_name ON persons(name);