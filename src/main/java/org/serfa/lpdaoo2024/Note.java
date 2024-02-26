package org.serfa.lpdaoo2024;

/**
 * The Note class represents a note in a tab in a note-taking application.
 * It contains methods to get and set the note's properties, and to interact with the database.
 */
public class Note {


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
     * The unique identifier for the color of this Note.
     */
    private int noteColorID;


    /**
     * Constructor for the Note class.
     * Initializes a new Note object with the specified tab, note ID, note name, note content, and note color ID.
     *
     * @param tab         The Tab object that this Note belongs to.
     * @param noteID      The unique identifier for this Note.
     * @param noteName    The name of this Note.
     * @param noteContent The content of this Note.
     * @param noteColorID The unique identifier for the color of this Note.
     */
    public Note(
            Tab tab,
            int noteID,
            String noteName,
            String noteContent,
            int noteColorID
    ) {
        this.noteID = noteID;
        this.tabID = tab.getTabID();
        this.noteName = noteName;
        this.noteContent = noteContent;
        this.noteColorID = noteColorID;
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
     * Returns the unique identifier for the color of this Note.
     *
     * @return The unique identifier for the color of this Note.
     */
    public int getNoteColorID() {
        return noteColorID;
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


    /**
     * Edits the color ID of this Note.
     * It updates the note color ID in the database and returns the result of the update operation.
     *
     * @param newColorID The new color ID for this Note.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editColor(int newColorID) {
        this.noteColorID = newColorID;
        return DatabaseManager.update(
                "notes",
                "notes_color_id",
                String.valueOf(newColorID),
                "note_id",
                String.valueOf(this.noteID)
        );
    }


}
