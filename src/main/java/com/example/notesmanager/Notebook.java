package com.example.notesmanager;

import java.sql.*;
import java.util.ArrayList;


/**
 * Represents a Notebook in the application.
 * A Notebook contains a list of Binders.
 */
public class Notebook {


    // An ArrayList that holds all the Binder objects associated with this Notebook.
    private ArrayList<Binder> binders = new ArrayList<>();

    // The unique identifier for the User that owns this Notebook.
    private final int userID;


    /**
     * Constructs a Notebook object for the given user.
     * <p>
     * This constructor initializes the userID with the ID of the given user.
     * It also initializes the binders list by calling the fetchAllBinders method, which retrieves all binders associated with the user from the database.
     *
     * @param user The User object representing the owner of this notebook.
     */
    public Notebook(User user) {
        this.userID = user.getUserID();
        setNotebookContent();
    }


    /**
     * Returns the user ID of the owner of this notebook.
     *
     * @return The user ID of the owner of this notebook.
     */
    public int getUserID() {
        return userID;
    }


    /**
     * Returns the list of Binders in this Notebook.
     *
     * @return The list of Binders in this Notebook.
     */
    public ArrayList<Binder> getBinders() {
        return binders;
    }


    /**
     * Adds a Binder object to the binders list.
     *
     * @param binder The Binder object to be added to the list.
     */
    public void addBinderToList(Binder binder) {
        binders.add(binder);
    }


    /**
     * Fetches all content related to the Notebook from the database.
     * This includes information about binders, tabs, and notes associated with the user.
     * The method performs a select operation on the database with multiple left joins to retrieve the required data.
     * The result of the select operation is returned as a ResultSet.
     *
     * @return A ResultSet containing all content related to the Notebook.
     */
    public ResultSet fetchAllNotebookContent() {

        System.out.println("\n***");
        System.out.println("getContentTree() for userID " + userID);

        String[] fields = {
                "binders.binder_id",
                "binders.binder_name",
                "binders.binder_color_id",
                "tabs.tab_id",
                "tabs.tab_name",
                "tabs.tab_color_id",
                "notes.note_id",
                "notes.note_name",
                "label1.label_name",
                "label2.label_name"
        };
        String[] conditionFields = {"users.user_id"};
        String[] conditionValues = {String.valueOf(userID)};

        return DatabaseManager.select(
                "binders "
                        + "LEFT JOIN users ON binders.user_id = users.user_id "
                        + "LEFT JOIN tabs ON tabs.binder_id = binders.binder_id "
                        + "LEFT JOIN notes ON notes.tab_id = tabs.tab_id "
                        + "LEFT JOIN labels AS label1 ON notes.note_label1_id = label1.label_id "
                        + "LEFT JOIN labels AS label2 ON notes.note_label2_id = label2.label_id ",
                fields,
                conditionFields,
                conditionValues
        );
    }


    /**
     * Sets the content of the Notebook by fetching all related content from the database.
     * This method is a shorthand for calling setNotebookContent with a null argument,
     * which means it fetches all content without any label filter.
     */
    public void setNotebookContent() {
        setNotebookContent(null);
    }


    /**
     * Sets the content of the Notebook by fetching all related content from the database.
     * The method fetches all content related to the Notebook and iterates over the ResultSet.
     * For each row in the ResultSet, it checks if the binder ID, tab ID, and note ID correspond to new entities.
     * If they do, it creates new Binder, Tab, and Note objects and adds them to the appropriate lists.
     * If an exception occurs during this process, it prints the error to the console.
     */
    public void setNotebookContent(String labelNameFilter) {
        // Clear the binders list
        binders = new ArrayList<>();

        // Fetch all content related to the Notebook from the database
        ResultSet notebookContent = fetchAllNotebookContent();

        // Initialize current binder and tab IDs
        int currentBinderID = 0;
        int currentTabID = 0;

        try {
            // Iterate over each row in the ResultSet
            while (notebookContent.next()) {
                // Retrieve data from the current row of the ResultSet
                int binderID = notebookContent.getInt(1);
                String binderName = notebookContent.getString(2);
                int binderColorID = notebookContent.getInt(3);
                int tabID = notebookContent.getInt(4);
                String tabName = notebookContent.getString(5);
                int tabColorID = notebookContent.getInt(6);
                int noteID = notebookContent.getInt(7);
                String noteName = notebookContent.getString(8);
                String noteLabel1 = notebookContent.getString(9);
                String noteLabel2 = notebookContent.getString(10);

                // Print the retrieved data
                //  System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID + " / " + tabID + " / " + tabName + " / " + tabColorID + " / " + noteID + " / " + noteName + " / " + noteColorID);

                // If a labelNameFilter is provided, do not skip the current row if the note's labels do not match the filter
                if (labelNameFilter != null) {
                    // If either label1 or label2 does not match the filter, skip the current row
                    if (!labelNameFilter.equals(noteLabel1) && !labelNameFilter.equals(noteLabel2)) {
                        continue;
                    }
                }

                System.out.println("Adding to notebook : " + noteName + " / " + noteLabel1 + " / " + noteLabel2);

                // If the binder ID corresponds to a new binder, create a new Binder object and add it to the binders list
                if (binderID != currentBinderID) {
                    System.out.println("Binder : " + binderName + " / ID #" + binderID);
                    addBinderToList(new Binder(this, binderID, binderName, binderColorID));
                    currentBinderID = binderID;
                }

                // If the tab ID corresponds to a new tab and is not null, create a new Tab object and add it to the tabs list of the last binder
                if (tabID != currentTabID && tabID != 0) {
                    System.out.println("  > Tab : " + tabName);
                    binders.getLast().addTabToList(new Tab(binders.getLast(), tabID, tabName, tabColorID));
                    currentTabID = tabID;
                }

                // If the note ID is not null, create a new Note object and add it to the notes list of the last tab of the last binder
                if (noteID != 0) {
                    System.out.println("Checking if " + noteLabel1 + " or " + noteLabel2 + " is null");
                    // Create a list of LabelNote objects to store the note's labels
                    ArrayList<NoteLabel> labels = new ArrayList<>();
                    if (noteLabel1 != null) {
                        labels.add(new NoteLabel(noteLabel1));
                        System.out.println("Adding label " + noteLabel1 + " to note " + noteName);
                    }
                    if (noteLabel2 != null) {
                        labels.add(new NoteLabel(noteLabel2));
                        System.out.println("Adding label " + noteLabel2 + " to note " + noteName);
                    }

                    System.out.println("\t>> Note : " + noteName);
                    binders.getLast().getTabs().getLast().addNoteToList(new Note(
                            binders.getLast().getTabs().getLast(),
                            noteID,
                            noteName,
                            labels
                    ));
                }
            }
        } catch (Exception e) {
            // Print any exceptions that occur
            System.out.println("Error : " + e);
        }
    }


    /**
     * Retrieves all binders associated with the user ID of this notebook.
     * This method first prints the details of the operation to the console.
     * Then, it performs a select operation on the "binders" table in the database, retrieving all binders associated with the user ID.
     * The results of the select operation are stored in a ResultSet.
     * The method then creates an ArrayList to store Binder objects.
     * It iterates over the ResultSet, retrieving the binder ID, binder name, and binder color ID for each binder.
     * For each binder, it prints the details to the console, creates a new Binder object with the retrieved data, and adds the Binder object to the ArrayList.
     * If an exception occurs during this process, it prints the error to the console and returns null.
     * If the process completes successfully, it returns the ArrayList of Binder objects.
     *
     * @return An ArrayList of Binder objects representing all binders associated with the user ID of this notebook, or null if an exception occurs.
     */
    private ArrayList<Binder> fetchAllBinders() {
        System.out.println("\n***");
        System.out.println("getAllBinders() for userID " + this.userID + " :");

        ResultSet resultSet = DatabaseManager.select(
                "binders",
                new String[]{
                        "binders.binder_id",
                        "binders.binder_name",
                        "binders.binder_color_id"
                },
                new String[]{"user_id"},
                new String[]{String.valueOf(userID)}
        );

        // Create ArrayList to store all Binder objects
        ArrayList<Binder> binders = new ArrayList<>();

        // Parse query results to new Binder object and store it into ArrayList
        try {
            while (resultSet.next()) {
                // Retrieve data from resultSet
                int binderID = resultSet.getInt(1);
                String binderName = resultSet.getString(2);
                int binderColorID = resultSet.getInt(3);

                // Print out data from resultSet
                System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID);

                // Create new Binder object with parsed data and add it to ArrayList
                binders.add(new Binder(this, binderID, binderName, binderColorID));
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
            return null;
        }
        return binders;
    }


    /**
     * Creates a new Binder with the specified name and color ID.
     * It inserts the new binder into the database, and then returns the new Binder object.
     *
     * @param binderName    The name for the new Binder.
     * @param binderColorID The color ID for the new Binder.
     * @return The new Binder object.
     */
    public Binder createBinder(String binderName, int binderColorID) {
        System.out.println("\n***");
        System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + binderColorID);

        String[] fields = {"binder_name", "user_id", "binder_color_id"};
        String[] values = {binderName, String.valueOf(userID), String.valueOf(binderColorID)};

        int binderID = DatabaseManager.insert("binders", fields, values);

        Binder binder = new Binder(this, binderID, binderName, binderColorID);
        binders.add(binder);

        return binder;
    }


    /**
     * Deletes the binder with the specified ID from this notebook.
     * This method first prints the details of the operation to the console.
     * Then, it calls the delete method of the DatabaseManager class to perform the operation.
     *
     * @param binderID The ID of the binder to delete.
     * @return The result of the database delete operation.
     */
    public int deleteBinder(int binderID) {
        System.out.println("\n***");
        System.out.println("deleteBinder() : " + " binderID " + binderID);

        int result = DatabaseManager.delete("binders", "binderID", String.valueOf(binderID));

        // If query is successful, remove the Binder object from the ArrayList
        if (result > 0) {

            for (Binder binder : binders) {
                if (binder.getBinderID() == binderID) {
                    binders.remove(binder);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Recherche un Binder par son nom.
     *
     * @param binderName Le nom du Binder à rechercher.
     * @return Le Binder correspondant au nom donné, ou null si non trouvé.
     */
    public Binder getBinderByName(String binderName) {
        for (Binder binder : this.binders) {
            if (binder.getBinderName().equalsIgnoreCase(binderName)) {
                return binder;
            }
        }
        return null;
    }

    /**
     * Recherche un Tab (intervalaire) par son nom dans tous les classeurs.
     *
     * @param tabName Le nom du Tab (intervalaire) à rechercher.
     * @return Le premier Tab (intervalaire) correspondant au nom donné, ou null si non trouvé.
     */
    public Tab getTabByDividerName(String tabName) {
        for (Binder binder : binders) {
            for (Tab tab : binder.getTabs()) {
                if (tab.getTabName().equalsIgnoreCase(tabName)) {
                    return tab;
                }
            }
        }
        return null;
    }


    public Note getNoteFromBinderTabNoteName(String noteName, String tabName, String binderName) {

        ArrayList<Binder> binders = this.getBinders();
        for (Binder binder : binders) {

            if (binder.getBinderName().equals(binderName)) {
                System.out.println("Binder " + binderName + " found.");

                ArrayList<Tab> tabs = binder.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getTabName().equals(tabName)) {
                        System.out.println("Tab " + tabName + " found.");

                        ArrayList<Note> notes = tab.getNotes();
                        for (Note note : notes) {
                            if (note.getNoteName().equals(noteName)) {
                                return note;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


}
