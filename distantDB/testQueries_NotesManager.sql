SELECT binders.binder_id,
       binders.binder_name,
       binders.binder_color_id,
       tabs.tab_id,
       tabs.tab_name,
       tabs.tab_color_id,
       notes.note_id,
       notes.note_name,
       notes.note_color_id
FROM binders
         LEFT JOIN users ON binders.user_id = users.user_id
         LEFT JOIN tabs ON tabs.binder_id = binders.binder_id
         LEFT JOIN notes ON notes.tab_id = tabs.tab_id
WHERE users.user_id = 1
;

