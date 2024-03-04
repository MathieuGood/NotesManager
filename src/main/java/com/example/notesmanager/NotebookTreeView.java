package com.example.notesmanager;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;


public class NotebookTreeView {

    private  TreeView<String> binderTree;
    private  Notebook notebook;

    public  NotebookTreeView(TreeView<String> binderTree, Notebook notebook) {
        this.binderTree = binderTree;
        this.notebook = notebook;
    }



    public void createTreeView() {

        TreeItem<String> rootItem = new TreeItem<>("Classeurs");
        binderTree.setRoot(rootItem);
        binderTree.setShowRoot(true);

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

                    tabItem.getChildren().add(noteItem);
                }

                binderItem.getChildren().add(tabItem);
            }

            rootItem.getChildren().add(binderItem);
        }
    }

    private Node getColorCircle(String colorHex) {
        Circle circle = new Circle(5, Color.web(colorHex));
        return circle;
    }

}
