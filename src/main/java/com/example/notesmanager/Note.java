package com.example.notesmanager;

import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Classe Note qui représente une note dans l'application NotesManager.
 * Une note est associée à un onglet spécifique, a un identifiant unique, un nom, un contenu et peut avoir des étiquettes.
 */
public class Note {


    /**
     * Liste des étiquettes associées à cette note.
     */
    private ArrayList<NoteLabel> labels = new ArrayList<>();


    /**
     * L'identifiant unique de cette note.
     */
    private final int noteID;


    /**
     * L'identifiant de l'onglet auquel cette note est associée.
     */
    private final int tabID;


    /**
     * Le nom de cette note.
     */
    private String noteName;


    /**
     * Le contenu de cette note.
     */
    private String noteContent;


    /**
     * Le gestionnaire d'étiquettes pour cette note.
     */
    private LabelManager notebookLabel;


    /**
     * Constructeur de la classe Note.
     *
     * @param tab      L'onglet auquel cette note est associée.
     * @param noteID   L'identifiant unique de cette note.
     * @param noteName Le nom de cette note.
     * @param labels   La liste des étiquettes associées à cette note.
     */
    public Note(
            Tab tab,
            int noteID,
            String noteName,
            ArrayList<NoteLabel> labels
    ) {
        this.noteID = noteID;
        this.tabID = tab.getTabID();
        this.noteName = noteName;
        this.labels = labels;
    }


    /**
     * Récupère l'identifiant unique de cette note.
     *
     * @return L'identifiant unique de cette note.
     */
    public int getNoteID() {
        return noteID;
    }


    /**
     * Récupère le nom de cette note.
     *
     * @return Le nom de cette note.
     */
    public String getNoteName() {
        return noteName;
    }


    /**
     * Récupère la liste des étiquettes associées à cette note.
     *
     * @return La liste des étiquettes associées à cette note.
     */
    public ArrayList<NoteLabel> getLabels() {
        return labels;
    }


    /**
     * Récupère le contenu de cette note.
     *
     * @return Le contenu de cette note.
     */
    public String getNoteContent() {
        return noteContent;
    }


    /**
     * Récupère le contenu de cette note depuis la base de données si celui-ci est null.
     */
    public void fetchNoteContent() {
        if (noteContent == null) {
            ResultSet resultSet = DatabaseManager.select(
                    "notes",
                    new String[]{"note_content"},
                    new String[]{"note_id"},
                    new String[]{String.valueOf(this.noteID)}
            );

            try {
                resultSet.first();
                noteContent = resultSet.getString(1);
            } catch (SQLException e) {
                System.out.println("Error : " + e);
            }
        }
    }


    /**
     * Modifie le nom de cette note dans la base de données.
     *
     * @param newName Le nouveau nom de cette note.
     * @return Le nombre de lignes affectées par l'opération de mise à jour.
     */
    public int editName(String newName) {
        int result = DatabaseManager.update(
                "notes",
                "note_name",
                newName,
                "note_id",
                String.valueOf(this.noteID)
        );

        if (result > 0) {
            this.noteName = newName;
        }

        return result;
    }


    /**
     * Modifie le contenu de cette note dans la base de données.
     *
     * @param newContent Le nouveau contenu de cette note.
     * @return Le nombre de lignes affectées par l'opération de mise à jour.
     */
    public int editContent(String newContent) {
        int result = DatabaseManager.update(
                "notes",
                "note_content",
                newContent,
                "note_id",
                String.valueOf(this.noteID)
        );

        if (result > 0) {
            this.noteContent = newContent;
        }

        return result;
    }


    /**
     * Cette méthode attache une étiquette à cette note.
     *
     * @param labelName Le nom de l'étiquette à attacher.
     * @return Le nombre de lignes affectées par l'opération de mise à jour.
     */
    public int attachLabelToNote(String labelName) {
        // Initialisation du gestionnaire d'étiquettes
        notebookLabel = new LabelManager();
        // Récupération de l'ID de l'étiquette à partir du nom de l'étiquette
        int labelID = notebookLabel.getLabelID(labelName);

        // Vérification si la note a déjà deux étiquettes ou si l'étiquette choisie est déjà appliquée à la note
        if (labels.size() == 2) {

            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }

            CustomAlert.create(
                    Alert.AlertType.INFORMATION,
                    "Erreur",
                    "Erreur",
                    "Impossible d'appliquer l'étiquette " + labelName + " La note a déjà deux étiquettes",
                    "show"
            );

            return 0;

        } else if (labels.stream().anyMatch(label -> label.getLabelName().equals(labelName))) {
            // Affichage d'un message indiquant que la note a déjà cette étiquette
            System.out.println("Impossible d'appliquer l'étiquette " + labelName + " La note a déjà cette étiquette");
            // Affichage de toutes les étiquettes de la note
            System.out.println("Étiquettes dans la note " + this.noteID + " : ");
            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }

            CustomAlert.create(
                    Alert.AlertType.INFORMATION,
                    "Erreur",
                    "Erreur",
                    "Impossible d'appliquer l'étiquette " + labelName + " La note porte déjà cette étiquette",
                    "show"
            );

            return 0;

        } else {
            // Mise à jour de la note dans la base de données avec la nouvelle étiquette
            String[] inArgs = {
                    // ID de l'étiquette à ajouter
                    String.valueOf(labelID),
                    // ID de la note à laquelle l'étiquette est ajoutée
                    String.valueOf(this.noteID)
            };

            // Appel de la méthode de mise à jour de la base de données
            int result = DatabaseManager.call(
                    "UpdateNoteLabelToNewValue",
                    inArgs,
                    ""
            );

            // Si l'opération de mise à jour a réussi, ajout de l'étiquette à la liste des étiquettes de la note
            if (result > 0) {
                labels.add(new NoteLabel(labelName));
            }

            return result;
        }

    }


    /**
     * Cette méthode détache une étiquette de cette note.
     *
     * @param labelName Le nom de l'étiquette à détacher.
     * @return Le nombre de lignes affectées par l'opération de mise à jour.
     */
    public int detachLabelFromNote(String labelName) {
        // Initialisation du gestionnaire d'étiquettes
        notebookLabel = new LabelManager();
        // Récupération de l'ID de l'étiquette à partir du nom de l'étiquette
        int labelID = notebookLabel.getLabelID(labelName);

        // Préparation des arguments pour la mise à jour de la base de données
        String[] inArgs = {
                // ID de l'étiquette à supprimer
                String.valueOf(labelID),
                // ID de la note de laquelle l'étiquette est supprimée
                String.valueOf(this.noteID)
        };

        // Appel de la méthode de mise à jour de la base de données
        int result = DatabaseManager.call(
                "UpdateNoteLabelToNull",
                inArgs,
                ""
        );

        // Si l'opération de mise à jour a réussi, suppression de l'étiquette de la liste des étiquettes de la note
        if (result > 0) {
            // Affichage du contenu de la liste des étiquettes avant la suppression
            System.out.println("Avant la suppression de l'étiquette " + labelName + " de la note " + this.noteID);
            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }

            // Suppression de l'étiquette de la liste
            labels.removeIf(label -> label.getLabelName().equals(labelName));

            // Affichage du contenu de la liste des étiquettes après la suppression
            System.out.println("Après la suppression de l'étiquette " + labelName + " de la note " + this.noteID);
            for (NoteLabel label : labels) {
                System.out.println(label.getLabelName());
            }
        }

        return result;
    }

}