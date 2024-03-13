package com.example.notesmanager;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;

/**
 * Cette classe représente la vue en arborescence d'un carnet de notes.
 * Elle contient des méthodes pour créer la vue en arborescence et obtenir un cercle de couleur pour représenter les couleurs des classeurs, onglets et notes.
 */
public class NotebookTreeView {


    /**
     * L'arbre de classeurs à afficher.
     */
    private TreeView<String> binderTree;


    /**
     * Le carnet de notes à afficher.
     */
    private Notebook notebook;


    /**
     * L'élément racine de l'arbre de classeurs.
     */
    TreeItem<String> rootItem = new TreeItem<>("Main node");


    /**
     * Constructeur de la classe NotebookTreeView.
     *
     * @param binderTree L'arbre de classeurs à afficher.
     * @param notebook   Le carnet de notes à afficher.
     */
    public NotebookTreeView(TreeView<String> binderTree, Notebook notebook) {
        this.binderTree = binderTree;
        this.notebook = notebook;
    }


    /**
     * Cette méthode crée la vue en arborescence du carnet de notes.
     * Elle parcourt tous les classeurs, onglets et notes du carnet de notes et les ajoute à l'arbre de classeurs.
     */
    public void createTreeView() {

        System.out.println("> Creating tree view");

        binderTree.setRoot(rootItem);
        binderTree.setShowRoot(false);

        ArrayList<Binder> binders = notebook.getBinders();
        for (Binder binder : binders) {
            String binderColorHex = binder.getColorHex();
            TreeItem<String> binderItem = new TreeItem<>(binder.getBinderName());
            binderItem.setGraphic(NotebookColor.getColorCircle(binderColorHex));

            ArrayList<Tab> tabs = binder.getTabs();
            for (Tab tab : tabs) {

                TreeItem<String> tabItem = new TreeItem<>(tab.getTabName());
                tabItem.setGraphic(NotebookColor.getColorCircle(tab.getColorHex()));

                ArrayList<Note> notes = tab.getNotes();
                for (Note note : notes) {

                    TreeItem<String> noteItem = new TreeItem<>(note.getNoteName());

                    // Affiche le contenu de la note dans la zone de texte lorsqu'on clique sur la note.
                    noteItem.addEventHandler(TreeItem.treeNotificationEvent(), event -> {
                        System.out.println("Clicked on note: " + note.getNoteName());
                    });

                    tabItem.getChildren().add(noteItem);
                }
                binderItem.getChildren().add(tabItem);
            }
            rootItem.getChildren().add(binderItem);
        }


        binderTree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                TreeItem<String> item = binderTree.getSelectionModel().getSelectedItem();
                if (item != null && item.getParent() != null && item.getParent().getParent() != null) {
                    TreeItem<String> parent = item.getParent();
                    TreeItem<String> grandParent = parent.getParent();
                    if (rootItem.getChildren().contains(grandParent)) {
                        String noteName = item.getValue();

                        Note note = notebook.getNoteFromBinderTabNoteName(noteName, parent.getValue(), grandParent.getValue());

                        NoteArea.setContentInNoteArea(note);

                        if (!NoteArea.getNoteSelectedPaneStatus()) {
                            NoteArea.setPaneNoteContentVisible();
                        }

                        System.out.println(NoteArea.getNoteSelectedPaneStatus());
                    }
                }
            }
        });
    }

}