package com.example.notesmanager;

import java.sql.ResultSet;
import java.util.Map;


public class LabelManager {


    private static Map<Integer, String> labels = new java.util.HashMap<>();


    private static ResultSet fetchAllLabels() {
        return DatabaseManager.select(
                "labels",
                new String[]{"label_id", "label_name"},
                new String[]{},
                new String[]{}
        );
    }


    public static void updateLabels() {
        ResultSet resultSet = fetchAllLabels();
        try {
            System.out.println("Updating labels in LabelManager class");
            while (resultSet.next()) {
                int labelID = resultSet.getInt(1);
                String labelName = resultSet.getString(2);
                labels.put(labelID, labelName);
                System.out.println("Label ID : " + labelID + " Label Name : " + labelName);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
    }


    // Method that returns the label id given a label name
    public static int getLabelID(String labelName) {
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {
            if (entry.getValue().equals(labelName)) {
                return entry.getKey();
            }
        }
        return -1;
    }


    public static Map<Integer, String> getAllLabels() {
        // If the labels HashMap is empty, update it
        if (labels.isEmpty()) {
            updateLabels();
        }
        return labels;
    }


    public static int createLabel(String labelName) {
        return DatabaseManager.insert("labels",
                new String[]{"label_name"},
                new String[]{labelName}
        );
    }

    public static int updateLabel(String labelName, String newLabelName) {
        return DatabaseManager.update(
                "labels",
                "label_name",
                newLabelName,
                "label_name",
                labelName
        );
    }

    public static int deleteLabel(String labelName) {
        return DatabaseManager.delete("labels",
                "label_name",
                labelName
        );
    }

}