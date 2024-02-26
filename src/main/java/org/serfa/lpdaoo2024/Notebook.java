package org.serfa.lpdaoo2024;

import java.sql.*;
import java.util.ArrayList;


/**
 * The Notebook class represents a notebook in a note-taking application.
 * It contains methods to get the user ID of the owner of the notebook.
 */
public class Notebook {


    /**
     * The ID of the user who owns this notebook.
     */
    private final int userID;

    /**
     * Constructs a new Notebook with the specified user ID.
     *
     * @param userID The ID of the user who owns this notebook.
     */
    public Notebook(int userID) {
        this.userID = userID;
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
     * Prints the content tree of this notebook to the console.
     * This method first prints the details of the operation to the console.
     * Then, it performs a select operation on the "binders", "tabs", and "notes" tables in the database, retrieving all binders, tabs, and notes associated with the user ID.
     * The results of the select operation are stored in a ResultSet.
     * The method then iterates over the ResultSet, retrieving the binder ID, binder name, binder color ID, tab ID, tab name, tab color ID, note ID, note name, and note color ID for each row.
     * For each row, it prints the details to the console.
     * If an exception occurs during this process, it prints the error to the console.
     */
    public void getContentTree() {

        System.out.println("\n***");
        System.out.println("getContentTree() for userID " + userID);

        ResultSet resultSet = DatabaseManager.select(
                "binders "
                        + "INNER JOIN tabs ON binders.binder_id = tabs.binder_id "
                        + "INNER JOIN notes ON tabs.tab_id = notes.tab_id",
                new String[]{
                        "binders.binder_id",
                        "binders.binder_name",
                        "binders.binder_color_id",
                        "tabs.tab_id",
                        "tabs.tab_name",
                        "tabs.tab_color_id",
                        "notes.note_id",
                        "notes.note_name",
                        "notes.note_color_id"
                },
                "user_id",
                String.valueOf(userID));

        // Print ResultSet data
        try {
            while (resultSet.next()) {
                int binderID = resultSet.getInt(1);
                String binderName = resultSet.getString(2);
                int binderColorID = resultSet.getInt(3);
                int tabID = resultSet.getInt(4);
                String tabName = resultSet.getString(5);
                int tabColorID = resultSet.getInt(6);
                int noteID = resultSet.getInt(7);
                String noteName = resultSet.getString(8);
                int noteColorID = resultSet.getInt(9);
                System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID + " / " + tabID + " / " + tabName + " / " + tabColorID + " / " + noteID + " / " + noteName + " / " + noteColorID);
            }
        } catch (Exception e) {
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
    public ArrayList<Binder> getAllBinders() {
        System.out.println("\n***");
        System.out.println("getAllBinders() for userID " + this.userID + " :");

        ResultSet resultSet = DatabaseManager.select(
                "binders",
                new String[]{
                        "binders.binder_id",
                        "binders.binder_name",
                        "binders.binder_color_id"
                },
                "user_id",
                String.valueOf(userID));

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
     * Creates a new binder with the specified name and color ID, and adds it to this notebook.
     * This method first prints the details of the operation to the console.
     * Then, it prepares the fields and values for the database insert operation.
     * Finally, it calls the insert method of the DatabaseManager class to perform the operation.
     *
     * @param binderName    The name of the new binder.
     * @param binderColorID The color ID of the new binder.
     * @return The result of the database insert operation.
     */
    public int createBinder(String binderName, int binderColorID) {
        System.out.println("\n***");
        System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + binderColorID);

        String[] fields = {"binder_name", "user_id", "binder_color_id"};
        String[] values = {binderName, String.valueOf(userID), String.valueOf(binderColorID)};

        return DatabaseManager.insert("binders", fields, values);
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

        return DatabaseManager.delete("binders", "binderID", String.valueOf(binderID));
    }

}
