package com.example.notesmanager;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Classe LabelManager qui gère les opérations de base de données pour les étiquettes.
 */
public class LabelManager {


    /**
     * Map qui stocke les étiquettes récupérées de la base de données.
     */
    private static Map<Integer, String> labels = new java.util.HashMap<>();


    /**
     * Récupère toutes les étiquettes de la base de données.
     *
     * @return Un ResultSet contenant toutes les étiquettes.
     */
    private static ResultSet fetchAllLabels() {
        return DatabaseManager.select(
                "labels",
                new String[]{"label_id", "label_name"},
                new String[]{},
                new String[]{}
        );
    }


    /**
     * Met à jour les étiquettes stockées dans la Map à partir de la base de données.
     */
    public static void updateLabels() {
        ResultSet resultSet = fetchAllLabels();
        try {
            // Clear the labels HashMap
            labels = new java.util.HashMap<>();
            System.out.println("Mise à jour des étiquettes dans la classe LabelManager");
            while (resultSet.next()) {
                int labelID = resultSet.getInt(1);
                String labelName = resultSet.getString(2);
                // Ajoute l'étiquette à la Map labels
                labels.put(labelID, labelName);
                System.out.println("ID de l'étiquette : " + labelID + " Nom de l'étiquette : " + labelName);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e);
        }
    }


    /**
     * Récupère l'ID d'une étiquette donnée.
     *
     * @param labelName Le nom de l'étiquette.
     * @return L'ID de l'étiquette. Retourne -1 si l'étiquette n'est pas trouvée.
     */
    public static int getLabelID(String labelName) {
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {
            if (entry.getValue().equals(labelName)) {
                return entry.getKey();
            }
        }
        return -1;
    }


    /**
     * Récupère toutes les étiquettes.
     *
     * @return Une Map contenant toutes les étiquettes.
     */
    public static Map<Integer, String> getAllLabels() {
        // Si la Map labels est vide, la met à jour
        if (labels.isEmpty()) {
            updateLabels();
        }
        return labels;
    }


    /**
     * Crée une nouvelle étiquette.
     *
     * @param labelName Le nom de la nouvelle étiquette.
     * @return L'ID de la nouvelle étiquette.
     */
    public static int createLabel(String labelName) {
        return DatabaseManager.insert("labels",
                new String[]{"label_name"},
                new String[]{labelName}
        );
    }


    /**
     * Met à jour une étiquette existante.
     *
     * @param labelName    Le nom actuel de l'étiquette.
     * @param newLabelName Le nouveau nom de l'étiquette.
     * @return Le nombre de lignes affectées par l'opération de mise à jour.
     */
    public static int updateLabel(String labelName, String newLabelName) {
        return DatabaseManager.update(
                "labels",
                "label_name",
                newLabelName,
                "label_name",
                labelName
        );
    }


    /**
     * Supprime une étiquette existante.
     *
     * @param labelName Le nom de l'étiquette à supprimer.
     * @return Le nombre de lignes affectées par l'opération de suppression.
     */
    public static int deleteLabel(String labelName) {
        return DatabaseManager.delete("labels",
                "label_name",
                labelName
        );
    }

}