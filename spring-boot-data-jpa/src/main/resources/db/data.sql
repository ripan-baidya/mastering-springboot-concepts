-- ===============================================
-- 1️⃣ PARENTS
-- ===============================================
INSERT INTO parents (id, father_name, mother_name) VALUES
(1, 'John Doe', 'Jane Doe'),
(2, 'Robert Smith', 'Mary Smith'),
(3, 'Michael Johnson', 'Linda Johnson'),
(4, 'William Brown', 'Patricia Brown'),
(5, 'James Davis', 'Barbara Davis'),
(6, 'Charles Wilson', 'Jennifer Wilson'),
(7, 'George Moore', 'Elizabeth Moore'),
(8, 'Thomas Taylor', 'Susan Taylor'),
(9, 'Daniel Anderson', 'Karen Anderson'),
(10, 'Matthew Thomas', 'Nancy Thomas');

-- ===============================================
-- 2️⃣ COURSES
-- ===============================================
INSERT INTO courses (course_id, course_name) VALUES
(1, 'Java'),
(2, 'Spring Boot'),
(3, 'React'),
(4, 'Database Systems'),
(5, 'Python');

-- ===============================================
-- 3️⃣ STUDENTS (1:1 relationship with Parent)
-- ===============================================
INSERT INTO students (student_id, full_name, email, phone_number, date_of_birth, status, created_at, updated_at, parent_id)
VALUES
(1, 'Ripan Baidya', 'ripan1@example.com', '9876543210', '2000-01-01 00:00:00', 'ACTIVE', NOW(), NOW(), 1),
(2, 'Alice Smith', 'alice@example.com', '9876543211', '2000-02-02 00:00:00', 'ACTIVE', NOW(), NOW(), 2),
(3, 'Bob Johnson', 'bob@example.com', '9876543212', '2000-03-03 00:00:00', 'ACTIVE', NOW(), NOW(), 3),
(4, 'Charlie Brown', 'charlie@example.com', '9876543213', '2000-04-04 00:00:00', 'ACTIVE', NOW(), NOW(), 4),
(5, 'Diana Davis', 'diana@example.com', '9876543214', '2000-05-05 00:00:00', 'ACTIVE', NOW(), NOW(), 5),
(6, 'Edward Wilson', 'edward@example.com', '9876543215', '2000-06-06 00:00:00', 'ACTIVE', NOW(), NOW(), 6),
(7, 'Fiona Moore', 'fiona@example.com', '9876543216', '2000-07-07 00:00:00', 'ACTIVE', NOW(), NOW(), 7),
(8, 'George Taylor', 'george@example.com', '9876543217', '2000-08-08 00:00:00', 'ACTIVE', NOW(), NOW(), 8),
(9, 'Hannah Anderson', 'hannah@example.com', '9876543218', '2000-09-09 00:00:00', 'ACTIVE', NOW(), NOW(), 9),
(10, 'Ian Thomas', 'ian@example.com', '9876543219', '2000-10-10 00:00:00', 'ACTIVE', NOW(), NOW(), 10);

-- ===============================================
-- 4️⃣ GRADES (One-to-Many with Student)
-- ===============================================
INSERT INTO grades (id, subject, score, student_id) VALUES
(1, 'Math', 95, 1),
(2, 'English', 88, 1),
(3, 'Math', 75, 2),
(4, 'English', 82, 2),
(5, 'Math', 90, 3),
(6, 'English', 85, 3),
(7, 'Math', 70, 4),
(8, 'English', 80, 4),
(9, 'Math', 92, 5),
(10, 'English', 87, 5),
(11, 'Math', 78, 6),
(12, 'English', 81, 6),
(13, 'Math', 88, 7),
(14, 'English', 90, 7),
(15, 'Math', 85, 8),
(16, 'English', 83, 8),
(17, 'Math', 91, 9),
(18, 'English', 89, 9),
(19, 'Math', 77, 10),
(20, 'English', 80, 10);

-- ===============================================
-- 5️⃣ STUDENT_COURSE (Many-to-Many Join Table)
-- ===============================================
INSERT INTO student_course (student_id, course_id) VALUES
(1, 1), (1, 2), (1, 4),
(2, 1), (2, 3), (2, 5),
(3, 2), (3, 3),
(4, 1), (4, 4),
(5, 2), (5, 5),
(6, 3), (6, 4),
(7, 1), (7, 2), (7, 3),
(8, 4), (8, 5),
(9, 1), (9, 5),
(10, 2), (10, 3);
