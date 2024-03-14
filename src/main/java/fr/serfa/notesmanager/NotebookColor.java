package fr.serfa.notesmanager;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe représente les couleurs d'un carnet de notes.
 * Elle contient des méthodes pour récupérer toutes les couleurs, obtenir une couleur par son ID, obtenir le nom d'une couleur par son ID, obtenir l'ID d'une couleur par son nom et obtenir tous les noms de couleurs.
 */
public class NotebookColor {


    /**
     * Un booléen pour vérifier si toutes les couleurs ont été récupérées de la base de données.
     * Si la valeur est false, les couleurs n'ont pas encore été récupérées.
     * Si la valeur est true, les couleurs ont déjà été récupérées.
     */
    private static boolean colorsFetched = false;


    /**
     * Une Map pour stocker les noms des couleurs récupérées de la base de données.
     * La clé est l'ID de la couleur et la valeur est le nom de la couleur.
     */
    private static Map<Integer, String> colorNames;


    /**
     * Une Map pour stocker les valeurs hexadécimales des couleurs récupérées de la base de données.
     * La clé est l'ID de la couleur et la valeur est la valeur hexadécimale de la couleur.
     */
    private static Map<Integer, String> colorHexes;


    /**
     * Cette méthode récupère toutes les couleurs de la base de données.
     * Elle effectue une requête SQL SELECT pour récupérer toutes les couleurs et les stocke dans des Map pour une utilisation ultérieure.
     */
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

    /**
     * Cette méthode récupère la valeur hexadécimale d'une couleur par son ID.
     *
     * @param colorID L'ID de la couleur.
     * @return La valeur hexadécimale de la couleur.
     */
    public static String getHexColorByID(int colorID) {
        fetchAllColors();
        return colorHexes.get(colorID);
    }


    /**
     * Cette méthode récupère le nom d'une couleur par son ID.
     *
     * @param colorID L'ID de la couleur.
     * @return Le nom de la couleur.
     */
    public static String getColorNameByID(int colorID) {
        fetchAllColors();
        return colorNames.get(colorID);
    }


    /**
     * Cette méthode récupère l'ID d'une couleur par son nom.
     *
     * @param colorName Le nom de la couleur.
     * @return L'ID de la couleur, ou 0 si aucune couleur avec ce nom n'est trouvée.
     */
    public static int getColorIDByName(String colorName) {
        fetchAllColors();
        for (Map.Entry<Integer, String> entry : colorNames.entrySet()) {
            if (entry.getValue().equals(colorName)) {
                return entry.getKey();
            }
        }
        return 0;
    }


    /**
     * Cette méthode récupère tous les noms de couleurs.
     *
     * @return Une ArrayList contenant tous les noms de couleurs.
     */
    public static ArrayList<String> getAllColorNames() {
        fetchAllColors();
        return new ArrayList<>(colorNames.values());
    }

    public static Node getColorCircle(String colorHex) {
        return new Circle(5, Color.web(colorHex));
    }



}