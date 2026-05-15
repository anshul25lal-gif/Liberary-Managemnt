-- Dummy seed data for Library Management System
-- Admin login:  username=admin   password=admin123
-- Member login: username=member1 password=member123

-- ─── Users ───────────────────────────────────────────────────────────────────

INSERT INTO users (username, password, email, full_name, phone, address, role, is_blocked, created_at) VALUES
('admin',   '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Zxmy', 'admin@library.com',   'Library Administrator', '9999999999', 'Library Building, College Campus', 'ADMIN',  false, NOW()),
('member1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'alice@college.edu',   'Alice Johnson',         '9876543210', '12 MG Road, Pune',                'MEMBER', false, NOW()),
('member2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'bob@college.edu',     'Bob Smith',             '9123456780', '45 Park Street, Mumbai',          'MEMBER', false, NOW()),
('member3', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'charlie@college.edu', 'Charlie Brown',         '9001234567', '7 Lake View, Nashik',             'MEMBER', false, NOW()),
('member4', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'diana@college.edu',   'Diana Prince',          '9812345670', '22 Rose Garden, Nagpur',          'MEMBER', false, NOW()),
('member5', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'evan@college.edu',    'Evan Williams',         '9734561230', '55 Hill Top, Aurangabad',         'MEMBER', false, NOW());

-- ─── Books ───────────────────────────────────────────────────────────────────

INSERT INTO books (title, author, isbn, category, publisher, publication_year, total_copies, available_copies, shelf_location, created_at) VALUES
('The Great Gatsby',                'F. Scott Fitzgerald',  '978-0-7432-7356-5', 'Fiction',          'Scribner',              1925, 5, 4, 'A-101', NOW()),
('To Kill a Mockingbird',           'Harper Lee',           '978-0-0619-3534-6', 'Fiction',          'J. B. Lippincott & Co.',1960, 4, 3, 'A-102', NOW()),
('Introduction to Algorithms',      'Thomas H. Cormen',     '978-0-2620-3384-8', 'Computer Science', 'MIT Press',             2009, 6, 6, 'B-201', NOW()),
('Clean Code',                      'Robert C. Martin',     '978-0-1323-5088-4', 'Computer Science', 'Prentice Hall',         2008, 5, 4, 'B-202', NOW()),
('A Brief History of Time',         'Stephen Hawking',      '978-0-5530-5425-9', 'Science',          'Bantam Books',          1988, 3, 3, 'C-301', NOW()),
('The Alchemist',                   'Paulo Coelho',         '978-0-0617-2492-6', 'Fiction',          'HarperCollins',         1988, 7, 6, 'A-103', NOW()),
('Sapiens: A Brief History',        'Yuval Noah Harari',    '978-0-0624-1609-7', 'History',          'Harper',                2011, 4, 4, 'D-401', NOW()),
('Calculus: Early Transcendentals', 'James Stewart',        '978-1-2854-1552-1', 'Mathematics',      'Cengage Learning',      2015, 5, 5, 'E-501', NOW()),
('The Pragmatic Programmer',        'Andrew Hunt',          '978-0-1359-5705-9', 'Computer Science', 'Addison-Wesley',        1999, 3, 2, 'B-203', NOW()),
('Design Patterns',                 'Gang of Four',         '978-0-2016-3361-5', 'Computer Science', 'Addison-Wesley',        1994, 4, 4, 'B-204', NOW()),
('Thinking, Fast and Slow',         'Daniel Kahneman',      '978-0-3745-3355-7', 'Psychology',       'Farrar Straus Giroux',  2011, 3, 3, 'F-601', NOW()),
('Atomic Habits',                   'James Clear',          '978-0-7352-1129-2', 'Self-Help',        'Avery',                 2018, 6, 5, 'G-701', NOW()),
('1984',                            'George Orwell',        '978-0-4522-8423-4', 'Fiction',          'Secker & Warburg',      1949, 5, 5, 'A-104', NOW()),
('The Lean Startup',                'Eric Ries',            '978-0-3071-8789-5', 'Business',         'Crown Business',        2011, 3, 3, 'H-801', NOW()),
('Operating System Concepts',       'Abraham Silberschatz', '978-1-1186-3388-4', 'Computer Science', 'Wiley',                 2018, 5, 4, 'B-205', NOW()),
('Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', '978-0-4398-6809-9', 'Fiction',          'Scholastic',            1997, 8, 7, 'A-105', NOW()),
('The Psychology of Money',         'Morgan Housel',        '978-0-8573-7963-9', 'Business',         'Harriman House',        2020, 4, 4, 'H-802', NOW()),
('Ikigai',                          'Hector Garcia',        '978-0-1434-4462-1', 'Self-Help',        'Penguin Books',         2016, 5, 5, 'G-702', NOW()),
('The Art of War',                  'Sun Tzu',              '978-1-5995-0347-0', 'History',          'Filiquarian',           2006, 3, 3, 'D-402', NOW()),
('Deep Work',                       'Cal Newport',          '978-1-4555-8669-1', 'Self-Help',        'Grand Central',         2016, 4, 3, 'G-703', NOW());

-- ─── Transactions (sample issued/returned books) ──────────────────────────────

INSERT INTO transactions (user_id, book_id, issue_date, due_date, return_date, fine_amount, status, fine_paid) VALUES
(2, 1,  DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -6,  CURRENT_DATE), CURRENT_DATE,          0.00, 'RETURNED', false),
(2, 4,  DATEADD('DAY', -5,  CURRENT_DATE), DATEADD('DAY',  9,  CURRENT_DATE), NULL,                  0.00, 'ISSUED',   false),
(3, 2,  DATEADD('DAY', -10, CURRENT_DATE), DATEADD('DAY',  4,  CURRENT_DATE), NULL,                  0.00, 'ISSUED',   false),
(3, 6,  DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -16, CURRENT_DATE), DATEADD('DAY', -2, CURRENT_DATE), 70.00, 'RETURNED', false),
(4, 9,  DATEADD('DAY', -3,  CURRENT_DATE), DATEADD('DAY',  11, CURRENT_DATE), NULL,                  0.00, 'ISSUED',   false),
(5, 12, DATEADD('DAY', -8,  CURRENT_DATE), DATEADD('DAY',  6,  CURRENT_DATE), NULL,                  0.00, 'ISSUED',   false),
(6, 15, DATEADD('DAY', -25, CURRENT_DATE), DATEADD('DAY', -11, CURRENT_DATE), NULL,                  55.00,'OVERDUE',  false);
