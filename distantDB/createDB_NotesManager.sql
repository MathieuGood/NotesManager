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
       ('Python', 1, 9)
;


CREATE TABLE notes
(
    note_id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    note_name      VARCHAR(50),
    note_content   LONGTEXT DEFAULT "",
    tab_id         INT UNSIGNED NOT NULL,
    note_label1_id INT UNSIGNED DEFAULT NULL,
    note_label2_id INT UNSIGNED DEFAULT NULL,
    PRIMARY KEY (note_id),
    FOREIGN KEY (tab_id) REFERENCES tabs (tab_id)
        ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO notes(note_name, note_content, tab_id)
VALUES
    -- Notes for Java tab
    ('Java Basics', 'Learn about variables, data types, and operators in Java.', 1),
    ('Java OOP Concepts', 'Understand concepts like classes, objects, inheritance, and polymorphism.', 1),
    ('Java Collections', 'Explore Java collections framework including ArrayList, HashMap, and LinkedList.', 1),
    ('Exception Handling', 'Study how to handle exceptions in Java using try-catch blocks.', 1),
    ('File Handling', 'Learn to read from and write to files in Java using FileReader and FileWriter.', 1),
    ('Multithreading', 'Understand the basics of multithreading and synchronization in Java.', 1),

    -- Notes for React Native tab
    ('React Native Setup', 'Follow the React Native documentation to set up your development environment.', 2),
    ('React Native Components', 'Understand basic components like View, Text, and Image in React Native.', 2),
    ('React Navigation', 'Learn how to implement navigation in React Native using React Navigation library.', 2),
    ('State Management', 'Explore state management options in React Native including useState and useContext.', 2),
    ('Redux Integration', 'Integrate Redux into your React Native app for centralized state management.', 2),
    ('Styling in React Native', 'Learn different methods to style components in React Native using StyleSheet.', 2),

    -- Notes for SQL tab
    ('SQL Queries', 'Practice writing SQL SELECT queries to retrieve data from a database.', 3),
    ('SQL Joins', 'Understand different types of SQL joins like INNER JOIN, LEFT JOIN, and RIGHT JOIN.', 3),
    ('SQL DDL Commands', 'Learn about Data Definition Language commands like CREATE, ALTER, and DROP.', 3),
    ('SQL DML Commands', 'Explore Data Manipulation Language commands like INSERT, UPDATE, and DELETE.', 3),
    ('Database Normalization', 'Understand the normalization process to organize data in a relational database.', 3),
    ('Transactions in SQL', 'Study transactions and their importance in maintaining data integrity in SQL.', 3)
;

UPDATE notes
SET note_label1_id = 2,
    note_label2_id = 4
WHERE note_id = 1;


CREATE TABLE labels
(
    label_id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    label_name VARCHAR(50),
    PRIMARY KEY (label_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO labels(label_name)
VALUES ('Important'),
       ('Urgent'),
       ('Work'),
       ('Personal'),
       ('School'),
       ('Home'),
       ('Fun'),
       ('Family'),
       ('Friends')
;


-- Add unique constraints on binders, tabs and notes relatively to their user
ALTER TABLE binders
    ADD CONSTRAINT UC_user_binder_name UNIQUE (user_id, binder_name);
ALTER TABLE tabs
    ADD CONSTRAINT UC_binder_tab_name UNIQUE (binder_id, tab_name);
ALTER TABLE notes
    ADD CONSTRAINT UC_tab_note_name UNIQUE (tab_id, note_name)
    ,
    ADD CONSTRAINT FK_note_label1 FOREIGN KEY (note_label1_id) REFERENCES labels (label_id)
        ON DELETE SET NULL,
    ADD CONSTRAINT FK_note_label2 FOREIGN KEY (note_label2_id) REFERENCES labels (label_id)
        ON DELETE SET NULL
;
ALTER TABLE labels
    ADD CONSTRAINT UC_label_name UNIQUE (label_name);


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
       notes.note_label1_id,
       notes.note_label2_id
FROM binders
         LEFT JOIN users ON binders.user_id = users.user_id
         LEFT JOIN tabs ON tabs.binder_id = binders.binder_id
         LEFT JOIN notes ON notes.tab_id = tabs.tab_id
;


-- Create a stored procedure to update the label of a note to NULL, leaving the other label unchanged, dynamically finding the right column to update
DELIMITER //

CREATE PROCEDURE UpdateNoteLabelToNull(IN input_label_id INT, IN input_note_id INT, OUT success INT)
BEGIN
    DECLARE label1_value INT;
    DECLARE label2_value INT;
    DECLARE label_found BOOLEAN DEFAULT FALSE;

    -- Get the current values of note_label1_id and note_label2_id for the given note_id
    SELECT note_label1_id, note_label2_id
    INTO label1_value, label2_value
    FROM notes
    WHERE note_id = input_note_id
    LIMIT 1;

    -- Check if input_label_id matches either note_label1_id or note_label2_id
    IF label1_value = input_label_id THEN
        UPDATE notes SET note_label1_id = NULL WHERE note_id = input_note_id;
        SET label_found = TRUE;
    ELSEIF label2_value = input_label_id THEN
        UPDATE notes SET note_label2_id = NULL WHERE note_id = input_note_id;
        SET label_found = TRUE;
    END IF;

    -- Set success based on whether the label was found and updated
    IF label_found THEN
        SET success = 1;
    ELSE
        SET success = 0;
    END IF;
END //

DELIMITER ;


-- Create a stored procedure to update the label of a note to the inputted value, leaving the other label unchanged, dynamically finding the right column to update (column with NULL value)
DELIMITER //

CREATE PROCEDURE UpdateNoteLabelToNewValue(IN input_label_id INT, IN input_note_id INT, OUT success INT)
BEGIN
    DECLARE label1_value INT;
    DECLARE label2_value INT;
    DECLARE label_found BOOLEAN DEFAULT FALSE;

    -- Get the current values of note_label1_id and note_label2_id for the given note_id
    SELECT note_label1_id, note_label2_id
    INTO label1_value, label2_value
    FROM notes
    WHERE note_id = input_note_id
    LIMIT 1;

    -- Check if input_label_id matches either note_label1_id or note_label2_id
    IF label1_value IS NULL THEN
        UPDATE notes SET note_label1_id = input_label_id WHERE note_id = input_note_id;
        SET label_found = TRUE;
    ELSEIF label2_value IS NULL THEN
        UPDATE notes SET note_label2_id = input_label_id WHERE note_id = input_note_id;
        SET label_found = TRUE;
    END IF;

    -- Set success based on whether the label was found and updated
    IF label_found THEN
        SET success = 1;
    ELSE
        SET success = 0;
    END IF;
END //

DELIMITER ;


COMMIT;
SET AUTOCOMMIT = 1;