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

    /**
     * Constructs a new NoteArea object with the given Note and HTMLEditor.
     *
     * @param note     the Note object to be associated with this NoteArea
     * @param noteArea the HTMLEditor object to be used for displaying and editing the note's content
     */
//    public static NoteArea(Note note, HTMLEditor noteArea) {
//        this.note = note;
//        this.noteArea = noteArea;
//        String noteContent = getContent();
//        if (!noteContent.isEmpty()) {
//            noteArea.setHtmlText(noteContent + "<br>" + note.getNoteLabel1() + "<br>" + note.getNoteLabel2());
//        }
//    }

    public static Note getNote() {
        return note;
    }

    public static void setNote(Note note) {
        NoteArea.note = note;
        note.fetchNoteContent();
        setContent(note.getNoteContent());
//        setContent();
    }

    public static HTMLEditor getNoteArea() {
        return noteArea;
    }

    public static void setNoteArea(HTMLEditor noteArea) {
        NoteArea.noteArea = noteArea;
    }

    /**
     * Returns the content of the note.
     *
     * @return the content of the note
     */
    public static String getContent() {
        note.fetchNoteContent();
        System.out.println("NOTE CONTENT :");
        System.out.println(note.getNoteContent());
        return note.getNoteContent();
    }

    /**
     * Sets the content of the note to the given string and updates the HTMLEditor's content.
     *
     * @param newContent the new content for the note
     */
    public static void setContent(String newContent) {
//        try {
//            note.editContent(newContent);
//            System.out.println("Saved");
//        } catch (Exception ex) {
//            System.out.println("Error : " + ex);
//        }
        noteArea.setHtmlText(newContent);
    }
}