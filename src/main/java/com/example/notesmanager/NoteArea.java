package com.example.notesmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.scene.web.HTMLEditor;

import java.util.ArrayList;


/**
 * La classe NoteArea représente la zone de note dans l'interface utilisateur.
 * Elle contient des méthodes pour gérer l'affichage et l'édition des notes.
 */
public class NoteArea {


    /**
     * L'objet Note associé à cette NoteArea.
     */
    static Note note;


    /**
     * L'objet Label utilisé pour afficher le titre de la note.
     */
    static Label noteTitle;


    /**
     * L'objet Label utilisé pour afficher les étiquettes de la note.
     */
    static Label noteLabels;


    /**
     * Le bouton MenuButton utilisé pour choisir une étiquette pour la note.
     */
    static MenuButton btnChooseLabel;


    /**
     * L'objet HTMLEditor utilisé pour afficher et modifier le contenu de la note.
     */
    static HTMLEditor noteArea;


    /**
     * L'objet Pane utilisé pour afficher tous les éléments.
     */
    @FXML
    private static Pane noteSelectedPane;


    /**
     * L'objet Pane utilisé pour afficher le message d'attente.
     */
    @FXML
    private static Pane waitingNoteSelectedPane;


    /**
     * Récupère l'objet Note associé à cette NoteArea.
     *
     * @return L'objet Note associé à cette NoteArea.
     */
    public static Note getNote() {
        return note;
    }


    /**
     * Vérifie si le Pane de la note sélectionnée est visible.
     *
     * @return Un booléen indiquant si le Pane de la note sélectionnée est visible.
     */
    public static boolean getNoteSelectedPaneStatus() {
        return noteSelectedPane.isVisible();
    }


    /**
     * Cette méthode définit le contenu de la zone de note en fonction de la note donnée.
     * Elle récupère le contenu de la note, met à jour le texte de la zone de note et du titre de la note,
     * met à jour le texte des étiquettes de la note et active le bouton de choix d'étiquette.
     * Elle efface également les coches des éléments de menu du bouton de choix d'étiquette et ajoute des coches
     * aux étiquettes déjà attachées à la note.
     *
     * @param note La note dont le contenu doit être affiché dans la zone de note.
     */
    public static void setContentInNoteArea(Note note) {
        NoteArea.note = note;
        note.fetchNoteContent();

        noteArea.setHtmlText(note.getNoteContent());
        noteTitle.setText(note.getNoteName());
        NoteArea.setLabelsText();
        btnChooseLabel.setDisable(false);

        // Clear the checkmarks from the btnChooseLabel MenuItems
        btnChooseLabel.getItems().forEach(menuItem -> menuItem.setGraphic(null));

        // In the btnChooseLabel MenuItems, add checkmarks to the labels already attached to the note
        btnChooseLabel.getItems().forEach(menuItem -> {
            System.out.println("Print output of the ArrayList note.getLabels() :");
            note.getLabels().forEach(
                    label -> {
                        System.out.println(label.getLabelName());
                        if (label.getLabelName().equals(menuItem.getText())) {
                            menuItem.setGraphic(new Label("✔"));
                        }
                    }
            );
        });

    }


    /**
     * Cette méthode met à jour le texte des étiquettes de la note dans la zone de note.
     * Elle récupère les étiquettes de la note, les concatène en une seule chaîne et met à jour le texte des étiquettes de la note.
     * Elle affiche également des informations de débogage dans la console.
     */
    public static void setLabelsText() {

        ArrayList<NoteLabel> labels = note.getLabels();

        String allLabels = "";

        for (int i = 0; i < labels.size(); i++) {
            if (i == labels.size() - 1) allLabels += labels.get(i).getLabelName();
            else allLabels += labels.get(i).getLabelName() + " - ";
        }

        noteLabels.setText(allLabels);

        System.out.println("CALLING setLabelsText() :");
        System.out.println("Note ID : " + note.getNoteID());
        System.out.println("Note Name : " + note.getNoteName());
        System.out.println("Note Labels : " + allLabels);
    }


    /**
     * Cette méthode initialise les composants de l'interface utilisateur de la zone de note.
     * Elle prend en paramètre les objets HTMLEditor, Pane, Label et MenuButton qui sont utilisés pour afficher et modifier les notes.
     * Elle met également à jour la visibilité des panneaux et désactive le bouton de choix d'étiquette.
     *
     * @param noteArea                L'objet HTMLEditor utilisé pour afficher et modifier le contenu de la note.
     * @param noteSelectedPane        L'objet Pane utilisé pour afficher tous les éléments.
     * @param waitingNoteSelectedPane L'objet Pane utilisé pour afficher le message d'attente.
     * @param noteTitle               L'objet Label utilisé pour afficher le titre de la note.
     * @param noteLables              L'objet Label utilisé pour afficher les étiquettes de la note.
     * @param btnChooseLabel          Le bouton MenuButton utilisé pour choisir une étiquette pour la note.
     */
    public static void setNoteArea(
            HTMLEditor noteArea,
            Pane noteSelectedPane,
            Pane waitingNoteSelectedPane,
            Label noteTitle,
            Label noteLables,
            MenuButton btnChooseLabel) {
        NoteArea.noteArea = noteArea;
        NoteArea.noteSelectedPane = noteSelectedPane;
        NoteArea.waitingNoteSelectedPane = waitingNoteSelectedPane;
        NoteArea.noteTitle = noteTitle;
        NoteArea.noteLabels = noteLables;
        NoteArea.btnChooseLabel = btnChooseLabel;

        noteSelectedPane.setVisible(false);
        waitingNoteSelectedPane.setVisible(true);
        btnChooseLabel.setDisable(true);
    }


    /**
     * Cette méthode met à jour la visibilité des panneaux de la zone de note.
     * Elle rend le panneau de la note sélectionnée visible et le panneau d'attente invisible.
     */
    public static void setPaneNoteContentVisible() {
        noteSelectedPane.setVisible(true);
        waitingNoteSelectedPane.setVisible(false);
    }

}


    
