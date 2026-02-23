-- Baseline schema: creates all tables required before V2 enhancements

CREATE SEQUENCE IF NOT EXISTS primary_sequence START WITH 10000 INCREMENT BY 1;

-- User table (quoted for case-sensitive name)
CREATE TABLE IF NOT EXISTS "User" (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('primary_sequence'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('primary_sequence'),
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Event table
CREATE TABLE IF NOT EXISTS event (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('primary_sequence'),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    date_time TIMESTAMPTZ NOT NULL,
    location VARCHAR(255),
    price NUMERIC(12,2) NOT NULL,
    available_seats INTEGER NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    organizer_id BIGINT NOT NULL REFERENCES "User"(id),
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Ticket table
CREATE TABLE IF NOT EXISTS ticket (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('primary_sequence'),
    qr_code VARCHAR(255) NOT NULL,
    purchase_date TIMESTAMPTZ,
    payment_status VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES "User"(id),
    event_id BIGINT NOT NULL REFERENCES event(id),
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Payment table
CREATE TABLE IF NOT EXISTS payment (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('primary_sequence'),
    payment_method VARCHAR(255) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    payment_date TIMESTAMPTZ,
    status VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES "User"(id),
    ticket_id BIGINT NOT NULL REFERENCES ticket(id),
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Notification table
CREATE TABLE IF NOT EXISTS notification (
    id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('primary_sequence'),
    message VARCHAR(255) NOT NULL,
    timestamp TIMESTAMPTZ,
    is_read BOOLEAN,
    user_id BIGINT NOT NULL REFERENCES "User"(id),
    date_created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
