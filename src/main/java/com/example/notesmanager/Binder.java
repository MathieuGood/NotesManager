package com.example.notesmanager;


import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * La classe Binder représente un classeur dans l'application de gestion de notes.
 * Un classeur est associé à un carnet de notes et peut contenir plusieurs onglets.
 * Chaque classeur a un identifiant unique, un nom, une couleur et est associé à un utilisateur spécifique.
 */
public class Binder {


    /**
     * Le carnet de notes parent de ce classeur.
     */
    private Notebook notebook;


    /**
     * Une ArrayList qui contient tous les objets Tab associés à ce classeur.
     */
    private ArrayList<Tab> tabs = new ArrayList<>();


    /**
     * L'identifiant unique pour ce classeur.
     */
    private final int binderID;


    /**
     * L'identifiant unique pour l'utilisateur qui possède ce classeur.
     */
    private final int userID;


    /**
     * Le nom de ce classeur.
     */
    private String binderName;


    /**
     * L'identifiant unique pour la couleur de ce classeur.
     */
    private int binderColorID;


    /**
     * Constructeur pour la classe Binder.
     *
     * @param notebook      Le carnet de notes parent de ce classeur.
     * @param binderID      L'identifiant unique pour ce classeur.
     * @param binderName    Le nom de ce classeur.
     * @param binderColorID L'identifiant unique pour la couleur de ce classeur.
     */
    public Binder(
            Notebook notebook,
            int binderID,
            String binderName,
            int binderColorID
    ) {
        this.binderID = binderID;
        this.notebook = notebook;
        this.userID = notebook.getUserID();
        this.binderName = binderName;
        this.binderColorID = binderColorID;
    }


    /**
     * Récupère l'identifiant unique du classeur.
     *
     * @return L'identifiant unique du classeur.
     */
    public int getBinderID() {
        return this.binderID;
    }


    /**
     * Récupère le nom du classeur.
     *
     * @return Le nom du classeur.
     */
    public String getBinderName() {
        return this.binderName;
    }


    /**
     * Récupère l'identifiant unique de la couleur du classeur.
     *
     * @return L'identifiant unique de la couleur du classeur.
     */
    public int getBinderColorID() {
        return this.binderColorID;
    }


    /**
     * Récupère la liste des onglets associés à ce classeur.
     *
     * @return Une ArrayList contenant tous les objets Tab associés à ce classeur.
     */
    public ArrayList<Tab> getTabs() {
        return tabs;
    }


    /**
     * Ajoute un nouvel onglet à la liste des onglets de ce classeur.
     *
     * @param tab L'onglet à ajouter à la liste.
     */
    public void addTabToList(Tab tab) {
        tabs.add(tab);
    }


    /**
     * Récupère tous les onglets associés à ce classeur à partir de la base de données.
     *
     * @return Une ArrayList contenant tous les objets Tab associés à ce classeur.
     */
    private ArrayList<Tab> fetchAllTabs() {
        System.out.println("\n***");
        System.out.println("fetchAllTabs() for userID " + this.userID + " / binder " + binderID);

        // Les champs à récupérer de la table 'tabs'.
        String[] fields = {
                "tabs.tab_id",
                "tabs.tab_name",
                "tabs.tab_color_id"
        };

        // Le champ de condition pour la requête SELECT.
        String[] conditionFields = {"binder_id"};

        // La valeur de condition pour la requête SELECT.
        String[] conditionValues = {String.valueOf(binderID)};

        // Exécute la requête SELECT et récupère le ResultSet.
        ResultSet resultSet = DatabaseManager.select(
                "tabs",
                fields,
                conditionFields,
                conditionValues
        );

        // Crée une ArrayList pour stocker tous les objets Tab.
        ArrayList<Tab> tabs = new ArrayList<>();

        // Analyse les résultats de la requête pour créer de nouveaux objets Tab et les stocker dans l'ArrayList.
        try {
            while (resultSet.next()) {
                // Récupère les données du resultSet.
                int tabID = resultSet.getInt(1);
                String tabName = resultSet.getString(2);
                int tabColorID = resultSet.getInt(3);

                // Imprime les données du resultSet.
                System.out.println("\t> " + tabID + " / " + tabName + " / " + tabColorID);

                // Crée un nouvel objet Tab avec les données analysées et l'ajoute à l'ArrayList.
                tabs.add(new Tab(this, tabID, tabName, tabColorID));
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
            return null;
        }
        return tabs;
    }


    /**
     * Modifie le nom du classeur dans la base de données.
     *
     * @param newName Le nouveau nom du classeur.
     * @return Le résultat de la mise à jour dans la base de données. Si le résultat est supérieur à 0, la mise à jour a réussi.
     */
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "binders",
                "binder_name",
                newName,
                "binder_id",
                String.valueOf(this.binderID));

        if (result > 0) {
            this.binderName = newName;
        }

        return result;
    }


    /**
     * Modifie la couleur du classeur dans la base de données.
     *
     * @param newColorID Le nouvel identifiant de couleur du classeur.
     * @return Le résultat de la mise à jour dans la base de données. Si le résultat est supérieur à 0, la mise à jour a réussi.
     */
    public int editColor(int newColorID) {
        int result = DatabaseManager.update(
                "binders",
                "binder_color_id",
                String.valueOf(newColorID),
                "binder_id",
                String.valueOf(this.binderID)
        );

        if (result > 0) {
            this.binderColorID = newColorID;
        }

        return result;
    }


    /**
     * Crée un nouvel onglet et l'ajoute à la base de données.
     *
     * @param tabName    Le nom du nouvel onglet.
     * @param tabColorID L'identifiant de couleur du nouvel onglet.
     * @return Le nouvel onglet créé. Si l'identifiant de couleur est négatif, retourne null.
     */
    public Tab createTab(String tabName, int tabColorID) {

        // Vérif si l'ID de couleur n'est pas négatif avant de faire l'insertion
        if (tabColorID < 0) {
            System.err.println("Erreur : L'ID de la couleur est hors de portée.");
            return null;
        }

        System.out.println("\n***");
        System.out.println("createTab() : " + tabName + " / binderID " + binderID + " / colorID " + tabColorID);

        String[] fields = {"tab_name", "binder_id", "tab_color_id"};
        String[] values = {tabName, String.valueOf(binderID), String.valueOf(tabColorID)};

        int tabID = DatabaseManager.insert("tabs", fields, values);

        Tab tab = new Tab(this, tabID, tabName, tabColorID);
        tabs.add(tab);

        return tab;
    }


    /**
     * Supprime un onglet de la base de données.
     *
     * @param tabID L'identifiant unique de l'onglet à supprimer.
     * @return Le résultat de la suppression dans la base de données. Si le résultat est supérieur à 0, la suppression a réussi.
     */
    public int deleteTab(int tabID) {
        System.out.println("\n***");
        System.out.println("deleteTab() : " + " tabID " + tabID);

        int result = DatabaseManager.delete("tabs", "tab_id", String.valueOf(tabID));

        // Si la requête est réussie, supprime l'objet Binder de l'ArrayList
        if (result > 0) {
            for (Tab tab : tabs) {
                if (tab.getTabID() == tabID) {
                    tabs.remove(tab);
                    break;
                }
            }
        }
        return result;
    }


    /**
     * Récupère la couleur hexadécimale du classeur.
     *
     * @return La couleur hexadécimale du classeur.
     */
    public String getColorHex() {
        return NotebookColor.getHexColorByID(this.binderColorID);
    }


    /**
     * Recherche un onglet par son nom.
     *
     * @param tabName Le nom de l'onglet à rechercher.
     * @return L'onglet trouvé. Si aucun onglet n'est trouvé, retourne null.
     */
    public Tab findTabByName(String tabName) {
        for (Tab tab : this.tabs) {
            if (tab.getTabName().equals(tabName)) {
                return tab;
            }
        }
        return null;
    }

}