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
       ('Testeur', 'test@test.com', 'Testeur*1')
;


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
VALUES ('Rouge', '#FF0000'),
       ('Bleu', '#0000FF'),
       ('Vert', '#008000'),
       ('Jaune', '#FFFF00'),
       ('Orange', '#FFA500'),
       ('Violet', '#800080'),
       ('Rose', '#FFC0CB'),
       ('Marron', '#A52A2A'),
       ('Noir', '#000000'),
       ('Blanc', '#FFFFFF');


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
VALUES ('SERFA', 1, 2),
       ('Livres', 1, 5)
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
       ('Python', 1, 9),
       ('Histoire de France', 2, 1),
       ('Fantastique', 2, 2),
       ('Biographies', 2, 8)
;


CREATE TABLE notes
(
    note_id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    note_name      VARCHAR(50),
    note_content   LONGTEXT     DEFAULT "",
    tab_id         INT UNSIGNED NOT NULL,
    note_label1_id INT UNSIGNED DEFAULT NULL,
    note_label2_id INT UNSIGNED DEFAULT NULL,
    PRIMARY KEY (note_id),
    FOREIGN KEY (tab_id) REFERENCES tabs (tab_id)
        ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO notes (note_name, note_content, tab_id)
VALUES
    -- Notes pour l'onglet Java
    ('Bases de Java', 'Apprenez les variables, les types de données et les opérateurs en Java.', 1),
    ('Concepts de POO en Java',
     'Comprenez des concepts comme les classes, les objets, l''héritage et le polymorphisme en Java.', 1),
    ('Collections en Java', 'Explorez le framework des collections Java, y compris ArrayList, HashMap et LinkedList.',
     1),
    ('Gestion des exceptions', 'Étudiez comment gérer les exceptions en Java en utilisant des blocs try-catch.', 1),
    ('Gestion des fichiers',
     'Apprenez à lire depuis et à écrire dans des fichiers en Java en utilisant FileReader et FileWriter.', 1),
    ('Multithreading', 'Comprenez les bases du multithreading et de la synchronisation en Java.', 1),

    -- Notes pour l'onglet React Native
    ('Configuration de React Native',
     'Suivez la documentation de React Native pour configurer votre environnement de développement.', 2),
    ('Composants de React Native', 'Comprenez les composants de base tels que View, Text et Image en React Native.', 2),
    ('Navigation avec React',
     'Apprenez à implémenter la navigation en React Native en utilisant la bibliothèque React Navigation.', 2),
    ('Gestion d''état', 'Explorez les options de gestion d''état en React Native, y compris useState et useContext.',
     2),
    ('Intégration de Redux', 'Intégrez Redux dans votre application React Native pour une gestion d''état centralisée.',
     2),
    ('Stylisation en React Native',
     'Apprenez différentes méthodes pour styliser des composants en React Native en utilisant StyleSheet.', 2),

    -- Notes pour l'onglet SQL
    ('Requêtes SQL', 'Pratiquez l''écriture de requêtes SQL SELECT pour récupérer des données d''une base de données.',
     3),
    ('Joins SQL', 'Comprenez les différents types de joints SQL tels que INNER JOIN, LEFT JOIN et RIGHT JOIN.', 3),
    ('Commandes DDL SQL',
     'Apprenez les commandes du langage de définition de données (DDL) telles que CREATE, ALTER et DROP.', 3),
    ('Commandes DML SQL',
     'Explorez les commandes du langage de manipulation de données (DML) telles que INSERT, UPDATE et DELETE.', 3),
    ('Normalisation de base de données',
     'Comprenez le processus de normalisation pour organiser les données dans une base de données relationnelle.', 3),
    ('Transactions en SQL',
     'Étudiez les transactions et leur importance dans le maintien de l''intégrité des données en SQL.', 3),

    -- Notes pour l'onglet Python
    ('Bases de Python',
     'Apprenez les bases de Python, y compris les variables, les types de données et les opérateurs.',
     4),
    ('Structures de contrôle',
     'Explorez les structures de contrôle en Python, telles que les boucles for, les boucles while et les instructions if.',
     4),
    ('Fonctions en Python',
     'Comprenez comment définir et appeler des fonctions en Python, ainsi que les arguments et les valeurs de retour.',
     4),
    ('Modules et packages',
     'Découvrez comment organiser votre code en utilisant des modules et des packages en Python.', 4),
    ('Gestion des erreurs',
     'Apprenez à gérer les erreurs et les exceptions en Python en utilisant des blocs try-except.', 4),
    ('Manipulation de fichiers',
     'Explorez les opérations de lecture et d''écriture de fichiers en Python en utilisant les fonctions open et close.',
     4),

    -- Notes pour l'onglet Histoire de France
    ('Révolution française',
     'Découvrez les événements clés de la Révolution française, y compris la prise de la Bastille et la Terreur.', 5),
    ('Napoléon Bonaparte',
     'Apprenez-en plus sur la vie et les réalisations de Napoléon Bonaparte, empereur des Français.', 5),
    ('Guerre mondiale',
     'Explorez les causes et les conséquences de la Première et de la Seconde Guerre mondiale.', 5),
    ('Renaissance française',
     'Découvrez la période de la Renaissance en France et son impact sur l''art, la science et la culture.', 5),

    -- Notes pour l'onglet Fantastique
    ('Créatures mythiques',
     'Explorez les différentes créatures mythiques de la littérature fantastique, y compris les dragons et les licornes.',
     6),
    ('Monde imaginaire',
     'Découvrez des mondes imaginaires créés par des auteurs de fantasy tels que J.R.R. Tolkien et George R.R. Martin.',
     6),
    ('Magie et sorcellerie',
     'Étudiez les concepts de magie et de sorcellerie dans la littérature fantastique et les mythes anciens.', 6),
    ('Héros et héroïnes',
     'Découvrez des héros et héroïnes emblématiques de la littérature fantastique, tels que Harry Potter et Frodo Baggins.',
     6),

    -- Notes pour l'onglet Biographies
    ('Albert Einstein', 'Découvrez la vie et les réalisations du célèbre physicien Albert Einstein.', 7),
    ('Marie Curie', 'Apprenez-en plus sur la vie et les contributions de la scientifique Marie Curie.', 7),
    ('Leonardo da Vinci', 'Explorez les œuvres et les inventions du polymathe italien Leonardo da Vinci.', 7)
;


UPDATE notes
SET note_label1_id = 1
    WHERE note_id = 1;

UPDATE notes
SET note_label1_id = 5
WHERE note_id IN (4, 5, 10, 13);

UPDATE notes
SET note_label1_id = 2
WHERE note_id IN (2, 6, 8, 11, 14);


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
       ('Travail'),
       ('Personnel'),
       ('À réviser'),
       ('Maison'),
       ('Loisirs'),
       ('Famille'),
       ('Amis')
;


-- Add unique constraints on binders, tabs and notes relatively to their user
ALTER TABLE binders
    ADD CONSTRAINT UC_user_binder_name UNIQUE (user_id, binder_name);
ALTER TABLE tabs
    ADD CONSTRAINT UC_binder_tab_name UNIQUE (binder_id, tab_name);
ALTER TABLE notes
    ADD CONSTRAINT UC_tab_note_name UNIQUE (tab_id, note_name),
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