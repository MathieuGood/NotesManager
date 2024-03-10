package com.example.notesmanager;


import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Represents a Tab in the application.
 * A Tab contains a list of Notes.
 */
public class Tab {


    // An ArrayList that holds all the Note objects associated with this Tab.
    private ArrayList<Note> notes = new ArrayList<>();

    // The unique identifier for this Tab.
    private final int tabID;

    // The unique identifier for the Binder that this Tab belongs to.
    private final int binderID;

    // The name of this Tab.
    private String tabName;

    // The unique identifier for the color of this Tab.
    private int tabColorID;


    public Tab(
            Binder binder,
            int tabID,
            String tabName,
            int tabColorID
    ) {
        this.tabID = tabID;
        this.binderID = binder.getBinderID();
        this.tabName = tabName;
        this.tabColorID = tabColorID;
    }


    /**
     * Returns the list of Notes in this Tab.
     *
     * @return The list of Notes in this Tab.
     */
    public ArrayList<Note> getNotes() {
        return notes;
    }


    /**
     * Returns the unique identifier for this Tab.
     *
     * @return The unique identifier for this Tab.
     */
    public int getTabID() {
        return tabID;
    }


    /**
     * Returns the unique identifier for the Binder that this Tab belongs to.
     *
     * @return The unique identifier for the Binder that this Tab belongs to.
     */
    public int getBinderID() {
        return binderID;
    }


    /**
     * Returns the name of this Tab.
     *
     * @return The name of this Tab.
     */
    public String getTabName() {
        return tabName;
    }


    /**
     * Returns the unique identifier for the color of this Tab.
     *
     * @return The unique identifier for the color of this Tab.
     */
    public int getTabColorID() {
        return tabColorID;
    }


    public void addNoteToList(Note note) {
        notes.add(note);
    }

    /**
     * Edits the name of this Tab.
     * It updates the tab name in the database and returns the result of the update operation.
     *
     * @param newName The new name for this Tab.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "tabs",
                "tab_name",
                newName,
                "tab_id",
                String.valueOf(this.tabID)
        );

        if (result > 0) {
            this.tabName = newName;
        }

        return result;
    }


    /**
     * Edits the color ID of this Tab.
     * It updates the tab color ID in the database and returns the result of the update operation.
     *
     * @param newColorID The new color ID for this Tab.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editColor(int newColorID) {
        int result = DatabaseManager.update(
                "tabs",
                "tab_color_id",
                String.valueOf(newColorID),
                "tab_id",
                String.valueOf(this.tabID)
        );

        if (result > 0) {
            this.tabColorID = newColorID;
        }

        return result;
    }


    public Note createNote(String noteName) {
        System.out.println("\n***");
        System.out.println("createNote() : " + noteName + " / tabID " + tabID);

        String[] fields = {"note_name", "tab_id"};
        String[] values = {noteName, String.valueOf(tabID)};

        int noteID = DatabaseManager.insert("notes", fields, values);

        Note note = new Note(this, noteID, noteName, new ArrayList<NoteLabel>());
        notes.add(note);

        return note;
    }


    /**
     * Deletes the note with the specified ID.
     * It prints the ID of the note to the console, and then deletes the note from the database.
     *
     * @param noteID The unique identifier for the note to be deleted.
     * @return The result of the delete operation. Typically, the number of rows affected.
     */
    public int deleteNote(int noteID) {
        System.out.println("\n***");
        System.out.println("deleteNote() : " + " noteID " + noteID);

        int result = DatabaseManager.delete("notes", "note_id", String.valueOf(noteID));

        // If the delete operation was successful, remove the note from the ArrayList
        if (result > 0) {
            for (Note note : notes) {
                if (note.getNoteID() == noteID) {
                    notes.remove(note);
                    break;
                }
            }
        }
        return result;
    }

    public String getColorHex() {
        return NotebookColor.getHexColorByID(this.tabColorID);
    }

}
