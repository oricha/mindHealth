-- Security, privacy, and data constraints

ALTER TABLE "User"
    ADD CONSTRAINT uq_user_email UNIQUE (email);

ALTER TABLE ticket
    ADD CONSTRAINT uq_ticket_qr_code UNIQUE (qr_code);

ALTER TABLE review
    ADD CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5);

ALTER TABLE event
    ADD CONSTRAINT chk_event_available_seats CHECK (available_seats >= 0),
    ADD CONSTRAINT chk_event_max_attendees CHECK (max_attendees IS NULL OR max_attendees >= 0),
    ADD CONSTRAINT chk_event_price_non_negative CHECK (price >= 0);

CREATE INDEX IF NOT EXISTS idx_event_title_desc_fts
    ON event USING GIN (to_tsvector('simple', coalesce(title,'') || ' ' || coalesce(description,'')));

CREATE INDEX IF NOT EXISTS idx_ticket_user_event ON ticket(user_id, event_id);
CREATE INDEX IF NOT EXISTS idx_payment_user_status ON payment(user_id, status);
