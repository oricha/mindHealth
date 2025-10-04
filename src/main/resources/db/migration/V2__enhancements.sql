-- User table enhancements
ALTER TABLE "User"
    ADD COLUMN IF NOT EXISTS provider VARCHAR(50),
    ADD COLUMN IF NOT EXISTS provider_id VARCHAR(255),
    ADD COLUMN IF NOT EXISTS email_verified BOOLEAN;

-- Event table enhancements
ALTER TABLE event
    ADD COLUMN IF NOT EXISTS address VARCHAR(255),
    ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION,
    ADD COLUMN IF NOT EXISTS max_attendees INTEGER,
    ADD COLUMN IF NOT EXISTS sold_tickets INTEGER,
    ADD COLUMN IF NOT EXISTS total_revenue NUMERIC(12,2),
    ADD COLUMN IF NOT EXISTS active BOOLEAN;

-- Category table enhancements
ALTER TABLE categories
    ADD COLUMN IF NOT EXISTS icon_url VARCHAR(255),
    ADD COLUMN IF NOT EXISTS active BOOLEAN,
    ADD COLUMN IF NOT EXISTS display_order INTEGER;

-- Ticket table enhancements
ALTER TABLE ticket
    ADD COLUMN IF NOT EXISTS s3_key VARCHAR(255),
    ADD COLUMN IF NOT EXISTS quantity INTEGER;

-- Payment table enhancements
ALTER TABLE payment
    ADD COLUMN IF NOT EXISTS currency VARCHAR(10),
    ADD COLUMN IF NOT EXISTS stripe_payment_intent_id VARCHAR(255);

-- Review table
CREATE TABLE IF NOT EXISTS review (
    id BIGINT PRIMARY KEY,
    rating INTEGER NOT NULL,
    comment VARCHAR(2000),
    organizer_response VARCHAR(2000),
    user_id BIGINT,
    event_id BIGINT,
    date_created TIMESTAMPTZ,
    last_updated TIMESTAMPTZ
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_event_datetime ON event(date_time);
CREATE INDEX IF NOT EXISTS idx_event_category ON event(category_id);
CREATE INDEX IF NOT EXISTS idx_event_location ON event(location);
CREATE INDEX IF NOT EXISTS idx_ticket_qrcode ON ticket(qr_code);
CREATE INDEX IF NOT EXISTS idx_payment_stripe_id ON payment(stripe_payment_intent_id);
