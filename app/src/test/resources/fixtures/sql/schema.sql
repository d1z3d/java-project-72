DROP TABLE IF EXISTS urls, urls_checks;
CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS urls_checks(
    id SERIAL PRIMARY KEY,
    status_code INTEGER NOT NULL,
    title VARCHAR(255),
    h1 VARCHAR(255),
    description text,
    url_id INTEGER REFERENCES urls(id) NOT NULL,
    created_at TIMESTAMP NOT NULL
);