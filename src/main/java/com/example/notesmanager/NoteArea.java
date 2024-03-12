package com.example.notesmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The NoteArea class represents the area where a note is displayed and edited.
 * It contains a Note object and an HTMLEditor object.
 */
public class NoteArea {

    // The Note object associated with this NoteArea.
    static Note note;

    // The Label object used to display the title note.
    static Label noteTitle;

    // The Label object used to display the note's labels.
    static Label noteLabels;

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
        noteTitle.setText(note.getNoteName());
        NoteArea.setLabelsText();
    }



    public static void setLabelsText() {

        ArrayList<NoteLabel> labels = note.getLabels();

        String allLabels = "";

        for (int i = 0; i < labels.size(); i++) {
            if (i == labels.size() - 1) allLabels += labels.get(i).getLabelName();
            else allLabels += labels.get(i).getLabelName() + " - ";
        }

        noteLabels.setText(allLabels);

        System.out.println("CALLING setLabelsText() :");
        System.out.println("Note ID : " + note.getNoteID());
        System.out.println("Note Name : " + note.getNoteName());
        System.out.println("Note Labels : " + allLabels);
    }

    public static Note getNote() {
        return note;
    }

    /**
     * This method is called when the main window is called the first time.
     *
     * @param noteArea                the HTMLEditor object
     * @param noteSelectedPane        the Pane object that display content
     * @param waitingNoteSelectedPane the Pane object that display waiting message
     */
    public static void setNoteArea(
            HTMLEditor noteArea,
            Pane noteSelectedPane,
            Pane waitingNoteSelectedPane,
            Label noteTitle,
            Label noteLables) {
        NoteArea.noteArea = noteArea;
        NoteArea.noteSelectedPane = noteSelectedPane;
        NoteArea.waitingNoteSelectedPane = waitingNoteSelectedPane;
        NoteArea.noteTitle = noteTitle;
        NoteArea.noteLabels = noteLables;

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