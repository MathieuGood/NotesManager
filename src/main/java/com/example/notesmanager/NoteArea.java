package com.example.notesmanager;

import javafx.scene.web.HTMLEditor;

/**
 * The NoteArea class represents the area where a note is displayed and edited.
 * It contains a Note object and an HTMLEditor object.
 */
public class NoteArea {

    // The Note object associated with this NoteArea.
    Note note;

    // The HTMLEditor object used to display and edit the note's content.
    HTMLEditor noteArea;

    /**
     * Constructs a new NoteArea object with the given Note and HTMLEditor.
     *
     * @param note     the Note object to be associated with this NoteArea
     * @param noteArea the HTMLEditor object to be used for displaying and editing the note's content
     */
    public NoteArea(Note note, HTMLEditor noteArea) {
        this.note = note;
        this.noteArea = noteArea;
        String noteContent = getContent();
        if (!noteContent.isEmpty()) {
            noteArea.setHtmlText(noteContent);
        }
    }

    /**
     * Returns the content of the note.
     *
     * @return the content of the note
     */
    public String getContent() {
        this.note.fetchNoteContent();
        System.out.println("NOTE CONTENT :");
        System.out.println(this.note.getNoteContent());
        return this.note.getNoteContent();
    }

    /**
     * Sets the content of the note to the given string and updates the HTMLEditor's content.
     *
     * @param newContent the new content for the note
     */
    public void setContent(String newContent) {
        try {
            note.editContent(newContent);
            System.out.println("Saved");
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
        this.noteArea.setHtmlText(newContent);
    }
}