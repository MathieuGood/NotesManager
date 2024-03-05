package com.example.notesmanager;

import java.sql.ResultSet;
import java.util.Map;

public class CustomLabel {

    private final Map<Integer, String> labelNames = new java.util.HashMap<>();


    CustomLabel() {
        setLabelsContent();
        System.out.println("Constructing CustomLabel");
    }


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

    // Inject results from fetchAllLabels() into labelNames hash map
    // Column 1 is an int named labelID, column 2 is a string named labelName
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

    // With the labelID, return the labelName
    public String getLabelName(int labelID) {
        return labelNames.get(labelID);
    }

    public Map<Integer, String> getAllLabels() {
        return labelNames;
    }

}
