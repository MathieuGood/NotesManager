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

    /**
     * Constructor for the Tab class.
     * Initializes a new Tab object with the specified binder, tab ID, tab name, and tab color ID.
     * It also fetches all the notes associated with this tab from the database.
     *
     * @param binder     The Binder object that this Tab belongs to.
     * @param tabID      The unique identifier for this Tab.
     * @param tabName    The name of this Tab.
     * @param tabColorID The unique identifier for the color of this Tab.
     */
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


//    private ArrayList<Note> fetchAllNotes() {
//        System.out.println("\n***");
//        System.out.println("fetchAllNotes() for binderID " + this.getBinderID());
//
//        String[] fields = {
//                "notes.note_id",
//                "notes.note_name",
//                "notes.note_content",
//        };
//        String[] conditionFields = {"tab_id"};
//        String[] conditionValues = {String.valueOf(tabID)};
//
//        ResultSet resultSet = DatabaseManager.select(
//                "notes",
//                fields,
//                conditionFields,
//                conditionValues);
//
//        // Create ArrayList to store all Note objects
//        ArrayList<Note> notes = new ArrayList<>();
//
//        // Parse query results to new Note object and store it into ArrayList
//        try {
//            while (resultSet.next()) {
//                // Retrieve data from resultSet
//                int noteID = resultSet.getInt(1);
//                String noteName = resultSet.getString(2);
//                String noteContent = resultSet.getString(3);
//
//                // Print out data from resultSet
//                System.out.println("\t> " + noteID + " / " + noteName);
//
//                // Create new Note object with parsed data and add it to ArrayList
//                notes.add(new Note(this, noteID, noteName, noteContent));
//
//                // TODO : If there is a content in labelID, add it to the Note object created
//            }
//        } catch (Exception e) {
//            System.out.println("Error : " + e);
//            return null;
//        }
//        return notes;
//    }


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
        this.tabName = newName;
        return DatabaseManager.update(
                "tabs",
                "tab_name",
                newName,
                "tab_id",
                String.valueOf(this.tabID)
        );
    }


    /**
     * Edits the color ID of this Tab.
     * It updates the tab color ID in the database and returns the result of the update operation.
     *
     * @param newColorID The new color ID for this Tab.
     * @return The result of the update operation. Typically, the number of rows affected.
     */
    public int editColor(int newColorID) {
        this.tabColorID = newColorID;
        return DatabaseManager.update(
                "tabs",
                "tab_color_id",
                String.valueOf(newColorID),
                "tab_id",
                String.valueOf(this.tabID)
        );
    }


    public Note createNote(String noteName, String noteContent) {
        System.out.println("\n***");
        System.out.println("createNote() : " + noteName + " / tabID " + tabID);

        String[] fields = {"note_name", "tab_id", "note_content"};
        String[] values = {noteName, String.valueOf(tabID), noteContent};

        int noteID = DatabaseManager.insert("notes", fields, values);

        Note note = new Note(this, noteID, noteName, noteContent, null, null);
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

        return DatabaseManager.delete("notes", "noteID", String.valueOf(noteID));
    }

    public String getColorHex() {
        return DatabaseManager.getColorHexById(this.tabColorID);
    }

}
