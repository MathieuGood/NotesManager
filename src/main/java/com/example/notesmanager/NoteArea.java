package com.example.notesmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;

/**
 * The NoteArea class represents the area where a note is displayed and edited.
 * It contains a Note object and an HTMLEditor object.
 */
public class NoteArea {

    // The Note object associated with this NoteArea.
    static Note note;

    // The Label object used to display and edit the title note.
    static Label noteTitle;

    // The HTMLEditor object used to display and edit the note's content.
    static HTMLEditor noteArea;

    // The Pane object used to display all elements.
    @FXML
    private static Pane noteSelectedPane;

    // The Pane object used to display waiting message.
    @FXML
    private static Pane waitingNoteSelectedPane;

    public static void setContentInNoteArea(Note note) {
        NoteArea.note = note;
        note.fetchNoteContent();
        noteArea.setHtmlText(note.getNoteContent());
    }


    /**
     * This method is called when the main window is called the first time.
     *
     * @param noteArea the HTMLEditor object
     * @param noteSelectedPane the Pane object that display content
     * @param waitingNoteSelectedPane the Pane object that display waiting message
     */
    public static void setNoteArea(
            HTMLEditor noteArea,
            Pane noteSelectedPane,
            Pane waitingNoteSelectedPane,
            Label noteTitle)
    {
        NoteArea.noteArea = noteArea;
        NoteArea.noteSelectedPane = noteSelectedPane;
        NoteArea.waitingNoteSelectedPane = waitingNoteSelectedPane;
        NoteArea.noteTitle = noteTitle;

        noteSelectedPane.setVisible(false);
        waitingNoteSelectedPane.setVisible(true);
    }


    /**
     * This method is called when a note is clicked in order to display its content.
     */
    public static void setPaneNoteContentVisible() {
        noteSelectedPane.setVisible(true);
        waitingNoteSelectedPane.setVisible(false);
    }


    /**
     * This method is used to verify whether the content pane is currently visible.
     */
    public static boolean getNoteSelectedPaneStatus() {
        return noteSelectedPane.isVisible();
    }
}