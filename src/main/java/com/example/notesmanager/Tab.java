package com.example.notesmanager;


import java.util.ArrayList;


/**
 * Cette classe représente un onglet dans le gestionnaire de notes.
 * Un onglet est un moyen d'organiser les notes en les regroupant par catégories.
 */
public class Tab {


    /**
     * Une ArrayList qui contient tous les objets Note associés à cet onglet.
     */
    private ArrayList<Note> notes = new ArrayList<>();


    /**
     * L'identifiant unique de cet onglet.
     */
    private final int tabID;


    /**
     * L'identifiant unique du classeur auquel appartient cet onglet.
     */
    private final int binderID;


    /**
     * Le nom de cet onglet.
     */
    private String tabName;


    /**
     * L'identifiant unique de la couleur de cet onglet.
     */
    private int tabColorID;


    /**
     * Constructeur de la classe Tab.
     * Il initialise un nouvel onglet avec les informations fournies.
     *
     * @param binder     Le classeur auquel cet onglet appartient.
     * @param tabID      L'identifiant unique de cet onglet.
     * @param tabName    Le nom de cet onglet.
     * @param tabColorID L'identifiant unique de la couleur de cet onglet.
     */
    public Tab(
            Binder binder,
            int tabID,
            String tabName,
            int tabColorID
    ) {
        this.tabID = tabID;
        this.binderID = binder.getBinderID();
        this.tabName = tabName;
        this.tabColorID = tabColorID;
    }


    /**
     * Récupère la liste des notes associées à cet onglet.
     *
     * @return Une ArrayList contenant tous les objets Note associés à cet onglet.
     */
    public ArrayList<Note> getNotes() {
        return notes;
    }


    /**
     * Récupère l'identifiant unique de cet onglet.
     *
     * @return L'identifiant unique de cet onglet.
     */
    public int getTabID() {
        return tabID;
    }


    /**
     * Récupère le nom de cet onglet.
     *
     * @return Le nom de cet onglet.
     */
    public String getTabName() {
        return tabName;
    }


    /**
     * Récupère l'identifiant unique de la couleur de cet onglet.
     *
     * @return L'identifiant unique de la couleur de cet onglet.
     */
    public int getTabColorID() {
        return tabColorID;
    }


    /**
     * Ajoute une note à la liste des notes associées à cet onglet.
     *
     * @param note La note à ajouter à la liste.
     */
    public void addNoteToList(Note note) {
        notes.add(note);
    }


    /**
     * Modifie le nom de cet onglet dans la base de données et met à jour le nom de l'onglet dans l'objet.
     *
     * @param newName Le nouveau nom de l'onglet.
     * @return Le nombre de lignes affectées par l'opération de mise à jour dans la base de données.
     */
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "tabs",
                "tab_name",
                newName,
                "tab_id",
                String.valueOf(this.tabID)
        );

        if (result > 0) {
            this.tabName = newName;
        }

        return result;
    }


    /**
     * Modifie l'identifiant de couleur de cet onglet dans la base de données et met à jour l'identifiant de couleur de l'onglet dans l'objet.
     *
     * @param newColorID Le nouvel identifiant de couleur de l'onglet.
     * @return Le nombre de lignes affectées par l'opération de mise à jour dans la base de données.
     */
    public int editColor(int newColorID) {
        int result = DatabaseManager.update(
                "tabs",
                "tab_color_id",
                String.valueOf(newColorID),
                "tab_id",
                String.valueOf(this.tabID)
        );

        if (result > 0) {
            this.tabColorID = newColorID;
        }

        return result;
    }


    /**
     * Crée une nouvelle note avec le nom spécifié, l'ajoute à la base de données et à la liste des notes de cet onglet.
     *
     * @param noteName Le nom de la nouvelle note.
     * @return L'objet Note nouvellement créé.
     */
    public Note createNote(String noteName) {
        System.out.println("\n***");
        System.out.println("createNote() : " + noteName + " / tabID " + tabID);

        String[] fields = {"note_name", "tab_id"};
        String[] values = {noteName, String.valueOf(tabID)};

        int noteID = DatabaseManager.insert("notes", fields, values);

        Note note = new Note(this, noteID, noteName, new ArrayList<NoteLabel>());
        notes.add(note);

        return note;
    }


    /**
     * Supprime une note avec l'identifiant spécifié de la base de données et de la liste des notes de cet onglet.
     *
     * @param noteID L'identifiant de la note à supprimer.
     * @return Le nombre de lignes affectées par l'opération de suppression dans la base de données.
     */
    public int deleteNote(int noteID) {
        System.out.println("\n***");
        System.out.println("deleteNote() : " + " noteID " + noteID);

        int result = DatabaseManager.delete("notes", "note_id", String.valueOf(noteID));

        // Si la suppression a réussi, supprime la note de la liste des notes de cet onglet
        if (result > 0) {
            for (Note note : notes) {
                if (note.getNoteID() == noteID) {
                    notes.remove(note);
                    break;
                }
            }
        }
        return result;
    }


    /**
     * Récupère la couleur hexadécimale associée à l'identifiant de couleur de cet onglet.
     *
     * @return La couleur hexadécimale associée à l'identifiant de couleur de cet onglet.
     */
    public String getColorHex() {
        return NotebookColor.getHexColorByID(this.tabColorID);
    }

}
