CREATE TABLE users(
   user_id INT,
   user_name VARCHAR(50),
   user_email VARCHAR(50),
   user_password VARCHAR(50),
   PRIMARY KEY(user_id)
);

CREATE TABLE colors(
   color_id INT,
   color_name VARCHAR(50),
   PRIMARY KEY(color_id)
);

CREATE TABLE labels(
   label_id INT,
   label_name VARCHAR(50),
   color_id INT NOT NULL,
   PRIMARY KEY(label_id),
   FOREIGN KEY(color_id) REFERENCES colors(color_id)
);

CREATE TABLE binders(
   binder_id INT,
   binder_name VARCHAR(50),
   user_id INT NOT NULL,
   color_id INT NOT NULL,
   PRIMARY KEY(binder_id),
   FOREIGN KEY(user_id) REFERENCES users(user_id),
   FOREIGN KEY(color_id) REFERENCES colors(color_id)
);

CREATE TABLE tabs(
   binder_id LOGICAL,
   tab_name VARCHAR(50),
   binder_id_1 INT NOT NULL,
   color_id INT NOT NULL,
   PRIMARY KEY(binder_id),
   FOREIGN KEY(binder_id_1) REFERENCES binders(binder_id),
   FOREIGN KEY(color_id) REFERENCES colors(color_id)
);

CREATE TABLE notes(
   note_id INT,
   note_name VARCHAR(50),
   note_content TEXT,
   binder_id LOGICAL NOT NULL,
   PRIMARY KEY(note_id),
   FOREIGN KEY(binder_id) REFERENCES tabs(binder_id)
);

CREATE TABLE tag(
   note_id INT,
   label_id INT,
   PRIMARY KEY(note_id, label_id),
   FOREIGN KEY(note_id) REFERENCES notes(note_id),
   FOREIGN KEY(label_id) REFERENCES labels(label_id)
);
