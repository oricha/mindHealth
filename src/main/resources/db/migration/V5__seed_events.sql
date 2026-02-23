-- Seed sample events for home page display (idempotent)

INSERT INTO event (
    title, description, image_url, date_time, location, address,
    price, available_seats, max_attendees, sold_tickets, total_revenue, active,
    category_id, organizer_id, date_created, last_updated
)
SELECT
    'Morning Meditation Session',
    'Start your day with a guided meditation to find inner peace and clarity.',
    '/images/meditation-event.jpg',
    now() + interval '7 days',
    'Amsterdam',
    'Dam Square 1, Amsterdam',
    25.00, 20, 20, 0, 0, true,
    (SELECT id FROM categories WHERE name = 'Meditation' LIMIT 1),
    (SELECT id FROM "User" WHERE email = 'organizer@mindhealth.local' LIMIT 1),
    now(), now()
WHERE EXISTS (SELECT 1 FROM categories WHERE name = 'Meditation')
  AND EXISTS (SELECT 1 FROM "User" WHERE email = 'organizer@mindhealth.local')
  AND NOT EXISTS (SELECT 1 FROM event WHERE title = 'Morning Meditation Session' LIMIT 1);

INSERT INTO event (
    title, description, image_url, date_time, location, address,
    price, available_seats, max_attendees, sold_tickets, total_revenue, active,
    category_id, organizer_id, date_created, last_updated
)
SELECT
    'Yoga in the Park',
    'All-levels yoga class in the beautiful Vondelpark. Bring your mat!',
    '/images/yoga-event.jpg',
    now() + interval '14 days',
    'Amsterdam',
    'Vondelpark, Amsterdam',
    15.00, 30, 30, 0, 0, true,
    (SELECT id FROM categories WHERE name = 'Yoga' LIMIT 1),
    (SELECT id FROM "User" WHERE email = 'organizer@mindhealth.local' LIMIT 1),
    now(), now()
WHERE EXISTS (SELECT 1 FROM categories WHERE name = 'Yoga')
  AND EXISTS (SELECT 1 FROM "User" WHERE email = 'organizer@mindhealth.local')
  AND NOT EXISTS (SELECT 1 FROM event WHERE title = 'Yoga in the Park' LIMIT 1);

INSERT INTO event (
    title, description, image_url, date_time, location, address,
    price, available_seats, max_attendees, sold_tickets, total_revenue, active,
    category_id, organizer_id, date_created, last_updated
)
SELECT
    'Breathwork Workshop',
    'Learn powerful breathing techniques to reduce stress and increase energy.',
    '/images/breathwork-event.jpg',
    now() + interval '21 days',
    'Amsterdam',
    'De Pijp, Amsterdam',
    35.00, 15, 15, 0, 0, true,
    (SELECT id FROM categories WHERE name = 'Breathwork' LIMIT 1),
    (SELECT id FROM "User" WHERE email = 'organizer@mindhealth.local' LIMIT 1),
    now(), now()
WHERE EXISTS (SELECT 1 FROM categories WHERE name = 'Breathwork')
  AND EXISTS (SELECT 1 FROM "User" WHERE email = 'organizer@mindhealth.local')
  AND NOT EXISTS (SELECT 1 FROM event WHERE title = 'Breathwork Workshop' LIMIT 1);
