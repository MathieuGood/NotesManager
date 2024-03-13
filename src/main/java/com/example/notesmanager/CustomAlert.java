package com.example.notesmanager;

import javafx.scene.control.Alert;

/**
 * Classe CustomAlert pour créer et gérer des alertes personnalisées.
 */
public class CustomAlert {


    /**
     * L'objet Alert qui sera affiché à l'utilisateur
     */
    public static Alert alert;


    /**
     * Le titre de l'alerte
     */
    public static String title;


    /**
     * L'en-tête de l'alerte
     */
    public static String header;


    /**
     * Le contenu de l'alerte
     */
    public static String content;


    /**
     * Définit le titre de l'alerte.
     *
     * @param title Le titre de l'alerte
     */
    public static void setTitle(String title) {
        CustomAlert.title = title;
        alert.setTitle(title);
    }


    /**
     * Définit l'en-tête de l'alerte.
     *
     * @param header L'en-tête de l'alerte
     */
    public static void setHeader(String header) {
        CustomAlert.header = header;
        alert.setHeaderText(header);
    }


    /**
     * Définit le contenu de l'alerte.
     *
     * @param content Le contenu de l'alerte
     */
    public static void setContent(String content) {
        CustomAlert.content = content;
        alert.setContentText(content);
    }


    /**
     * Crée une nouvelle alerte avec les paramètres spécifiés.
     *
     * @param alertType   Le type de l'alerte (par exemple, INFORMATION, WARNING, etc.)
     * @param title       Le titre de l'alerte
     * @param header      L'en-tête de l'alerte
     * @param content     Le contenu de l'alerte
     * @param typeDisplay Le type d'affichage de l'alerte ("showAndWait" ou "show")
     * @return L'objet Alert créé
     */
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