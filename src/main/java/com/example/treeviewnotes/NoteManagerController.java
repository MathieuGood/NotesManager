package com.example.treeviewnotes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Optional;

public class NoteManagerController {

    @FXML
    private TreeView<String> binderTree;
    @FXML
    private TextArea noteArea;

    //  chargement du FXML
    public void initialize() {
        TreeItem<String> rootNode = new TreeItem<>("Classeurs");
        rootNode.setExpanded(true);
        binderTree.setRoot(rootNode);
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
}
