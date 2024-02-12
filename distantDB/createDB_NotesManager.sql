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

CREATE TABLE users(
   user_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
   user_name VARCHAR(50),
   user_email VARCHAR(50),
   user_password VARCHAR(50),
   PRIMARY KEY(user_id),
   INDEX(user_email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

ALTER TABLE users
    ADD CONSTRAINT UC_user_email UNIQUE (user_email);

INSERT INTO users(
   user_name,
   user_email,
   user_password
) VALUES
   ('Mathieu', 'bon.mathieu@gmail.com', 'mathieubon'),
   ('Soundouce', 'soundouce.chibani@gmail.com', 'soundouce'),
   ('Youssef', 'you.moudni@gmail.com', 'youssef'),
   ('Belgrand', 'grmabele@gmail.com', 'belgrand'),
   ('Testeur', 'test@test.com', 'test');


CREATE TABLE colors(
   color_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
   color_name VARCHAR(50),
   PRIMARY KEY(color_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO colors(
   color_name
) VALUES
   ('red'),
   ('blue'),
   ('green'),
   ('yellow'),
   ('orange'),
   ('purple'),
   ('pink'),
   ('brown'),
   ('black'),
   ('white');


CREATE TABLE labels(
   label_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
   label_name VARCHAR(50),
   color_id INT UNSIGNED NOT NULL,
   PRIMARY KEY(label_id),
   FOREIGN KEY(color_id) REFERENCES colors(color_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO labels(
   label_name,
   color_id
) VALUES
   ('Important', 1),
   ('Urgent', 2),
   ('Work', 3),
   ('Personal', 4),
   ('School', 5),
   ('Home', 6),
   ('Fun', 7),
   ('Family', 8),
   ('Friends', 9);


CREATE TABLE binders(
   binder_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
   binder_name VARCHAR(50),
   user_id INT UNSIGNED NOT NULL,
   color_id INT UNSIGNED NOT NULL,
   PRIMARY KEY(binder_id),
   FOREIGN KEY(user_id) REFERENCES users(user_id),
   FOREIGN KEY(color_id) REFERENCES colors(color_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO binders(
   binder_name,
   user_id,
   color_id
) VALUES
   ('Cours SERFA', 1, 2),
   ('My First Binder', 2, 1),
   ('My First Binder', 3, 2),
   ('My First Binder', 4, 5),
   ('My First Binder', 5, 2);


CREATE TABLE tabs(
   tab_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
   tab_name VARCHAR(50),
   binder_id INT UNSIGNED NOT NULL,
   color_id INT UNSIGNED NOT NULL,
   PRIMARY KEY(tab_id),
   FOREIGN KEY(binder_id) REFERENCES binders(binder_id),
   FOREIGN KEY(color_id) REFERENCES colors(color_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO tabs(
   tab_name,
   binder_id,
   color_id
) VALUES
   ('Java', 1, 2),
   ('React Native', 1, 3),
   ('SQL', 1, 4),
   ('My First Tab', 2, 1),
   ('My First Tab', 3, 2),
   ('My First Tab', 4, 5),
   ('My First Tab', 5, 2);


CREATE TABLE notes(
   note_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
   note_name VARCHAR(50),
   note_content TEXT,
   tab_id INT UNSIGNED NOT NULL,
   PRIMARY KEY(note_id),
   FOREIGN KEY(tab_id) REFERENCES tabs(tab_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO notes(
   note_name,
   note_content,
   tab_id
) VALUES
   ('Syntax', 'Print a statement : System.out.println("This is a statement")', 1),
   ('Create a class', 'Do not forget to add the constructor.', 1),
   ('React Native Introduction', 'React Native is a framework for building native apps using React.', 2),
   ('SQL Introduction', 'SQL is a standard language for storing, manipulating and retrieving data in databases.', 3),
   ('My First Note', 'This is my first note.', 4),
   ('My First Note', 'This is my first note.', 5),
   ('My First Note', 'This is my first note.', 6),
   ('My First Note', 'This is my first note.', 7);


CREATE TABLE tag(
   note_id INT UNSIGNED NOT NULL,
   label_id INT UNSIGNED NOT NULL,
   PRIMARY KEY(note_id, label_id),
   FOREIGN KEY(note_id) REFERENCES notes(note_id),
   FOREIGN KEY(label_id) REFERENCES labels(label_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO tag(
   note_id,
   label_id
) VALUES
   (1, 1),
   (2, 2);


-- Create a view to get all notes from all users
CREATE VIEW viewAllUserNotes AS
    SELECT binder_name, tab_name, note_name, note_content
    FROM notes
        INNER JOIN tabs ON notes.tab_id = tabs.tab_id
        INNER JOIN binders ON tabs.binder_id = binders.binder_id
        INNER JOIN users ON binders.user_id = users.user_id;



COMMIT;
SET AUTOCOMMIT = 1;