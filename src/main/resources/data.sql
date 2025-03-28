INSERT INTO category (id, name, description, date_created, last_updated)
VALUES (1, 'Yoga', 'Relaxing group yoga classes', now(), now());
SELECT setval('primary_sequence', (SELECT MAX(id) FROM category));

INSERT INTO "user" (id, name, email, password, role, first_name, last_name, date_created, last_updated)
VALUES (1, 'Admin User', 'admin@example.com', 'password', 'ADMIN', 'Admin', 'User', now(), now());
SELECT setval('primary_sequence', (SELECT MAX(id) FROM "user"));

INSERT INTO event (id, title, description, image_url, date_time, location, price, available_seats, category_id, organizer_id, date_created, last_updated)
VALUES (1, 'Sunset Yoga', 'Evening yoga session on the beach', 'http://image.url/yoga.jpg', now(), 'Beach', 10.00, 20, 1, 1, now(), now());
SELECT setval('primary_sequence', (SELECT MAX(id) FROM event));

INSERT INTO ticket (id, event_id, user_id, quantity, total_price, status, date_created, last_updated)
VALUES (1, 1, 1, 2, 20.00, 0, now(), now());
SELECT setval('primary_sequence', (SELECT MAX(id) FROM ticket));

INSERT INTO payment (id, ticket_id, amount, status, date_created, last_updated)
VALUES (1, 1, 20.00, 0, now(), now());
SELECT setval('primary_sequence', (SELECT MAX(id) FROM payment));

INSERT INTO notification (id, user_id, title, message, is_read, date_created, last_updated)
VALUES (1, 1, 'Welcome', 'Welcome to MindHealth!', false, now(), now());
SELECT setval('primary_sequence', (SELECT MAX(id) FROM notification));
