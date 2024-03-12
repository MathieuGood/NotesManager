package com.example.notesmanager;


import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Represents a Binder in the application.
 * A Binder contains a list of Tabs.
 */
public class Binder {


    // Parent notebook of this binder
    private Notebook notebook;

    // An ArrayList that holds all the Tab objects associated with this Binder.
    private ArrayList<Tab> tabs = new ArrayList<>();

    // The unique identifier for this Binder.
    private final int binderID;

    // The unique identifier for the User that owns this Binder.
    private final int userID;

    // The name of this Binder.
    private String binderName;

    // The unique identifier for the color of this Binder.
    private int binderColorID;


    /**
     * Constructs a new Binder with the specified binder ID, binder name, and binder color ID.
     *
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
        this.binderID = binderID;
        this.notebook = notebook;
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
     * Returns the list of Tabs in this Binder.
     *
     * @return The list of Tabs in this Binder.
     */
    public ArrayList<Tab> getTabs() {
        return tabs;
    }


    public void addTabToList(Tab tab) {
        tabs.add(tab);
    }


    /**
     * Fetches all the tabs associated with this binder from the database.
     * It queries the database for tabs that belong to the user who owns this binder.
     * The results are parsed into Tab objects and stored in an ArrayList.
     * If an error occurs during the database query or the parsing of the results, it prints the error and returns null.
     *
     * @return An ArrayList of Tab objects representing all the tabs associated with this binder, or null if an error occurs.
     */
    private ArrayList<Tab> fetchAllTabs() {
        System.out.println("\n***");
        System.out.println("fetchAllTabs() for userID " + this.userID + " / binder " + binderID);

        String[] fields = {
                "tabs.tab_id",
                "tabs.tab_name",
                "tabs.tab_color_id"
        };
        String[] conditionFields = {"binder_id"};
        String[] conditionValues = {String.valueOf(binderID)};

        ResultSet resultSet = DatabaseManager.select(
                "tabs",
                fields,
                conditionFields,
                conditionValues
        );

        // Create ArrayList to store all Tab objects
        ArrayList<Tab> tabs = new ArrayList<>();

        // Parse query results to new Tab object and store it into ArrayList
        try {
            while (resultSet.next()) {
                // Retrieve data from resultSet
                int tabID = resultSet.getInt(1);
                String tabName = resultSet.getString(2);
                int tabColorID = resultSet.getInt(3);

                // Print out data from resultSet
                System.out.println("\t> " + tabID + " / " + tabName + " / " + tabColorID);

                // Create new Tab object with parsed data and add it to ArrayList
                tabs.add(new Tab(this, tabID, tabName, tabColorID));
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
            return null;
        }
        return tabs;
    }


    /**
     * Edits the name of this binder and updates it in the database.
     *
     * @param newName The new name of this binder.
     * @return The result of the database update operation.
     */
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "binders",
                "binder_name",
                newName,
                "binder_id",
                String.valueOf(this.binderID));

        if (result > 0) {
            this.binderName = newName;
        }

        return result;
    }


    /**
     * Edits the color ID of this binder and updates it in the database.
     *
     * @param newColorID The new color ID of this binder.
     * @return The result of the database update operation.
     */
    public int editColor(int newColorID) {
        int result = DatabaseManager.update(
                "binders",
                "binder_color_id",
                String.valueOf(newColorID),
                "binder_id",
                String.valueOf(this.binderID)
        );

        if (result > 0) {
            this.binderColorID = newColorID;
        }

        return result;
    }


    /**
     * Creates a new Tab with the specified name and color ID.
     * It inserts the new tab into the database, and then returns the new Tab object.
     *
     * @param tabName    The name for the new Tab.
     * @param tabColorID The color ID for the new Tab.
     * @return The new Tab object.
     */
    public Tab createTab(String tabName, int tabColorID) {

        // Vérif si l'ID de couleur n'est pas négatif avant de faire l'insertion
        if (tabColorID < 0) {
            System.err.println("Erreur : L'ID de la couleur est hors de portée.");
            return null;
        }

        System.out.println("\n***");
        System.out.println("createTab() : " + tabName + " / binderID " + binderID + " / colorID " + tabColorID);

        String[] fields = {"tab_name", "binder_id", "tab_color_id"};
        String[] values = {tabName, String.valueOf(binderID), String.valueOf(tabColorID)};

        int tabID = DatabaseManager.insert("tabs", fields, values);

        Tab tab = new Tab(this, tabID, tabName, tabColorID);
        tabs.add(tab);

        return tab;
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

        int result = DatabaseManager.delete("tabs", "tab_id", String.valueOf(tabID));

        // If query is successful, remove the Binder object from the ArrayList
        if (result > 0) {
            for (Tab tab : tabs) {
                if (tab.getTabID() == tabID) {
                    tabs.remove(tab);
                    break;
                }
            }
        }
        return result;
    }


    public String getColorHex() {
        return NotebookColor.getHexColorByID(this.binderColorID);
    }


    /**
     * Recherche un Tab (intervalaire) par son nom.
     *
     * @param tabName Le nom du Tab (intervalaire) à rechercher.
     * @return Le Tab (intervalaire) correspondant au nom donné, ou null si non trouvé.
     */
    public Tab findTabByName(String tabName) {
        for (Tab tab : this.tabs) {
            if (tab.getTabName().equals(tabName)) {
                return tab;
            }
        }
        return null;
    }
}