package org.serfa.lpdaoo2024;

import java.sql.*;
import java.util.ArrayList;

public class Notebook {

    private final int userID;

    public Notebook(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }


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


    public int createBinder(String binderName, int binderColorID) {
        System.out.println("\n***");
        System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + binderColorID);

        String[] fields = {"binder_name", "user_id", "binder_color_id"};
        String[] values = {binderName, String.valueOf(userID), String.valueOf(binderColorID)};

        return DatabaseManager.insert("binders", fields, values);
    }


    public int deleteBinder(int binderID) {
        System.out.println("\n***");
        System.out.println("deleteBinder() : " + " binderID " + binderID);

        return DatabaseManager.delete("binders", "binderID", String.valueOf(binderID));
    }


}
