-- Create sequence for ID generation
CREATE SEQUENCE IF NOT EXISTS primary_sequence
    START WITH 10000
    INCREMENT BY 1;

-- Create tables
CREATE TABLE category (
    id BIGINT PRIMARY KEY DEFAULT nextval('primary_sequence'),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE "user" (
    id BIGINT PRIMARY KEY DEFAULT nextval('primary_sequence'),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE event (
    id BIGINT PRIMARY KEY DEFAULT nextval('primary_sequence'),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    image_url VARCHAR(255),
    date_time TIMESTAMP WITH TIME ZONE NOT NULL,
    location VARCHAR(255),
    price NUMERIC(12,2) NOT NULL,
    available_seats INTEGER NOT NULL,
    category_id BIGINT NOT NULL,
    organizer_id BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE ticket (
    id BIGINT PRIMARY KEY DEFAULT nextval('primary_sequence'),
    qr_code VARCHAR(255),
    purchase_date TIMESTAMP WITH TIME ZONE,
    payment_status VARCHAR(255),
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE payment (
    id BIGINT PRIMARY KEY DEFAULT nextval('primary_sequence'),
    payment_method VARCHAR(255),
    amount NUMERIC(12,2),
    payment_date TIMESTAMP WITH TIME ZONE,
    status VARCHAR(255),
    ticket_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE notification (
    id BIGINT PRIMARY KEY DEFAULT nextval('primary_sequence'),
    message VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE,
    is_read BOOLEAN,
    user_id BIGINT NOT NULL,
    date_created TIMESTAMP WITH TIME ZONE NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Add foreign key constraints
ALTER TABLE event
    ADD CONSTRAINT fk_event_category
    FOREIGN KEY (category_id)
    REFERENCES category(id);

ALTER TABLE event
    ADD CONSTRAINT fk_event_organizer
    FOREIGN KEY (organizer_id)
    REFERENCES "user"(id);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_user
    FOREIGN KEY (user_id)
    REFERENCES "user"(id);

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_event
    FOREIGN KEY (event_id)
    REFERENCES event(id);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_ticket
    FOREIGN KEY (ticket_id)
    REFERENCES ticket(id);

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_user
    FOREIGN KEY (user_id)
    REFERENCES "user"(id);

ALTER TABLE notification
    ADD CONSTRAINT fk_notification_user
    FOREIGN KEY (user_id)
    REFERENCES "user"(id);