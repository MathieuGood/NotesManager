package org.serfa.lpdaoo2024;

/**
 * The Tab class represents a tab in a binder in a note-taking application.
 * It contains methods to get and set the tab's properties, and to interact with the database.
 */
public class Tab {


    /**
     * The Binder object that this Tab belongs to.
     */
    private final Binder binder;

    /**
     * The unique identifier for this Tab.
     */
    private final int tabID;

    /**
     * The unique identifier for the User who owns this Tab.
     */
    private final int userID;

    /**
     * The name of this Tab.
     */
    private String tabName;

    /**
     * The unique identifier for the color of this Tab.
     */
    private int tabColorID;


    /**
     * Constructs a new Tab with the specified Binder, tab ID, tab name, and tab color ID.
     * It also sets the user ID from the Binder's user ID.
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
        this.binder = binder;
        this.tabID = tabID;
        this.userID = binder.getUserID();
        this.tabName = tabName;
        this.tabColorID = tabColorID;
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
     * Returns the unique identifier for the User who owns this Tab.
     *
     * @return The unique identifier for the User who owns this Tab.
     */
    public int getUserID() {
        return userID;
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


    /**
     * Creates a new note with the specified name and color ID.
     * It prints the details of the note to the console, and then inserts the note into the database.
     *
     * @param noteName    The name of the new note.
     * @param noteColorID The unique identifier for the color of the new note.
     * @return The result of the insert operation. Typically, the number of rows affected.
     */
    public int createNote(String noteName, int noteColorID) {
        System.out.println("\n***");
        System.out.println("createNote() : " + noteName + " / tabID " + tabID + " / colorID " + noteColorID);

        String[] fields = {"note_name", "tab_id", "note_color_id"};
        String[] values = {noteName, String.valueOf(tabID), String.valueOf(noteColorID)};

        return DatabaseManager.insert("tabs", fields, values);
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

}
