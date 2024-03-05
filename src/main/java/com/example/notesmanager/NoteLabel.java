package com.example.notesmanager;

import java.sql.ResultSet;
import java.util.Map;

/**
 * CustomLabel class is used to manage labels in the application.
 * It fetches all labels from the database and stores them in a HashMap for easy access.
 */
public class NoteLabel {

    /**
     * A HashMap to store label IDs and their corresponding names.
     */
    private final Map<Integer, String> labelNames = new java.util.HashMap<>();

    /**
     * Constructor for the CustomLabel class.
     * It sets the content of the labels by fetching them from the database.
     */
    NoteLabel() {
        setLabelsContent();
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

    /**
     * Injects results from fetchAllLabels() into labelNames HashMap.
     * Column 1 is an int named labelID, column 2 is a string named labelName.
     */
    public void setLabelsContent() {
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

    /**
     * Returns the label name for a given label ID.
     * @param labelID The ID of the label.
     * @return The name of the label.
     */
    public String getLabelName(int labelID) {
        return labelNames.get(labelID);
    }

    /**
     * Returns all labels stored in the labelNames HashMap.
     * @return A Map containing all labels.
     */
    public Map<Integer, String> getAllLabels() {
        return labelNames;
    }

}