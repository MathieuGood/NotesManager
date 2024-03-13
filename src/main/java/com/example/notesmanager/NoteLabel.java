package com.example.notesmanager;

/**
 * Cette classe représente une étiquette de note dans le gestionnaire de notes.
 * Une étiquette est un moyen d'organiser les notes en les regroupant par catégories.
 */
public class NoteLabel {


    /**
     * Le nom de l'étiquette.
     */
    private String labelName;


    /**
     * Constructeur de la classe NoteLabel.
     *
     * @param labelName Le nom de l'étiquette.
     */
    NoteLabel(String labelName) {
        this.labelName = labelName;
    }


    /**
     * Cette méthode récupère le nom de l'étiquette.
     *
     * @return Le nom de l'étiquette.
     */
    public String getLabelName() {
        return labelName;
    }

}