package com.example.treeviewnotes;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Optional;

public class NoteManagerApp extends Application {

    @FXML
    private TreeView<String> binderTree;
    @FXML
    private TextArea noteArea;


    @Override
    public void start(Stage primaryStage) {
        // racine  de l'UI
        BorderPane root = new BorderPane();
        this.binderTree = createTreeView();
        this.noteArea = new TextArea();

        //  HBox pour les boutons ( alignement horizontale)
        HBox buttonBar = new HBox();
        buttonBar.setSpacing(10);

        Button addBinderButton = new Button("Ajouter Classeur");
        addBinderButton.setOnAction(event -> createNewBinder());

        Button addDividerButton = new Button("Ajouter Intercalaire");
        addDividerButton.setOnAction(event -> createNewDivider());

        Button addNoteButton = new Button("Ajouter Note");
        addNoteButton.setOnAction(event -> createNewNote());

        // Ajout des boutons à la HBox
        buttonBar.getChildren().addAll(addBinderButton, addDividerButton, addNoteButton);

        // Fixation du HBox en haut du BorderPane
        root.setTop(buttonBar);

        // Ajout du TreeView et du TextArea au centre et à droite
        HBox centerBox = new HBox(binderTree, noteArea);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("NoteManagerApp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private TreeView<String> createTreeView() {
        TreeItem<String> rootNode = new TreeItem<>("Classeurs");
        rootNode.setExpanded(true);

        TreeView<String> tree = new TreeView<>(rootNode);
        tree.setShowRoot(true);

        return tree;
    }

    @FXML
    private void createNewBinder() {
        TextInputDialog dialog = new TextInputDialog("Nom du Classeur");
        dialog.setTitle("Création d'un Nouveau Classeur");
        dialog.setHeaderText("Entrez le nom du nouveau classeur :");
        dialog.setContentText("Nom :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            TreeItem<String> newBinder = new TreeItem<>(name);
            binderTree.getRoot().getChildren().add(newBinder);
        });
    }

    @FXML
    private void createNewDivider() {
        TreeItem<String> selectedBinder = binderTree.getSelectionModel().getSelectedItem();

        // Vérification si l'élément sélectionné est  sous la racine
        if (selectedBinder != null && selectedBinder.getParent() == binderTree.getRoot()) {
            TextInputDialog dialog = new TextInputDialog("Nom de l'Intercalaire");
            dialog.setTitle("Création d'un Nouvel Intercalaire");
            dialog.setHeaderText("Entrez le nom du nouvel intercalaire :");
            dialog.setContentText("Nom :");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                TreeItem<String> newDivider = new TreeItem<>(name);
                selectedBinder.getChildren().add(newDivider);
            });
        } else {
            showAlert("Veuillez sélectionner un classeur pour ajouter un intercalaire.");
        }
    }

    @FXML
    private void createNewNote() {
        TreeItem<String> selectedDivider = binderTree.getSelectionModel().getSelectedItem();

        // Vérification si l'élément sélectionné est un intercalaire (a un parent qui n'est pas la racine).
        if (selectedDivider != null && selectedDivider.getParent() != null && selectedDivider.getParent().getParent() == binderTree.getRoot()) {
            TextInputDialog dialog = new TextInputDialog("Nom de la Note");
            dialog.setTitle("Création d'une Nouvelle Note");
            dialog.setHeaderText("Entrez le nom de la nouvelle note :");
            dialog.setContentText("Nom :");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                TreeItem<String> newNote = new TreeItem<>(name);
                selectedDivider.getChildren().add(newNote);
            });
        } else {
            showAlert("Veuillez sélectionner un intercalaire pour ajouter une note.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Action Requise");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
