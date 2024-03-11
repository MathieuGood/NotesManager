package com.example.notesmanager;

import java.sql.ResultSet;
import java.util.Map;

/**
 * The NoteLabel class represents the labels that can be assigned to notes.
 * It contains methods to fetch and store labels from the database, and to get label names and IDs.
 */
public class LabelMenuBuilder {

    /**
     * A HashMap to store label IDs and their corresponding names.
     */
    private final Map<Integer, String> labelNames = new java.util.HashMap<>();

    /**
     * Constructor for the CustomLabel class.
     * It sets the content of the labels by fetching them from the database.
     */
    LabelMenuBuilder() {
        setLabelList();
        System.out.println("Constructing CustomLabel");
    }


    /**
     * Fetches all labels from the database.
     * @return A ResultSet containing all labels from the database.
     */
    public ResultSet fetchAllLabels() {
        String[] fields = {"label_id", "label_name"};
        String[] conditionFields = {};
        String[] conditionValues = {};

        return DatabaseManager.select(
                "labels",
                fields,
                conditionFields,
                conditionValues
        );
    }



    public void setLabelList() {
        ResultSet resultSet = fetchAllLabels();
        try {
            while (resultSet.next()) {
                int labelID = resultSet.getInt(1);
                String labelName = resultSet.getString(2);
                labelNames.put(labelID, labelName);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }


    // Method that returns the label id given a label name
    public int getLabelID(String labelName) {
        for (Map.Entry<Integer, String> entry : labelNames.entrySet()) {
            if (entry.getValue().equals(labelName)) {
                return entry.getKey();
            }
        }
        return -1;
    }


    /**
     * Returns the label name for a given label ID.
     * @param labelID The ID of the label.
     * @return The name of the label.
     */
    public String getLabelNameByID(int labelID) {
        return labelNames.get(labelID);
    }


    /**
     * Returns all labels stored in the labelNames HashMap.
     * @return A Map containing all labels.
     */
    public Map<Integer, String> getAllLabels() {
        return labelNames;
    }


    public static int createLabel(String labelName) {
        String[] fields = {"label_name"};
        String[] values = {labelName};

        int result =  DatabaseManager.insert("labels", fields, values);

        return result;
    }

    public static int updateLabel(String labelName, String newLabelName) {
        System.out.println("updateLabel function ");

        int result =  DatabaseManager.update("labels", "label_name", newLabelName, "label_name", labelName);

        System.out.println("result dans updateLabel " + result);

        return result;
    }

    public static int deleteLabel(String labelName) {

        System.out.println("deletelabel function ");
        System.out.println(labelName);

        int result =  DatabaseManager.delete("labels", "label_name", labelName);

        System.out.println("result dans deleteLabel " + result);

        return result;
    }

}