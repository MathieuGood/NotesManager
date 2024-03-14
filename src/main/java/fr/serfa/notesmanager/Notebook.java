package fr.serfa.notesmanager;

import java.sql.*;
import java.util.ArrayList;


/**
 * La classe Notebook représente un carnet de notes dans l'application.
 * Un carnet de notes est associé à un utilisateur unique et contient plusieurs classeurs (Binder).
 * Chaque classeur peut contenir plusieurs onglets (Tab) et chaque onglet peut contenir plusieurs notes (Note).
 */
public class Notebook {


    /**
     * Une liste d'objets Binder représentant les classeurs dans le carnet de notes.
     */
    private ArrayList<Binder> binders = new ArrayList<>();


    /**
     * L'ID de l'utilisateur associé à ce carnet de notes.
     */
    private final int userID;


    /**
     * Constructeur de la classe Notebook.
     * Il initialise l'ID de l'utilisateur avec l'ID de l'utilisateur passé en paramètre et remplit le contenu du carnet de notes.
     *
     * @param user L'utilisateur associé à ce carnet de notes.
     */
    public Notebook(User user) {
        this.userID = user.getUserID();
        setNotebookContent();
    }


    /**
     * Récupère l'ID de l'utilisateur associé à ce carnet de notes.
     *
     * @return L'ID de l'utilisateur.
     */
    public int getUserID() {
        return userID;
    }


    /**
     * Récupère la liste des classeurs dans le carnet de notes.
     *
     * @return Une ArrayList contenant les objets Binder du carnet de notes.
     */
    public ArrayList<Binder> getBinders() {
        return binders;
    }


    /**
     * Récupère une note spécifique en fonction de son ID.
     * Parcourt tous les classeurs, onglets et notes jusqu'à trouver la note avec l'ID correspondant.
     *
     * @param noteID L'ID de la note à récupérer.
     * @return L'objet Note correspondant à l'ID donné, ou null si aucune note avec cet ID n'est trouvée.
     */
    public Note getNoteByID(int noteID) {
        for (Binder binder : binders) {
            for (Tab tab : binder.getTabs()) {
                for (Note note : tab.getNotes()) {
                    if (note.getNoteID() == noteID) {
                        return note;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Ajoute un classeur à la liste des classeurs du carnet de notes.
     *
     * @param binder L'objet Binder à ajouter à la liste.
     */
    public void addBinderToList(Binder binder) {
        binders.add(binder);
    }


    /**
     * Cette méthode récupère tout le contenu du carnet de notes à partir de la base de données.
     * Elle effectue une requête SQL SELECT pour récupérer les informations sur les classeurs, les onglets, les notes et les étiquettes associées à l'utilisateur.
     * Les informations récupérées sont ensuite renvoyées sous forme de ResultSet pour être traitées ultérieurement.
     *
     * @return Un ResultSet contenant les informations sur les classeurs, les onglets, les notes et les étiquettes de l'utilisateur.
     */
    public ResultSet fetchAllNotebookContent() {

        System.out.println("\n***");
        System.out.println("getContentTree() for userID " + userID);

        String[] fields = {
                "binders.binder_id",
                "binders.binder_name",
                "binders.binder_color_id",
                "tabs.tab_id",
                "tabs.tab_name",
                "tabs.tab_color_id",
                "notes.note_id",
                "notes.note_name",
                "label1.label_name",
                "label2.label_name"
        };
        String[] conditionFields = {"users.user_id"};
        String[] conditionValues = {String.valueOf(userID)};

        return DatabaseManager.select(
                "binders "
                        + "LEFT JOIN users ON binders.user_id = users.user_id "
                        + "LEFT JOIN tabs ON tabs.binder_id = binders.binder_id "
                        + "LEFT JOIN notes ON notes.tab_id = tabs.tab_id "
                        + "LEFT JOIN labels AS label1 ON notes.note_label1_id = label1.label_id "
                        + "LEFT JOIN labels AS label2 ON notes.note_label2_id = label2.label_id ",
                fields,
                conditionFields,
                conditionValues
        );
    }


    /**
     * Cette méthode initialise le contenu du carnet de notes.
     * Elle appelle la méthode setNotebookContent avec un paramètre null.
     */
    public void setNotebookContent() {
        setNotebookContent(null);
    }


    /**
     * Cette méthode initialise le contenu du carnet de notes en fonction d'un filtre de nom d'étiquette.
     * Elle récupère tout le contenu lié au carnet de notes à partir de la base de données et initialise les ID du classeur et de l'onglet actuels.
     * Elle parcourt ensuite chaque ligne du ResultSet, récupère les données de la ligne actuelle et ajoute les objets Binder, Tab et Note appropriés à la liste des classeurs.
     * Si un filtre de nom d'étiquette est fourni, elle ne saute pas la ligne actuelle si les étiquettes de la note ne correspondent pas au filtre.
     *
     * @param labelNameFilter Le nom de l'étiquette à utiliser comme filtre, ou null si aucun filtre ne doit être appliqué.
     */
    public void setNotebookContent(String labelNameFilter) {
        // Vide la liste des classeurs
        binders = new ArrayList<>();

        // Récupère tout le contenu lié au carnet de notes à partir de la base de données
        ResultSet notebookContent = fetchAllNotebookContent();

        // Initialise les ID du classeur et de l'onglet actuels
        int currentBinderID = 0;
        int currentTabID = 0;

        try {
            // Parcourt chaque ligne du ResultSet
            while (notebookContent.next()) {
                // Récupère les données de la ligne actuelle du ResultSet
                int binderID = notebookContent.getInt(1);
                String binderName = notebookContent.getString(2);
                int binderColorID = notebookContent.getInt(3);
                int tabID = notebookContent.getInt(4);
                String tabName = notebookContent.getString(5);
                int tabColorID = notebookContent.getInt(6);
                int noteID = notebookContent.getInt(7);
                String noteName = notebookContent.getString(8);
                String noteLabel1 = notebookContent.getString(9);
                String noteLabel2 = notebookContent.getString(10);

                // Si un labelNameFilter est fourni, ne pas sauter la ligne actuelle si les étiquettes de la note ne correspondent pas au filtre
                if (labelNameFilter != null) {
                    // Si ni label1 ni label2 ne correspondent au filtre, sauter la ligne actuelle
                    if (!labelNameFilter.equals(noteLabel1) && !labelNameFilter.equals(noteLabel2)) {
                        continue;
                    }
                }

                // Si l'ID du classeur correspond à un nouveau classeur, crée un nouvel objet Binder et l'ajoute à la liste des classeurs
                if (binderID != currentBinderID) {
                    addBinderToList(new Binder(this, binderID, binderName, binderColorID));
                    currentBinderID = binderID;
                }

                // Si l'ID de l'onglet correspond à un nouvel onglet et n'est pas nul, crée un nouvel objet Tab et l'ajoute à la liste des onglets du dernier classeur
                if (tabID != currentTabID && tabID != 0) {
                    binders.getLast().addTabToList(new Tab(binders.getLast(), tabID, tabName, tabColorID));
                    currentTabID = tabID;
                }

                // Si l'ID de la note n'est pas nul, crée un nouvel objet Note et l'ajoute à la liste des notes du dernier onglet du dernier classeur
                if (noteID != 0) {
                    // Crée une liste d'objets LabelNote pour stocker les étiquettes de la note
                    ArrayList<NoteLabel> labels = new ArrayList<>();
                    if (noteLabel1 != null) {
                        labels.add(new NoteLabel(noteLabel1));
                    }
                    if (noteLabel2 != null) {
                        labels.add(new NoteLabel(noteLabel2));
                    }

                    binders.getLast().getTabs().getLast().addNoteToList(new Note(
                            binders.getLast().getTabs().getLast(),
                            noteID,
                            noteName,
                            labels
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e);
        }
    }


    /**
     * Cette méthode crée un nouveau classeur et l'ajoute à la liste des classeurs du carnet de notes.
     * Elle effectue une requête SQL INSERT pour ajouter le nouveau classeur à la base de données.
     * Ensuite, elle crée un nouvel objet Binder avec les informations du nouveau classeur et l'ajoute à la liste des classeurs.
     *
     * @param binderName    Le nom du nouveau classeur.
     * @param binderColorID L'ID de la couleur du nouveau classeur.
     * @return L'objet Binder du nouveau classeur.
     */
    public Binder createBinder(String binderName, int binderColorID) {
        System.out.println("\n***");
        System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + binderColorID);

        String[] fields = {"binder_name", "user_id", "binder_color_id"};
        String[] values = {binderName, String.valueOf(userID), String.valueOf(binderColorID)};

        int binderID = DatabaseManager.insert("binders", fields, values);

        Binder binder = new Binder(this, binderID, binderName, binderColorID);
        binders.add(binder);

        return binder;
    }


    /**
     * Cette méthode supprime un classeur de la liste des classeurs du carnet de notes.
     * Elle effectue une requête SQL DELETE pour supprimer le classeur de la base de données.
     * Si la requête est réussie, elle supprime l'objet Binder correspondant de la liste des classeurs.
     *
     * @param binderID L'ID du classeur à supprimer.
     * @return Le nombre de lignes affectées par la requête SQL DELETE, ou 0 en cas d'échec.
     */
    public int deleteBinder(int binderID) {
        System.out.println("\n***");
        System.out.println("deleteBinder() : " + " binderID " + binderID);

        int result = DatabaseManager.delete("binders", "binder_id", String.valueOf(binderID));

        // Si la requête est réussie, supprime l'objet Binder de la liste des classeurs
        if (result > 0) {

            for (Binder binder : binders) {
                if (binder.getBinderID() == binderID) {
                    binders.remove(binder);
                    break;
                }
            }
        }
        return result;
    }


    /**
     * Cette méthode récupère un classeur spécifique en fonction de son nom.
     * Elle parcourt tous les classeurs jusqu'à trouver le classeur avec le nom correspondant.
     *
     * @param binderName Le nom du classeur à récupérer.
     * @return L'objet Binder correspondant au nom donné, ou null si aucun classeur avec ce nom n'est trouvé.
     */
    public Binder getBinderByName(String binderName) {
        for (Binder binder : this.binders) {
            if (binder.getBinderName().equalsIgnoreCase(binderName)) {
                return binder;
            }
        }
        return null;
    }


    /**
     * Cette méthode récupère un onglet spécifique en fonction de son nom.
     * Elle parcourt tous les classeurs et onglets jusqu'à trouver l'onglet avec le nom correspondant.
     *
     * @param tabName Le nom de l'onglet à récupérer.
     * @return L'objet Tab correspondant au nom donné, ou null si aucun onglet avec ce nom n'est trouvé.
     */
    public Tab getTabByName(String tabName) {
        for (Binder binder : binders) {
            for (Tab tab : binder.getTabs()) {
                if (tab.getTabName().equalsIgnoreCase(tabName)) {
                    return tab;
                }
            }
        }
        return null;
    }


    /**
     * Cette méthode récupère une note spécifique en fonction de son nom, du nom de l'onglet et du nom du classeur.
     * Elle parcourt tous les classeurs, onglets et notes jusqu'à trouver la note avec le nom correspondant dans l'onglet et le classeur spécifiés.
     *
     * @param noteName   Le nom de la note à récupérer.
     * @param tabName    Le nom de l'onglet contenant la note.
     * @param binderName Le nom du classeur contenant l'onglet.
     * @return L'objet Note correspondant aux noms donnés, ou null si aucune note avec ce nom n'est trouvée dans l'onglet et le classeur spécifiés.
     */
    public Note getNoteFromBinderTabNoteName(String noteName, String tabName, String binderName) {

        ArrayList<Binder> binders = this.getBinders();
        for (Binder binder : binders) {

            if (binder.getBinderName().equals(binderName)) {
                System.out.println("Binder " + binderName + " found.");

                ArrayList<Tab> tabs = binder.getTabs();
                for (Tab tab : tabs) {
                    if (tab.getTabName().equals(tabName)) {
                        System.out.println("Tab " + tabName + " found.");

                        ArrayList<Note> notes = tab.getNotes();
                        for (Note note : notes) {
                            if (note.getNoteName().equals(noteName)) {
                                return note;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


}
