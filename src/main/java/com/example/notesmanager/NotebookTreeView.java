package com.example.notesmanager;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * This class represents a tree view of a notebook.
 * It contains methods to create and manage the tree view.
 */
public class NotebookTreeView {


    /**
     * The TreeView object that represents the binder tree.
     */
    private TreeView<String> binderTree;

    /**
     * The Notebook object that contains the data for the notebook.
     */
    private Notebook notebook;

    /**
     * The root item of the tree view. It is a TreeItem object with the name "Main node".
     */
    TreeItem<String> rootItem = new TreeItem<>("Main node");


    /**
     * Constructor for the NotebookTreeView class.
     *
     * @param binderTree The tree view object
     * @param notebook   The notebook object
     */
    public NotebookTreeView(TreeView<String> binderTree, Notebook notebook) {
        this.binderTree = binderTree;
        this.notebook = notebook;
    }


    /**
     * This method creates the tree view.
     * It iterates over the binders, tabs, and notes in the notebook and adds them to the tree view.
     */
    public void createTreeView() {

        System.out.println(">>>>>>>> Creating tree view");

        binderTree.setRoot(rootItem);
        binderTree.setShowRoot(false);

        ArrayList<Binder> binders = notebook.getBinders();
        for (Binder binder : binders) {
            String binderColorHex = binder.getColorHex();
            TreeItem<String> binderItem = new TreeItem<>(binder.getBinderName());
            binderItem.setGraphic(getColorCircle(binderColorHex));

            ArrayList<Tab> tabs = binder.getTabs();
            for (Tab tab : tabs) {

                TreeItem<String> tabItem = new TreeItem<>(tab.getTabName());
                tabItem.setGraphic(getColorCircle(tab.getColorHex()));

                ArrayList<Note> notes = tab.getNotes();
                for (Note note : notes) {

                    TreeItem<String> noteItem = new TreeItem<>(note.getNoteName());

                    // Print note name on click on a note in the tree view
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


    /**
     * This method creates a circle with the specified color.
     *
     * @param colorHex The color of the circle in hexadecimal format
     * @return A Node object representing the circle
     */
    private Node getColorCircle(String colorHex) {
        return new Circle(5, Color.web(colorHex));
    }
}