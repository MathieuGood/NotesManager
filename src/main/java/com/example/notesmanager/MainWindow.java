package com.example.notesmanager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;



public class MainWindow extends Application {


    
    private Parent root;

    
    @FXML
    Label userNameLabel;

    
    @FXML
    Label noteTitle;

    
    @FXML
    Label noteLabels;

    
    @FXML
    private Pane noteSelectedPane;

    
    @FXML
    private Pane waitingNoteSelectedPane;

    
    @FXML
    Button btnLogOut, btnSave;

    Button btnCreateLabel, btnEditLabel, btnDeleteLabel;

    TextField nameField;

    ComboBox<String> categoryBox;

    
    @FXML
    MenuButton btnFilterLabel;

    @FXML
    MenuButton btnChooseLabel;

    
    @FXML
    HTMLEditor noteArea;

    
    @FXML
    private TreeView<String> binderTree;

    
    @FXML
    MenuButton btnCreateAction;

    
    private static User user;

    
    Notebook notebook;


    @Override
    public void start(Stage stage) throws Exception {

    }


    @FXML
    public void initialize() {
        //set size constraint
        Platform.runLater(() -> {
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setResizable(true);
            stage.setMinWidth(850.0);
            stage.setMinHeight(500.0);
        });

        // Set the text of the userNameLabel to greet the user
        userNameLabel.setText(user.getUserName());

        // Initialize the notebook with the current user
        notebook = new Notebook(user);

        // Initialize blank NoteArea in the HTMLEditor from the user interface
        NoteArea.setNoteArea(noteArea, noteSelectedPane, waitingNoteSelectedPane, noteTitle, noteLabels, btnChooseLabel);

        // Set the content of note filter dropdown menu
        setLabelFilterDropdownContent(btnFilterLabel, this::setLabelFilter, false);

        // Set the content of note label selector dropdown menu
        setLabelFilterDropdownContent(btnChooseLabel, this::setNoteLabel, false);

        // Generate the tree view for the notebook
        generateTreeView();
    }


    
    public void logOut(ActionEvent event) throws IOException {

        Alert alert = CustomAlert.create(Alert.AlertType.CONFIRMATION, "LogOut", "You're about to logout", "Do you want to save before existing", null);

        // Show the alert and wait for the user's response
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // If the user confirmed, load the LoginWindow
                root = FXMLLoader.load(getClass().getResource("loginWindow.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                // If an I/O error occurs, throw a new RuntimeException
                throw new RuntimeException(ex);
            }
        }
    }


    
    public void saveNote(ActionEvent e) {
        String content = noteArea.getHtmlText();
        NoteArea.note.editContent(content);

        CustomAlert.create(Alert.AlertType.INFORMATION, "Confirmation", null, "Note bien sauvegardé", "show");
        System.out.println("btn save note");
    }


    public void setLabelFilter(ActionEvent e) {
        // Check if the source of the event is a MenuItem
        if (e.getSource() instanceof MenuItem selectedLabel) {
            // Retrieve name of the label
            String labelName = selectedLabel.getText();
            System.out.println(labelName);

            // If the label name is "All labels", set the notebook content to all notes
            if (labelName.equals("All labels")) {
                // Reset the style of all MenuItems
                btnFilterLabel.setText("All labels");

                notebook.setNotebookContent();
                generateTreeView();
            } else {
                // Filter notes by label name and rerender the tree view
                notebook.setNotebookContent(labelName);

                // Change the text of the selected label to surround it with brackets
                btnFilterLabel.setText(labelName);

                // Render the tree view for the notebook
                generateTreeView();

                // Unfold all the binders and tabs in the tree view
                for (TreeItem<String> binder : binderTree.getRoot().getChildren()) {
                    binder.setExpanded(true);
                    for (TreeItem<String> tab : binder.getChildren()) {
                        tab.setExpanded(true);
                    }
                }
            }
        }
    }

    private void updateNoteAre(Note note) {
        notebook.setNotebookContent();
        generateTreeView();
        int noteID = note.getNoteID();
        Note newNote = notebook.getNoteByID(noteID);
        NoteArea.setContentInNoteArea(newNote);
        NoteArea.setLabelsText();
    }


    public void setNoteLabel(ActionEvent e) {

        System.out.println("\n******************* FUNCTION setNoteLabel");
        // Get current Note object from NoteArea
        Note note = NoteArea.getNote();

        // Check if the source of the event is a MenuItem
        if (e.getSource() instanceof MenuItem selectedLabel && note != null) {
            // Retrieve name of the label
            String labelName = selectedLabel.getText();
            System.out.println("Selected label" + labelName);

            System.out.println("Listing labels in note : " + note.getNoteName());
            // If the selected label is already in the note, delete it from the note, else add it to the note
            ArrayList<NoteLabel> labels = note.getLabels();
            for (NoteLabel label : labels) {
                System.out.println("Label : " + label.getLabelName());
                if (label.getLabelName().equals(labelName)) {
                    System.out.println("Label already in note. Removing it.");
                    int removeLabelResult = note.detachLabelFromNote(labelName);
                    if (removeLabelResult > 0) {
                        // Remove the checkmark sign before the corresponding label in the dropdown menu
                        selectedLabel.setGraphic(null);
                    }

                    updateNoteAre(note);
                    return;
                }
            }

            // Add the selected label to the note
            System.out.println("Adding label " + labelName + " to note " + note.getNoteName());
            int addLabelResult = note.attachLabelToNote(labelName);
            if (addLabelResult > 0) {
                System.out.println("Label added to note.");
                // Add a checkmark sign before the corresponding label in the dropdown menu
                selectedLabel.setGraphic(new Label("✔"));

                updateNoteAre(note);
            }

        }

    }


    public void handleLabels() {
        System.out.println("handle labels function");

        createDialog();
    }


    // Create a dialog to handle labels
    public void createDialog() {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("");
        dialog.setHeaderText("This is a custom dialog");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Intitulé du label:");
        nameField = new TextField();
        nameField.setPrefWidth(400);

        // Créez la boîte de disposition horizontale pour contenir les boutons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10); // Définit l'espacement entre les boutons

        // Créez les boutons de suppression et de modification
        btnCreateLabel = new Button("Ajouter");
        btnEditLabel = new Button("Modifier");
        btnDeleteLabel = new Button("Supprimer");


        // Ajoutez les boutons à la boîte de disposition horizontale
        buttonBox.getChildren().addAll(btnCreateLabel, btnEditLabel, btnDeleteLabel);

        // Desactiver les boutons initialement
        btnDeleteLabel.setDisable(true);
        btnEditLabel.setDisable(true);

        Label categoryLabel = new Label("Liste des labels:");
        categoryBox = new ComboBox<>();

        // Fetch all labels from the database and add them to the categoryBox
        Map<Integer, String> labels = LabelManager.getAllLabels();
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {
            categoryBox.getItems().add(entry.getValue());
        }

        categoryBox.setOnAction(event -> {
            String selectedValue = categoryBox.getValue();
            nameField.setText(selectedValue);

            btnEditLabel.setDisable(false);
            btnDeleteLabel.setDisable(false);
        });

        categoryBox.setPrefWidth(400);

        // Ajoutez la boîte de disposition horizontale à la grille
        grid.add(buttonBox, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(categoryLabel, 0, 2);
        grid.add(categoryBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        btnCreateLabel.setOnAction(e -> actionsLabel(nameField, "create", null));
        btnEditLabel.setOnAction(e -> actionsLabel(nameField, "edit", categoryBox.getValue()));
        btnDeleteLabel.setOnAction(e -> actionsLabel(nameField, "delete", null));

        dialog.showAndWait();
    }

    public void actionsLabel(TextField nameField, String action, String selectedLabel) {

        String labelField = nameField.getText();
        String headerText = null;
        String contentText = null;

        Alert alert = CustomAlert.create(Alert.AlertType.CONFIRMATION, "Statut", null, null, null);

        if (FormatChecker.checkLabelFormat(labelField)) {
            int result;

            if (action.equals("create") && !categoryBox.getItems().contains(labelField))
                result = LabelManager.createLabel(labelField);
            else if (action.equals("edit") && !categoryBox.getItems().contains(labelField))
                result = LabelManager.updateLabel(selectedLabel, labelField);
            else if (action.equals("delete"))
                result = LabelManager.deleteLabel(labelField);
            else
                result = -1;

            System.out.println("result " + result);
            if (result > 0) {
                switch (action) {
                    case "create" -> {
                        // Add the new label to the categoryBox
                        categoryBox.getItems().add(labelField);
                        headerText = "Insertion réussie";
                        contentText = "Le label " + labelField + " est bien inséré en BDD";
                    }
                    case "edit" -> {
                        // Update text in categoryBox to the new label name
                        categoryBox.getItems().set(categoryBox.getItems().indexOf(selectedLabel), labelField);
                        headerText = "Update réussie";
                        contentText = "Le label " + labelField + " est bien modifié en BDD";
                    }
                    case "delete" -> {
                        // Remove the selected label from the categoryBox
                        categoryBox.getItems().remove(selectedLabel);
                        headerText = "delete réussie";
                        contentText = "Le label " + labelField + " est bien supprimer en BDD";
                    }
                }

                // Set the alert's header and content text
                CustomAlert.setHeader(headerText);
                CustomAlert.setContent(contentText);

                setLabelFilterDropdownContent(btnFilterLabel, this::setLabelFilter, true);
                setLabelFilterDropdownContent(btnChooseLabel, this::setNoteLabel, false);
                notebook.setNotebookContent();

                // Obtain the ID of the note in NoteArea
                Note currentNote = NoteArea.getNote();

                if (currentNote != null) {
                    int noteID = currentNote.getNoteID();
                    // Get the note from the notebook, set the content in the NoteArea, and update the labels text
                    Note note = notebook.getNoteByID(noteID);
                    NoteArea.setContentInNoteArea(note);
                    NoteArea.setLabelsText();
                }


            } else if (result == -1) {
                CustomAlert.setHeader("Action impossible");
                CustomAlert.setContent("Le label " + labelField + " existe déjà dans la liste");
                nameField.clear();
            } else {
                CustomAlert.setHeader("Une erreur s'est produit");
                CustomAlert.setContent("Echec dans d'exécution pour le label " + labelField);
            }

            nameField.clear();
        } else {
            System.out.println("Incorrect format for label. Length need to be 4 and 10");
            CustomAlert.setHeader("Impossible to execute the action");
            CustomAlert.setContent("Format of label is incorrect.");
        }

        alert.show();
    }


    
    public static void setUser(User pUser) {
        System.out.println("initUser");
        user = pUser;
        System.out.println(user.toString());
    }


    
    public void setLabelFilterDropdownContent(MenuButton menuButton, EventHandler<ActionEvent> callback, boolean updateLabels) {

        if (updateLabels) {
            LabelManager.updateLabels();
        }

        // Fetch all labels from the database
        Map<Integer, String> labels = LabelManager.getAllLabels();

        // Clear MenuItems from the dropdown menu
        menuButton.getItems().clear();

        // Add a new menu item for all labels
        MenuItem allLabels = new MenuItem("All labels");
        allLabels.setOnAction(callback);
        menuButton.getItems().add(allLabels);

        // Loop over the fetched labels
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {

            // Create a new menu item for each label
            MenuItem label = new MenuItem(entry.getValue());

            // Set the action of the menu item to trigger the callback
            label.setOnAction(callback);

            // Add the menu item to the dropdown menu
            menuButton.getItems().add(label);
        }
    }


    public void generateTreeView() {
        NotebookTreeView notebookTreeView = new NotebookTreeView(binderTree, notebook);
        notebookTreeView.createTreeView();
    }


    @FXML
    private void addBinderMenu() {

        TextInputDialog dialog = new TextInputDialog("Nom du Classeur");
        dialog.setTitle("Création d'un Nouveau Classeur");
        dialog.setHeaderText("Entrez le nom du nouveau classeur :");
        dialog.setContentText("Nom :");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {

            String binderName = result.get();

            List<String> colorNames = NotebookColor.getAllColorNames();
            ChoiceDialog<String> dialogColor = new ChoiceDialog<>(colorNames.getFirst(), colorNames);
            dialogColor.setTitle("Choix de la couleur");
            dialogColor.setHeaderText("Choisissez une couleur pour le nouveau classeur :");
            dialogColor.setContentText("Couleur :");

            Optional<String> colorResult = dialogColor.showAndWait();
            if (colorResult.isPresent()) {
                int binderColorId = NotebookColor.getColorIDByName(colorResult.get());

                Binder newBinder = notebook.createBinder(binderName, binderColorId);

                if(newBinder != null) {
                    CustomAlert.create(Alert.AlertType.INFORMATION,"Information", null, "Création réussi", "show");

                    //  code hexadécimal de la couleur du nouveau classeur
                    String colorHex = NotebookColor.getHexColorByID(binderColorId);
                    Node circle = getColorCircle(colorHex);

                    TreeItem<String> newBinderItem = new TreeItem<>(binderName);
                    newBinderItem.setGraphic(circle);

                    // Ajout du nouveau classeur à la racine de l'arbre
                    binderTree.getRoot().getChildren().add(newBinderItem);
                } else {
                    CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Oups ! Une erreur s'est produite lors de la création du classeur", "show");
                }
            }
        }
    }


    @FXML
    private void editBinderMenu() {
        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() == binderTree.getRoot()) {

            String oldBinderName = selectedItem.getValue();
            Binder selectedBinder = notebook.getBinderByName(oldBinderName);

            if (selectedBinder != null) {

                TextInputDialog nameDialog = new TextInputDialog(selectedBinder.getBinderName());
                nameDialog.setTitle("Modification du Classeur");
                nameDialog.setHeaderText("Modifiez le nom du classeur :");
                nameDialog.setContentText("Nom :");

                Optional<String> nameResult = nameDialog.showAndWait();
                nameResult.ifPresent(newName -> {
                    selectedBinder.editName(newName);
                    selectedItem.setValue(newName);
                });


                List<String> colorNames = NotebookColor.getAllColorNames();
                String currentColorName = NotebookColor.getColorNameByID(selectedBinder.getBinderColorID());
                ChoiceDialog<String> colorDialog = new ChoiceDialog<>(currentColorName, colorNames);
                colorDialog.setTitle("Modification de la Couleur du Classeur");
                colorDialog.setHeaderText("Choisissez une nouvelle couleur pour le classeur :");
                colorDialog.setContentText("Couleur :");

                Optional<String> colorResult = colorDialog.showAndWait();
                colorResult.ifPresent(newColorName -> {
                    int newColorID = NotebookColor.getColorIDByName(newColorName);
                    selectedBinder.editColor(newColorID);

                    String colorHex = NotebookColor.getHexColorByID(newColorID);
                    Node circle = getColorCircle(colorHex);
                    selectedItem.setGraphic(circle);
                });

                CustomAlert.create(Alert.AlertType.INFORMATION,"Erreur", null, "Classeur édité.", "showAndWait");

            } else {
                CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Classeur non trouvé.", "showAndWait");
            }
        } else {
            CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Veuillez sélectionner un classeur à éditer.", "showAndWait");
        }
    }

    @FXML
    private void deleteBinderMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() == binderTree.getRoot()) {
            String binderName = selectedItem.getValue();
            Binder selectedBinder = notebook.getBinderByName(binderName);

            Alert alert = CustomAlert.create(Alert.AlertType.WARNING, "Confirmation de suppression", null, "Supprimer le classeur \" + binderName + \" ?", "Êtes-vous sûr ? Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Suppression du classeur
                int binderID = selectedBinder.getBinderID();
                int deleteResult = notebook.deleteBinder(binderID);

                if (deleteResult > 0) {

                    binderTree.getRoot().getChildren().remove(selectedItem);
                } else {
                    CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Erreur lors de la suppression du classeur.", "showAndWait");
                }
            }
        } else {
            CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Veuillez sélectionner un classeur à supprimer.", "showAndWait");
        }
    }


    
    @FXML
    private void addTabMenu() {
        System.out.println("Selected item: " + binderTree.getSelectionModel().getSelectedItem());

        TreeItem<String> selectedBinder = binderTree.getSelectionModel().getSelectedItem();

        if (selectedBinder != null) {

            System.out.println("Classeur sélectionné : " + selectedBinder.getValue());

            Binder binder = notebook.getBinderByName(selectedBinder.getValue());

            if (binder != null) {

                System.out.println("Binder trouvé avec succès.");

                TextInputDialog dialog = new TextInputDialog("Nouveau Intercalaire");
                dialog.setTitle("Création d'un Nouvel Intercalaire");
                dialog.setHeaderText("Entrez le nom du nouvel intercalaire :");
                dialog.setContentText("Nom :");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String tabName = result.get();

                    List<String> colorNames = NotebookColor.getAllColorNames();
                    ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(colorNames.get(0), colorNames);
                    choiceDialog.setTitle("Choix de la couleur");
                    choiceDialog.setHeaderText("Choisissez une couleur pour le nouvel intercalaire :");
                    choiceDialog.setContentText("Couleur :");

                    Optional<String> colorResult = choiceDialog.showAndWait();
                    if (colorResult.isPresent()) {
                        int tabColorID = NotebookColor.getColorIDByName(colorResult.get());

                        if (tabColorID >= 0) {
                            Tab newtab = binder.createTab(tabName, tabColorID);

                            if (newtab != null) {
                                CustomAlert.create(Alert.AlertType.INFORMATION,"Information", null, "Intercalaire bien créée", "show");

                                //  code hexadécimal de la couleur du nouveau classeur
                                String colorHex = NotebookColor.getHexColorByID(tabColorID);
                                Node circle = getColorCircle(colorHex);

                                TreeItem<String> newTabItem = new TreeItem<>(tabName);
                                newTabItem.setGraphic(circle);

                                selectedBinder.getChildren().add(newTabItem);
                            } else {
                                CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Oups ! Une erreur s'est produite lors de la création de l'intercalaire", "show");
                            }


                        } else {
                            CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "La couleur sélectionnée est invalide.", "show");
                        }
                    }
                }
            } else {
                CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Veuillez sélectionner un classeur pour ajouter un intercalaire.", "show");
            }
        } else {
            CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Aucun classeur sélectionné.", "show");
        }
    }


    @FXML
    private void editTabMenu() {
        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {

            if (!selectedItem.getParent().equals(binderTree.getRoot())) {
                String selectedTabName = selectedItem.getValue();
                Tab selectedTab = notebook.getTabByName(selectedTabName);

                if (selectedTab != null) {

                    TextInputDialog nameDialog = new TextInputDialog(selectedTab.getTabName());
                    nameDialog.setTitle("Modification de l'intercalaire");
                    nameDialog.setHeaderText("Modifiez le nom de l'intercalaire :");
                    nameDialog.setContentText("Nom :");

                    Optional<String> nameResult = nameDialog.showAndWait();
                    nameResult.ifPresent(newName -> {
                        selectedTab.editName(newName);
                        selectedItem.setValue(newName);
                    });


                    List<String> colorNames = NotebookColor.getAllColorNames();
                    ChoiceDialog<String> colorDialog = new ChoiceDialog<>(NotebookColor.getColorNameByID(selectedTab.getTabColorID()), colorNames);
                    colorDialog.setTitle("Modification de la couleur de l'intercalaire");
                    colorDialog.setHeaderText("Choisissez une nouvelle couleur pour l'intercalaire :");
                    colorDialog.setContentText("Couleur :");

                    Optional<String> colorResult = colorDialog.showAndWait();
                    colorResult.ifPresent(newColorName -> {
                        int newColorID = NotebookColor.getColorIDByName(newColorName);
                        if (newColorID >= 0) {
                            selectedTab.editColor(newColorID);

                            String colorHex = NotebookColor.getHexColorByID(newColorID);
                            Node circle = getColorCircle(colorHex);
                            selectedItem.setGraphic(circle);
                        }
                    });
                } else {
                    CustomAlert.create(Alert.AlertType.WARNING,"Information", null, "Intercalaire non trouvé.", "showAndWait");
                }
            }
        } else {
            CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Veuillez sélectionner un intercalaire à éditer.", "showAndWait");
        }
    }


    @FXML
    private void deleteTabMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null && !selectedItem.getParent().equals(binderTree.getRoot())) {

            String selectedTabName = selectedItem.getValue();
            Tab selectedTab = notebook.getTabByName(selectedTabName);

            if ((selectedTab != null)) {
                Alert alert = CustomAlert.create(Alert.AlertType.CONFIRMATION, "Confirmation de suppression", "Supprimer l'intercalaire " + selectedTabName + " ?", "Aucun élément sélectionné.", null);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Binder binder = notebook.getBinderByName(selectedItem.getParent().getValue());
                    if (binder != null) {
                        int TabID = selectedTab.getTabID();
                        int deleteResult = binder.deleteTab(TabID);
                        if (deleteResult > 0) {
                            selectedItem.getParent().getChildren().remove(selectedItem);
                        } else {
                            CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Erreur lors de la suppression de l'intercalaire.", "showAndWait");
                        }
                    }
                } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                    CustomAlert.create(Alert.AlertType.INFORMATION,"Information", null, "Vous avez annulé la suppressionde l'intercalaire.", "showAndWait");
                }

            } else {
                CustomAlert.create(Alert.AlertType.INFORMATION, "Erreur", null, "Veuillez sélectionner un intercalaire à supprimer.", "showAndWait");
            }
        }
    }



    @FXML
    private void addNoteMenu() {

        System.out.println("Selected item: " + binderTree.getSelectionModel().getSelectedItem());

        TreeItem<String> selectedNameTab = binderTree.getSelectionModel().getSelectedItem();

        if (selectedNameTab != null && selectedNameTab.getParent() != null && selectedNameTab.getParent().getParent() == binderTree.getRoot()) {

            Tab selectedTab = notebook.getTabByName(selectedNameTab.getValue());

            if (selectedTab != null) {
                TextInputDialog dialog = new TextInputDialog("Nom de la Note");
                dialog.setTitle("Création d'une Nouvelle Note");
                dialog.setHeaderText("Entrez le nom de la nouvelle note :");
                dialog.setContentText("Nom :");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {

                    Note newNote = selectedTab.createNote(name);

                    if (newNote != null) {
                        CustomAlert.create(Alert.AlertType.INFORMATION, "Information", null, "Note bien créé", "show");

                        // Mettez à jour l'interface utilisateur
                        TreeItem<String> newNoteItem = new TreeItem<>(name);
                        selectedNameTab.getChildren().add(newNoteItem);

                        NoteArea.setContentInNoteArea(newNote);
                        noteArea.setHtmlText(newNote.getNoteContent());
                    } else {
                        CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Oups ! Une erreur s'est produite lors de la création de la note", "show");
                    }
                });
            } else {
                CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Intercalaire non trouvé.", "show");
            }
        } else {
            CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Veuillez sélectionner un intercalaire pour ajouter une note.", "show");
        }


    }

    @FXML
    private void editNoteMenu(ActionEvent event) {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {

            Tab selectedTab = notebook.getTabByName(selectedItem.getParent().getValue());

            if (selectedTab != null) {

                Note selectedNote = selectedTab.getNotes().stream()
                        .filter(note -> note.getNoteName().equals(selectedItem.getValue()))
                        .findFirst().orElse(null);

                if (selectedNote != null) {

                    TextInputDialog dialog = new TextInputDialog(selectedNote.getNoteName());
                    dialog.setTitle("Modification du titre de la note");
                    dialog.setHeaderText("Modifiez le titre de la note :");
                    dialog.setContentText("Nom :");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newName -> {

                        int updateResult = selectedNote.editName(newName);
                        if (updateResult > 0) {
                            selectedItem.setValue(newName);
                            NoteArea.noteTitle.setText(newName);

                            CustomAlert.create(Alert.AlertType.INFORMATION,"Information", null, "Note bien modifiée", "show");
                        } else {
                            CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Oups ! Une erreur s'est produite lors de la modification de la note", "show");
                        }

                    });
                } else {
                    CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Note non trouvé", "show");
                }
            } else {
                CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Intercalaire non trouvé.", "show");
            }
        } else {
            CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Veuillez sélectionner une note à éditer.", "show");
        }
    }


    
    @FXML
    private void deleteNoteMenu() {
        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null && selectedItem.getParent().getParent() != null) {

            Tab selectedTab = notebook.getTabByName(selectedItem.getParent().getValue());

            if (selectedTab != null) {
                Note selectedNote = selectedTab.getNotes().stream()
                        .filter(note -> note.getNoteName().equals(selectedItem.getValue()))
                        .findFirst().orElse(null);

                if (selectedNote != null) {

                    Alert confirmAlert = CustomAlert.create(Alert.AlertType.CONFIRMATION, "Confirmation de suppression", "Supprimer la note " + selectedNote.getNoteName() + " ?", "Êtes-vous sûr ? Cette action est irréversible.", null);

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        int noteID = selectedNote.getNoteID();
                        int deleteResult = selectedTab.deleteNote(noteID);

                        if (deleteResult > 0) {
                            selectedItem.getParent().getChildren().remove(selectedItem);

                            noteSelectedPane.setVisible(false);
                            waitingNoteSelectedPane.setVisible(true);
                            NoteArea.btnChooseLabel.setDisable(true);

                            CustomAlert.create(Alert.AlertType.INFORMATION, "Information", null, "Note bien supprimé.", "show");

                        } else {
                            CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Erreur lors de la suppression de la note.", "show");
                        }
                    } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                        CustomAlert.create(Alert.AlertType.INFORMATION, "Information", null, "Vous avez annulé la suppression de la note.", "show");
                    }
                } else {
                    CustomAlert.create(Alert.AlertType.WARNING,"Erreur", null, "Veuillez sélectionner une note à supprimer.", "show");
                }
            } else {
                CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Veuillez sélectionner une note à supprimer.", "show");
            }
        }
    }

    private Node getColorCircle(String colorHex) {
        return new Circle(5, Color.web(colorHex));
    }

}