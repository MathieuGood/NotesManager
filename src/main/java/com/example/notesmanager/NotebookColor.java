package com.example.notesmanager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NotebookColor {

    private static boolean colorsFetched = false;
    private static Map<Integer, String> colorNames;
    private static Map<Integer, String> colorHexes;


    public static void fetchAllColors() {
        if (!colorsFetched) {
            colorNames = new HashMap<>();
            colorHexes = new HashMap<>();

            System.out.println("\nFetching all colors...");
            ResultSet resultSet = DatabaseManager.select(
                    "colors",
                    new String[]{"*"},
                    null,
                    null
            );

            colorsFetched = true;

            // Print entries of ResultSet
            try {
                while (resultSet.next()) {
                    int colorID = resultSet.getInt("color_id");
                    String colorName = resultSet.getString("color_name");
                    String colorHex = resultSet.getString("color_hex");
                    colorNames.put(colorID, colorName);
                    colorHexes.put(colorID, colorHex);
                    System.out.println("Color ID: " + colorID + " / Name: " + colorName + " / Hex: " + colorHex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String getHexColorByID(int colorID) {
        fetchAllColors();
        return colorHexes.get(colorID);
    }


    public static String getColorNameByID(int colorID) {
        fetchAllColors();
        return colorNames.get(colorID);
    }

    public static int getColorIDByName(String colorName) {
        fetchAllColors();
        for (Map.Entry<Integer, String> entry : colorNames.entrySet()) {
            if (entry.getValue().equals(colorName)) {
                return entry.getKey();
            }
        }
        return 0;
    }

    public static ArrayList<String> getAllColorNames() {
        fetchAllColors();
        return new ArrayList<>(colorNames.values());
    }

}