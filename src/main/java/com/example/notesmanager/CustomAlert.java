package com.example.notesmanager;

import javafx.scene.control.Alert;

public class CustomAlert {

    public static Alert alert;
    public static String title;
    public static String header;
    public static String content;

    public static void setTitle(String title) {
        CustomAlert.title = title;
        alert.setTitle(title);

    }

    public static void setHeader(String header) {
        CustomAlert.header = header;
        alert.setHeaderText(header);
    }

    public static void setContent(String content) {
        CustomAlert.content = content;
        alert.setContentText(content);
    }

    public static Alert create(Alert.AlertType alertType, String title, String header, String content, String typeDisplay) {
        CustomAlert.alert = new Alert(alertType);
        CustomAlert.title = title;
        CustomAlert.header = header;
        CustomAlert.content = content;

        CustomAlert.alert.setTitle(title);
        CustomAlert.alert.setHeaderText(header);
        CustomAlert.alert.setContentText(content);

        if (typeDisplay != null) {
            if (typeDisplay.equals("showAndWait")) alert.showAndWait();
            else if (typeDisplay.equals("show")) alert.show();
        }

        return alert;
    }
}