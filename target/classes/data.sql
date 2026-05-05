-- ─────────────────────────────────────────────────────────────────────────────
-- Seed data for Library Management System
-- Admin password: admin123  (BCrypt hash below)
-- Run once on first startup (spring.sql.init.mode=always guards duplicates via INSERT IGNORE)
-- ─────────────────────────────────────────────────────────────────────────────

-- Admin user (username: admin / password: admin123)
INSERT IGNORE INTO users (username, password, email, full_name, phone, address, role, is_blocked, created_at)
VALUES (
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Zxmy',
    'admin@library.com',
    'Library Administrator',
    '9999999999',
    'Library Building, College Campus',
    'ADMIN',
    false,
    NOW()
);

-- ─── Sample Books ─────────────────────────────────────────────────────────────

INSERT IGNORE INTO books (title, author, isbn, category, publisher, publication_year, total_copies, available_copies, shelf_location, created_at) VALUES
('The Great Gatsby',                  'F. Scott Fitzgerald',  '978-0-7432-7356-5', 'Fiction',          'Scribner',              1925, 5, 5, 'A-101', NOW()),
('To Kill a Mockingbird',             'Harper Lee',           '978-0-0619-3534-6', 'Fiction',          'J. B. Lippincott & Co.',1960, 4, 4, 'A-102', NOW()),
('Introduction to Algorithms',        'Thomas H. Cormen',     '978-0-2620-3384-8', 'Computer Science', 'MIT Press',             2009, 6, 6, 'B-201', NOW()),
('Clean Code',                        'Robert C. Martin',     '978-0-1323-5088-4', 'Computer Science', 'Prentice Hall',         2008, 5, 5, 'B-202', NOW()),
('A Brief History of Time',           'Stephen Hawking',      '978-0-5530-5425-9', 'Science',          'Bantam Books',          1988, 3, 3, 'C-301', NOW()),
('The Alchemist',                     'Paulo Coelho',         '978-0-0617-2492-6', 'Fiction',          'HarperCollins',         1988, 7, 7, 'A-103', NOW()),
('Sapiens: A Brief History',          'Yuval Noah Harari',    '978-0-0624-1609-7', 'History',          'Harper',                2011, 4, 4, 'D-401', NOW()),
('Calculus: Early Transcendentals',   'James Stewart',        '978-1-2854-1552-1', 'Mathematics',      'Cengage Learning',      2015, 5, 5, 'E-501', NOW()),
('The Pragmatic Programmer',          'Andrew Hunt',          '978-0-1359-5705-9', 'Computer Science', 'Addison-Wesley',        1999, 3, 3, 'B-203', NOW()),
('Design Patterns',                   'Gang of Four',         '978-0-2016-3361-5', 'Computer Science', 'Addison-Wesley',        1994, 4, 4, 'B-204', NOW()),
('Thinking, Fast and Slow',           'Daniel Kahneman',      '978-0-3745-3355-7', 'Psychology',       'Farrar Straus Giroux',  2011, 3, 3, 'F-601', NOW()),
('Atomic Habits',                     'James Clear',          '978-0-7352-1129-2', 'Self-Help',        'Avery',                 2018, 6, 6, 'G-701', NOW()),
('1984',                              'George Orwell',        '978-0-4522-8423-4', 'Fiction',          'Secker & Warburg',      1949, 5, 5, 'A-104', NOW()),
('The Lean Startup',                  'Eric Ries',            '978-0-3071-8789-5', 'Business',         'Crown Business',        2011, 3, 3, 'H-801', NOW()),
('Operating System Concepts',         'Abraham Silberschatz', '978-1-1186-3388-4', 'Computer Science', 'Wiley',                 2018, 5, 5, 'B-205', NOW());
