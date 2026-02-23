-- Seed data for local/dev use (idempotent)

INSERT INTO categories (name, description, icon_url, active, display_order, date_created, last_updated)
VALUES
    ('Meditation', 'Mindfulness and guided meditation sessions', '/images/icons/meditation.svg', true, 1, now(), now()),
    ('Yoga', 'Yoga classes for all levels', '/images/icons/yoga.svg', true, 2, now(), now()),
    ('Breathwork', 'Breathing techniques and circles', '/images/icons/breathwork.svg', true, 3, now(), now())
ON CONFLICT (name) DO NOTHING;

INSERT INTO "User" (name, email, password, role, first_name, last_name, avatar_url, date_created, last_updated)
VALUES
    ('Admin User', 'admin@mindhealth.local', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8T0s7xI.6Y4ZCkxzgv2Fne5DE5F1S', 'ROLE_ADMIN', 'Admin', 'User', NULL, now(), now()),
    ('Organizer User', 'organizer@mindhealth.local', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8T0s7xI.6Y4ZCkxzgv2Fne5DE5F1S', 'ROLE_ORGANIZER', 'Event', 'Host', NULL, now(), now()),
    ('Regular User', 'user@mindhealth.local', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHi8T0s7xI.6Y4ZCkxzgv2Fne5DE5F1S', 'ROLE_USER', 'Regular', 'User', NULL, now(), now())
ON CONFLICT (email) DO NOTHING;
