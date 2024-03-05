package com.example.notesmanager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Note class represents a note in a tab in a note-taking application.
 * It contains methods to get and set the note's properties, and to interact with the database.
 */
public class Note {


    // The unique identifier for this Note.
    private final int noteID;

    // The unique identifier for the Tab that this Note belongs to.
    private final int tabID;

    // The name of this Note.
    private String noteName;

    // The content of this Note.
    private String noteContent;

    private String noteLabel1;
    private String noteLabel2;



    public Note(
            Tab tab,
            int noteID,
            String noteName,
            String noteLabel1,
            String noteLabel2
    ) {
        this.noteID = noteID;
        this.tabID = tab.getTabID();
        this.noteName = noteName;
        this.noteLabel1 = noteLabel1;
        this.noteLabel2 = noteLabel2;
    }


    /**
     * This constructor creates a new Note object with a given Tab, noteID, noteName, and noteContent.
     * The noteID, noteName, and noteContent are directly assigned, while the tabID is retrieved from the given Tab object.
     *
     * @param tab         The Tab object that this Note belongs to.
     * @param noteID      The unique identifier for this Note.
     * @param noteName    The name of this Note.
     * @param noteContent The content of this Note.
     */
    public Note(
            Tab tab,
            int noteID,
            String noteName,
            String noteContent,
            String noteLabel1,
            String noteLabel2
    ) {
        this.noteID = noteID;
        this.tabID = tab.getTabID();
        this.noteName = noteName;
        this.noteContent = noteContent;
        this.noteLabel1 = noteLabel1;
        this.noteLabel2 = noteLabel2;
    }


    /**
     * Returns the unique identifier for this Note.
     *
     * @return The unique identifier for this Note.
     */
    public int getNoteID() {
        return noteID;
    }


    /**
     * Returns the unique identifier for the Tab that this Note belongs to.
     *
     * @return The unique identifier for the Tab that this Note belongs to.
     */
    public int getTabID() {
        return tabID;
    }


    /**
     * Returns the name of this Note.
     *
     * @return The name of this Note.
     */
    public String getNoteName() {
        return noteName;
    }


    public String getNoteLabel1() {
        return noteLabel1;
    }

    public String getNoteLabel2() {
        return noteLabel2;
    }

    /**
     * Returns the content of this Note.
     *
     * @return The content of this Note.
     */
    public String getNoteContent() {
        return noteContent;
    }

    /**
     * Sets the name of this Note.
     *
     * @param noteName The new name for this Note.
     */
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    /**
     * Sets the content of this Note.
     *
     * @param noteContent The new content for this Note.
     */
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }


    /**
     * Fetches the content of this Note from the database.
     * If the noteContent is null, it queries the database for the note content using the note's ID.
     * The result is then assigned to the noteContent.
     */
    public void fetchNoteContent() {
        if (noteContent == null) {
            ResultSet resultSet = DatabaseManager.select(
                    "notes",
                    new String[]{"note_content"},
                    new String[]{"note_id"},
                    new String[]{String.valueOf(this.noteID)}
            );

            try {
                resultSet.first();
                noteContent = resultSet.getString(1);
            } catch (SQLException e) {
                System.out.println("Error : " + e);
            }
        }
    }


    /**
     * Edits the name of this Note.
     * It updates the note name in the database and returns the result of the update operation.
     *
     * @param newName The new name for this Note.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editName(String newName) {
        this.noteName = newName;
        return DatabaseManager.update(
                "notes",
                "note_name",
                newName,
                "note_id",
                String.valueOf(this.noteID)
        );
    }


    /**
     * Edits the content of this Note.
     * It updates the note content in the database and returns the result of the update operation.
     *
     * @param newContent The new content for this Note.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editContent(String newContent) {
        this.noteContent = newContent;
        return DatabaseManager.update(
                "notes",
                "note_content",
                newContent,
                "note_id",
                String.valueOf(this.noteID)
        );
    }


}
