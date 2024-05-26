-- Delete all rows from all tables
DELETE FROM PARTICIPATE;
DELETE FROM SESSIONS;
DELETE FROM TEACHERS;
DELETE FROM USERS;

-- Reset the auto-incrementing primary key value
ALTER TABLE PARTICIPATE AUTO_INCREMENT = 1;
ALTER TABLE SESSIONS AUTO_INCREMENT = 1;
ALTER TABLE TEACHERS AUTO_INCREMENT = 1;
ALTER TABLE USERS AUTO_INCREMENT = 1;

-- Insert data into TEACHERS table
INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Teacher One', 'Teacher One'),
       ('Teacher Two', 'Teacher Two');

-- Insert data into SESSIONS table
INSERT INTO SESSIONS (name, description, `date`, teacher_id, created_at, updated_at)
VALUES ('Yoga1', 'Yoga session 1', '2023-05-01 09:00:00', 1, '2023-04-01 08:00:00', '2023-04-01 08:30:00'),
       ('Yoga2', 'Yoga session 2', '2023-06-01 10:00:00', 2, '2023-05-01 09:00:00', '2023-05-01 09:30:00');

-- Insert data into USERS table
INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('admin', 'admin lastname', true, 'admin@example.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       ('test', 'test lastname', false, 'test@example.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

-- Insert data into PARTICIPATE table
INSERT INTO PARTICIPATE (user_id,session_id) VALUES (1,1);