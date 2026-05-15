-- Dummy seed data for Library Management System
-- Admin login:  username=admin     password=admin123
-- Member login: username=jatin     password=jatin@123
-- Member login: username=member1   password=member123

-- ─── Users ───────────────────────────────────────────────────────────────────

INSERT INTO users (username, password, email, full_name, phone, address, role, is_blocked, created_at) VALUES
('admin',   '$2b$10$gk8At3CDUcbpmjox1.509OP3o1ROahdlcx0JBTee1DlWUI0BQpEV2', 'admin@library.com',  'Library Administrator', '9999999999', 'Library Building, College Campus', 'ADMIN',  false, NOW()),
('jatin',   '$2b$10$T7vkkZ9reWj15De4pY3Ph.2oVTFkw4ksIdezLmio4iZ698mqSU2TC', 'jatin@college.edu',  'Jatin Sharma',          '9876543210', '14 Green Park, Delhi',            'MEMBER', false, NOW()),
('member1', '$2b$10$U5ZWMR11XOiBb/Ut6aIHSeZLA1gDHeTbTcOYTXBVmws5B.nw9YNPO', 'alice@college.edu',  'Alice Johnson',         '9123456780', '45 Park Street, Mumbai',          'MEMBER', false, NOW()),
('member2', '$2b$10$U5ZWMR11XOiBb/Ut6aIHSeZLA1gDHeTbTcOYTXBVmws5B.nw9YNPO', 'bob@college.edu',    'Bob Smith',             '9001234567', '7 Lake View, Nashik',             'MEMBER', false, NOW()),
('member3', '$2b$10$U5ZWMR11XOiBb/Ut6aIHSeZLA1gDHeTbTcOYTXBVmws5B.nw9YNPO', 'charlie@college.edu','Charlie Brown',         '9812345670', '22 Rose Garden, Nagpur',          'MEMBER', false, NOW());

-- ─── Books ───────────────────────────────────────────────────────────────────

INSERT INTO books (title, author, isbn, category, publisher, publication_year, total_copies, available_copies, shelf_location, created_at) VALUES
('The Great Gatsby',                'F. Scott Fitzgerald',  '978-0-7432-7356-5', 'Fiction',          'Scribner',              1925, 5, 4, 'A-101', NOW()),
('To Kill a Mockingbird',           'Harper Lee',           '978-0-0619-3534-6', 'Fiction',          'J. B. Lippincott & Co.',1960, 4, 3, 'A-102', NOW()),
('Introduction to Algorithms',      'Thomas H. Cormen',     '978-0-2620-3384-8', 'Computer Science', 'MIT Press',             2009, 6, 5, 'B-201', NOW()),
('Clean Code',                      'Robert C. Martin',     '978-0-1323-5088-4', 'Computer Science', 'Prentice Hall',         2008, 5, 4, 'B-202', NOW()),
('A Brief History of Time',         'Stephen Hawking',      '978-0-5530-5425-9', 'Science',          'Bantam Books',          1988, 3, 2, 'C-301', NOW()),
('The Alchemist',                   'Paulo Coelho',         '978-0-0617-2492-6', 'Fiction',          'HarperCollins',         1988, 7, 6, 'A-103', NOW()),
('Sapiens: A Brief History',        'Yuval Noah Harari',    '978-0-0624-1609-7', 'History',          'Harper',                2011, 4, 4, 'D-401', NOW()),
('Atomic Habits',                   'James Clear',          '978-0-7352-1129-2', 'Self-Help',        'Avery',                 2018, 6, 5, 'G-701', NOW()),
('The Pragmatic Programmer',        'Andrew Hunt',          '978-0-1359-5705-9', 'Computer Science', 'Addison-Wesley',        1999, 3, 2, 'B-203', NOW()),
('1984',                            'George Orwell',        '978-0-4522-8423-4', 'Fiction',          'Secker & Warburg',      1949, 5, 4, 'A-104', NOW()),
('Thinking, Fast and Slow',         'Daniel Kahneman',      '978-0-3745-3355-7', 'Psychology',       'Farrar Straus Giroux',  2011, 3, 3, 'F-601', NOW()),
('Deep Work',                       'Cal Newport',          '978-1-4555-8669-1', 'Self-Help',        'Grand Central',         2016, 4, 3, 'G-703', NOW()),
('Design Patterns',                 'Gang of Four',         '978-0-2016-3361-5', 'Computer Science', 'Addison-Wesley',        1994, 4, 4, 'B-204', NOW()),
('The Lean Startup',                'Eric Ries',            '978-0-3071-8789-5', 'Business',         'Crown Business',        2011, 3, 3, 'H-801', NOW()),
('Operating System Concepts',       'Abraham Silberschatz', '978-1-1186-3388-4', 'Computer Science', 'Wiley',                 2018, 5, 4, 'B-205', NOW());

-- ─── Jatin's transactions (5 books, different states) ────────────────────────

-- Book 1: Returned on time (Clean Code)
INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(2, 4, DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -16, CURRENT_DATE), DATEADD('DAY', -18, CURRENT_DATE), 0.00, 'RETURNED', false);

-- Book 2: Currently issued, within due date (Introduction to Algorithms)
INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(2, 3, DATEADD('DAY', -7, CURRENT_DATE), DATEADD('DAY', 7, CURRENT_DATE), NULL, 0.00, 'ISSUED', false);

-- Book 3: Overdue, fine accumulated (The Great Gatsby)
INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(2, 1, DATEADD('DAY', -25, CURRENT_DATE), DATEADD('DAY', -11, CURRENT_DATE), NULL, 55.00, 'OVERDUE', false);

-- Book 4: Returned late with fine paid (A Brief History of Time)
INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(2, 5, DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -36, CURRENT_DATE), DATEADD('DAY', -30, CURRENT_DATE), 30.00, 'RETURNED', true);

-- Book 5: Currently issued, due soon (Atomic Habits)
INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(2, 8, DATEADD('DAY', -12, CURRENT_DATE), DATEADD('DAY', 2, CURRENT_DATE), NULL, 0.00, 'ISSUED', false);

-- ─── Other members' transactions ─────────────────────────────────────────────

INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(3, 2,  DATEADD('DAY', -5,  CURRENT_DATE), DATEADD('DAY', 9,  CURRENT_DATE), NULL,                              0.00,  'ISSUED',   false),
(4, 9,  DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -6, CURRENT_DATE), NULL,                              35.00, 'OVERDUE',  false),
(5, 10, DATEADD('DAY', -15, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE),  0.00,  'RETURNED', false);
