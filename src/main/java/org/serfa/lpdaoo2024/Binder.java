package org.serfa.lpdaoo2024;


/**
 * The Binder class represents a binder in a notebook.
 * It contains methods to get and set binder properties, as well as to create and delete tabs.
 */
public class Binder {


    /**
     * The Notebook object that this Binder belongs to.
     */
    private final Notebook notebook;

    /**
     * The unique identifier for this Binder.
     */
    private final int binderID;

    /**
     * The unique identifier for the User who owns this Binder.
     */
    private final int userID;

    /**
     * The name of this Binder.
     */
    private String binderName;

    /**
     * The unique identifier for the color of this Binder.
     */
    private int binderColorID;


    /**
     * Constructs a new Binder with the specified notebook, binder ID, binder name, and binder color ID.
     *
     * @param notebook      The notebook to which this binder belongs.
     * @param binderID      The ID of this binder.
     * @param binderName    The name of this binder.
     * @param binderColorID The color ID of this binder.
     */
    public Binder(
            Notebook notebook,
            int binderID,
            String binderName,
            int binderColorID
    ) {
        this.notebook = notebook;
        this.binderID = binderID;
        this.userID = notebook.getUserID();
        this.binderName = binderName;
        this.binderColorID = binderColorID;
    }


    /**
     * Returns the ID of this binder.
     *
     * @return The ID of this binder.
     */
    public int getBinderID() {
        return this.binderID;
    }


    /**
     * Returns the user ID of the owner of this binder.
     *
     * @return The user ID of the owner of this binder.
     */
    public int getUserID() {
        return this.userID;
    }


    /**
     * Returns the name of this binder.
     *
     * @return The name of this binder.
     */
    public String getBinderName() {
        return this.binderName;
    }


    /**
     * Returns the color ID of this binder.
     *
     * @return The color ID of this binder.
     */
    public int getBinderColorID() {
        return this.binderColorID;
    }


    /**
     * Edits the name of this binder and updates it in the database.
     *
     * @param newName The new name of this binder.
     * @return The result of the database update operation.
     */
    public int editName(String newName) {
        this.binderName = newName;
        return DatabaseManager.update(
                "binders",
                "binder_name",
                newName,
                "binder_id",
                String.valueOf(this.binderID)
        );
    }


    /**
     * Edits the color ID of this binder and updates it in the database.
     *
     * @param newColorID The new color ID of this binder.
     * @return The result of the database update operation.
     */
    public int editColor(int newColorID) {
        this.binderColorID = newColorID;
        return DatabaseManager.update(
                "binders",
                "binder_color_id",
                String.valueOf(newColorID),
                "binder_id",
                String.valueOf(this.binderID)
        );
    }


/**
 * Creates a new Tab with the specified name and color ID.
 * It inserts the new tab into the database, and then returns the new Tab object.
 *
 * @param tabName The name for the new Tab.
 * @param tabColorID The color ID for the new Tab.
 * @return The new Tab object.
 */
public Tab createTab(String tabName, int tabColorID) {
    System.out.println("\n***");
    System.out.println("createTab() : " + tabName + " / binderID " + binderID + " / colorID " + tabColorID);

    String[] fields = {"tab_name", "binder_id", "tab_color_id"};
    String[] values = {tabName, String.valueOf(binderID), String.valueOf(tabColorID)};

    int tabID = DatabaseManager.insert("tabs", fields, values);

    return new Tab(this, tabID, tabName, tabColorID);
}


    /**
     * Deletes the tab with the specified ID from this binder.
     *
     * @param tabID The ID of the tab to delete.
     * @return The result of the database delete operation.
     */
    public int deleteTab(int tabID) {
        System.out.println("\n***");
        System.out.println("deleteTab() : " + " tabID " + tabID);

        return DatabaseManager.delete("tabs", "tabID", String.valueOf(tabID));
    }
}