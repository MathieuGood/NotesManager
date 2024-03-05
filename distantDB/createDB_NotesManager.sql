SET AUTOCOMMIT = 0;
START TRANSACTION;


-- --------------------------------------------------------------------------------------
--
-- Database creation
--
-- --------------------------------------------------------------------------------------

DROP DATABASE
    IF EXISTS NotesManager;

CREATE DATABASE NotesManager
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE NotesManager;


-- --------------------------------------------------------------------------------------
--
-- Tables
--
-- --------------------------------------------------------------------------------------

CREATE TABLE users
(
    user_id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_name     VARCHAR(50),
    user_email    VARCHAR(50),
    user_password VARCHAR(50),
    PRIMARY KEY (user_id),
    INDEX (user_email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE users
    ADD CONSTRAINT UC_user_email UNIQUE (user_email);

INSERT INTO users(user_name,
                  user_email,
                  user_password)
VALUES ('Mathieu', 'bon.mathieu@gmail.com', 'Mathieu*1'),
       ('Soundouce', 'soundouce.chibani@gmail.com', 'Soundouce*1'),
       ('Youssef', 'you.moudni@gmail.com', 'Youssef*1'),
       ('Belgrand', 'grmabele@gmail.com', 'Belgrand*1'),
       ('Testeur', 'test@test.com', 'Testeur*1'),
       ('a', 'a@a.com', 'Aaaaaa*1');


CREATE TABLE colors
(
    color_id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    color_name VARCHAR(50),
    color_hex  VARCHAR(7),
    PRIMARY KEY (color_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO colors(color_name,
                   color_hex)
VALUES ('red', '#FF0000'),
       ('blue', '#0000FF'),
       ('green', '#008000'),
       ('yellow', '#FFFF00'),
       ('orange', '#FFA500'),
       ('purple', '#800080'),
       ('pink', '#FFC0CB'),
       ('brown', '#A52A2A'),
       ('black', '#000000'),
       ('white', '#FFFFFF');


CREATE TABLE binders
(
    binder_id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    binder_name     VARCHAR(50),
    user_id         INT UNSIGNED NOT NULL,
    binder_color_id INT UNSIGNED,
    PRIMARY KEY (binder_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
        ON DELETE CASCADE,
    FOREIGN KEY (binder_color_id) REFERENCES colors (color_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO binders(binder_name,
                    user_id,
                    binder_color_id)
VALUES ('Cours SERFA', 1, 2)
;


CREATE TABLE tabs
(
    tab_id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    tab_name     VARCHAR(50),
    binder_id    INT UNSIGNED NOT NULL,
    tab_color_id INT UNSIGNED,
    PRIMARY KEY (tab_id),
    FOREIGN KEY (binder_id) REFERENCES binders (binder_id)
        ON DELETE CASCADE,
    FOREIGN KEY (tab_color_id) REFERENCES colors (color_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO tabs(tab_name,
                 binder_id,
                 tab_color_id)
VALUES ('Java', 1, 2),
       ('React Native', 1, 3),
       ('SQL', 1, 4),
       ('HTML', 1, 5),
       ('CSS', 1, 6),
       ('JavaScript', 1, 7),
       ('PHP', 1, 8),
       ('Python', 1, 9),
       ('C++', 1, 10),
       ('C#', 1, 1),
       ('Ruby', 1, 2),
       ('Swift', 1, 3),
       ('Kotlin', 1, 4)
;


CREATE TABLE notes
(
    note_id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    note_name      VARCHAR(50),
    note_color_id  INT UNSIGNED,
    note_content   LONGTEXT,
    tab_id         INT UNSIGNED NOT NULL,
    note_label1_id INT DEFAULT NULL,
    note_label2_id INT DEFAULT NULL,
    PRIMARY KEY (note_id),
    FOREIGN KEY (tab_id) REFERENCES tabs (tab_id)
        ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO notes(note_name, note_color_id, note_content, tab_id)
VALUES
    -- Notes for Java tab
    ('Java Basics', 7, 'Learn about variables, data types, and operators in Java.', 1),
    ('Java OOP Concepts', 7, 'Understand concepts like classes, objects, inheritance, and polymorphism.', 1),
    ('Java Collections', 7, 'Explore Java collections framework including ArrayList, HashMap, and LinkedList.', 1),
    ('Exception Handling', 7, 'Study how to handle exceptions in Java using try-catch blocks.', 1),
    ('File Handling', 7, 'Learn to read from and write to files in Java using FileReader and FileWriter.', 1),
    ('Multithreading', 7, 'Understand the basics of multithreading and synchronization in Java.', 1),

    -- Notes for React Native tab
    ('React Native Setup', 4, 'Follow the React Native documentation to set up your development environment.', 2),
    ('React Native Components', 4, 'Understand basic components like View, Text, and Image in React Native.', 2),
    ('React Navigation', 4, 'Learn how to implement navigation in React Native using React Navigation library.', 2),
    ('State Management', 4, 'Explore state management options in React Native including useState and useContext.', 2),
    ('Redux Integration', 4, 'Integrate Redux into your React Native app for centralized state management.', 2),
    ('Styling in React Native', 4, 'Learn different methods to style components in React Native using StyleSheet.', 2),

    -- Notes for SQL tab
    ('SQL Queries', 5, 'Practice writing SQL SELECT queries to retrieve data from a database.', 3),
    ('SQL Joins', 5, 'Understand different types of SQL joins like INNER JOIN, LEFT JOIN, and RIGHT JOIN.', 3),
    ('SQL DDL Commands', 5, 'Learn about Data Definition Language commands like CREATE, ALTER, and DROP.', 3),
    ('SQL DML Commands', 5, 'Explore Data Manipulation Language commands like INSERT, UPDATE, and DELETE.', 3),
    ('Database Normalization', 5, 'Understand the normalization process to organize data in a relational database.', 3),
    ('Transactions in SQL', 5, 'Study transactions and their importance in maintaining data integrity in SQL.', 3)
;

UPDATE notes
SET note_label1_id = 2,
    note_label2_id=4
WHERE note_id = 1;


CREATE TABLE labels
(
    label_id       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    label_name     VARCHAR(50),
    label_color_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (label_id),
    FOREIGN KEY (label_color_id) REFERENCES colors (color_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO labels(label_name,
                   label_color_id)
VALUES ('Important', 1),
       ('Urgent', 2),
       ('Work', 3),
       ('Personal', 4),
       ('School', 5),
       ('Home', 6),
       ('Fun', 7),
       ('Family', 8),
       ('Friends', 9)
;


-- Create a view to get all notes from all users
CREATE VIEW viewAllUserNotes AS
SELECT binders.binder_id,
       binders.binder_name,
       binders.binder_color_id,
       tabs.tab_id,
       tabs.tab_name,
       tabs.tab_color_id,
       notes.note_id,
       notes.note_name,
       notes.note_color_id,
       notes.note_label1_id,
       notes.note_label2_id
FROM binders
         LEFT JOIN users ON binders.user_id = users.user_id
         LEFT JOIN tabs ON tabs.binder_id = binders.binder_id
         LEFT JOIN notes ON notes.tab_id = tabs.tab_id
;



COMMIT;
SET AUTOCOMMIT = 1;