-- Get all notes from user 1
SELECT binder_name, tab_name, note_name, note_content
    FROM notes 
        INNER JOIN tabs ON notes.tab_id = tabs.tab_id
        INNER JOIN binders ON tabs.binder_id = binders.binder_id
        INNER JOIN users ON binders.user_id = users.user_id
        	WHERE users.user_id = 1;

-- Create a view to get all notes from all users
CREATE VIEW viewAllUserNotes AS
    SELECT binder_name, tab_name, note_name, note_content
    FROM notes 
        INNER JOIN tabs ON notes.tab_id = tabs.tab_id
        INNER JOIN binders ON tabs.binder_id = binders.binder_id
        INNER JOIN users ON binders.user_id = users.user_id;
