package com.example.notesmanager;

import javafx.scene.web.HTMLEditor;

/**
 * The NoteArea class represents the area where a note is displayed and edited.
 * It contains a Note object and an HTMLEditor object.
 */
public class NoteArea {

    // The Note object associated with this NoteArea.
    static Note note;

    // The HTMLEditor object used to display and edit the note's content.
    static HTMLEditor noteArea;

    public static Note getNote() {
        return note;
    }

    public static void setContentInNoteArea(Note note) {
        NoteArea.note = note;
        note.fetchNoteContent();
        noteArea.setHtmlText(note.getNoteContent());
    }

    public static HTMLEditor getNoteArea() {
        return noteArea;
    }

    public static void setNoteArea(HTMLEditor noteArea) {
        NoteArea.noteArea = noteArea;
    }
}