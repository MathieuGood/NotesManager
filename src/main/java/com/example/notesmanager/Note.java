package com.example.notesmanager;

/**
 * The Note class represents a note in a tab in a note-taking application.
 * It contains methods to get and set the note's properties, and to interact with the database.
 */
public class Note  {


    /**
     * The unique identifier for this Note.
     */
    private final int noteID;

    /**
     * The unique identifier for the Tab that this Note belongs to.
     */
    private final int tabID;

    /**
     * The name of this Note.
     */
    private String noteName;

    /**
     * The content of this Note.
     */
    private String noteContent;

    /**
     * An array of unique identifiers for the Labels associated with this Note.
     * Each element in the array is an ID that corresponds to a Label in the database.
     */
    private int[] noteLabelID;


    /**
     * Constructor for the Note class.
     * Initializes a new Note object with the specified tab, note ID, note name and note content.
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
            String noteContent
    ) {
        this.noteID = noteID;
        this.tabID = tab.getTabID();
        this.noteName = noteName;
        this.noteContent = noteContent;
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


    /**
     * Returns the content of this Note.
     *
     * @return The content of this Note.
     */
    public String getNoteContent() {
        return noteContent;
    }


    /**
     * Returns the array of label IDs associated with this Note.
     *
     * @return The array of label IDs associated with this Note.
     */
    public int[] getNoteLabelID() {
        return noteLabelID;
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
     * Sets the array of label IDs associated with this Note.
     *
     * @param noteLabelID The new array of label IDs for this Note.
     */
    public void setNoteLabelID(int[] noteLabelID) {
        this.noteLabelID = noteLabelID;
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

    /*
    @Override
    public String getDisplayName() {
        return this.getNoteName(); // Ou tout autre nom que vous souhaitez afficher pour la Note.
    }

     */


}
