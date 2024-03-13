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
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * La classe MainWindow étend la classe Application de JavaFX.
 * Elle représente la fenêtre principale de l'application.
 */
public class MainWindow extends Application {


    /**
     * La racine de la scène JavaFX
     */
    private Parent root;


    /**
     * Le label affichant le nom de l'utilisateur
     */
    @FXML
    Label userNameLabel;


    /**
     * Le label affichant le titre de la note
     */
    @FXML
    Label noteTitle;


    /**
     * Le label affichant les labels de la note
     */
    @FXML
    Label noteLabels;


    /**
     * Le panneau affichant la note sélectionnée
     */
    @FXML
    private Pane noteSelectedPane;


    /**
     * Le panneau affichant l'attente de la sélection d'une note
     */
    @FXML
    private Pane waitingNoteSelectedPane;


    /**
     * Les boutons pour se déconnecter et sauvegarder
     */
    @FXML
    Button btnLogOut, btnSave;


    /**
     * Les boutons pour créer, éditer et supprimer un label
     */
    Button btnCreateLabel, btnEditLabel, btnDeleteLabel;


    /**
     * Le champ de texte pour le nom
     */
    TextField nameField;


    /**
     * La boîte de sélection pour les catégories
     */
    ComboBox<String> categoryBox;


    /**
     * Le bouton de menu pour filtrer les labels
     */
    @FXML
    MenuButton btnFilterLabel;


    /**
     * Le bouton de menu pour choisir un label
     */
    @FXML
    MenuButton btnChooseLabel;


    /**
     * L'éditeur HTML pour la note
     */
    @FXML
    HTMLEditor noteArea;


    /**
     * L'arbre de visualisation pour le classeur
     */
    @FXML
    private TreeView<String> binderTree;


    /**
     * Le bouton de menu pour créer une action
     */
    @FXML
    MenuButton btnCreateAction;


    /**
     * L'utilisateur actuellement connecté
     */
    private static User user;


    /**
     * Le classeur actuellement ouvert
     */
    Notebook notebook;


    /**
     * Cette méthode est appelée lors du lancement de l'application.
     *
     * @param stage Le stage principal de l'application.
     * @throws Exception Si une exception est levée lors de l'exécution.
     */
    @Override
    public void start(Stage stage) throws Exception {

    }


    /**
     * Cette méthode est appelée pour initialiser l'interface utilisateur après le chargement de la fenêtre principale.
     * Elle définit les contraintes de taille de la fenêtre, initialise le carnet de notes et la zone de notes,
     * et génère la vue en arbre pour le carnet de notes.
     */
    @FXML
    public void initialize() {
        // Définit les contraintes de taille de la fenêtre
        Platform.runLater(() -> {
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setResizable(true);
            stage.setMinWidth(850.0);
            stage.setMinHeight(500.0);
        });

        // Définit le texte du label userNameLabel pour saluer l'utilisateur
        userNameLabel.setText(user.getUserName());

        // Initialise le carnet de notes avec l'utilisateur actuel
        notebook = new Notebook(user);

        // Initialise la zone de notes vide dans l'éditeur HTML de l'interface utilisateur
        NoteArea.setNoteArea(noteArea, noteSelectedPane, waitingNoteSelectedPane, noteTitle, noteLabels, btnChooseLabel);

        // Définit le contenu du menu déroulant de filtrage des notes
        setLabelFilterDropdownContent(btnFilterLabel, this::setLabelFilter, false);

        // Définit le contenu du menu déroulant de sélection des labels de notes
        setLabelFilterDropdownContent(btnChooseLabel, this::setNoteLabel, false);

        // Génère la vue en arbre pour le carnet de notes
        generateTreeView();
    }


    /**
     * Cette méthode est appelée lors de la déconnexion de l'utilisateur.
     * Elle affiche une alerte de confirmation pour demander à l'utilisateur s'il souhaite enregistrer avant de se déconnecter.
     * Si l'utilisateur confirme, la méthode ferme la fenêtre actuelle et charge la fenêtre de connexion.
     *
     * @param event L'événement qui a déclenché cette méthode (clic sur le bouton de déconnexion).
     * @throws IOException Si une erreur d'E/S se produit lors du chargement de la fenêtre de connexion.
     */
    public void logOut(ActionEvent event) throws IOException {

        // Création d'une alerte de confirmation pour la déconnexion
        Alert alert = CustomAlert.create(
                Alert.AlertType.CONFIRMATION,
                "Déconnexion",
                "Déconnexion utilisateur",
                "Voulez-vous vous déconnecter ?",
                null
        );

        // Affichage de l'alerte et attente de la réponse de l'utilisateur
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // Fermeture de la fenêtre actuelle
                Stage currentStage = (Stage) userNameLabel.getScene().getWindow();
                currentStage.close();
                // Si l'utilisateur a confirmé, chargement de la fenêtre de connexion
                root = FXMLLoader.load(getClass().getResource("loginWindow.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setMinWidth(284);
                stage.setMinHeight(476);
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /**
     * Cette méthode est appelée lors de l'enregistrement d'une note.
     * Elle récupère le contenu de la note dans l'éditeur HTML, met à jour le contenu de la note et affiche une alerte de confirmation.
     *
     * @param e L'événement qui a déclenché cette méthode (clic sur le bouton d'enregistrement).
     */
    public void saveNote(ActionEvent e) {
        // Récupération du contenu de la note dans l'éditeur HTML
        String content = noteArea.getHtmlText();

        // Mise à jour du contenu de la note
        NoteArea.note.editContent(content);

        // Affichage d'une alerte de confirmation
        CustomAlert.create(
                Alert.AlertType.INFORMATION,
                "Confirmation",
                null,
                "Note bien sauvegardée",
                "show"
        );
    }


    /**
     * Cette méthode est appelée lors de l'application d'un filtre de label sur les notes.
     * Si le label sélectionné est "Tous", elle réinitialise le contenu du carnet de notes pour afficher toutes les notes.
     * Sinon, elle filtre les notes par le nom du label et met à jour l'affichage de l'arbre de visualisation.
     *
     * @param e L'événement qui a déclenché cette méthode (sélection d'un label dans le menu déroulant).
     */
    public void setLabelFilter(ActionEvent e) {
        // Vérifie si la source de l'événement est un MenuItem
        if (e.getSource() instanceof MenuItem selectedLabel) {
            // Récupère le nom du label
            String labelName = selectedLabel.getText();
            System.out.println(labelName);

            // Si le nom du label est "Tous", réinitialise le contenu du carnet de notes pour afficher toutes les notes
            if (labelName.equals("Tous")) {
                // Réinitialise le style de tous les MenuItems
                btnFilterLabel.setText("Tous");

                notebook.setNotebookContent();
                generateTreeView();
            } else {
                // Filtre les notes par le nom du label et met à jour l'affichage de l'arbre de visualisation
                notebook.setNotebookContent(labelName);

                // Change le texte du label sélectionné pour l'entourer de crochets
                btnFilterLabel.setText(labelName);

                // Génère l'arbre de visualisation pour le carnet de notes
                generateTreeView();

                // Déplie tous les classeurs et les onglets dans l'arbre de visualisation
                for (TreeItem<String> binder : binderTree.getRoot().getChildren()) {
                    binder.setExpanded(true);
                    for (TreeItem<String> tab : binder.getChildren()) {
                        tab.setExpanded(true);
                    }
                }
            }
        }
    }


    /**
     * Cette méthode est utilisée pour mettre à jour la zone de notes.
     * Elle réinitialise le contenu du carnet de notes, génère la vue en arbre pour le carnet de notes,
     * récupère la nouvelle note par son ID, met à jour le contenu de la zone de notes avec la nouvelle note,
     * et met à jour le texte des labels de la note.
     *
     * @param note La note à mettre à jour.
     */
    private void updateNoteArea(Note note) {
        // Réinitialise le contenu du carnet de notes
        notebook.setNotebookContent();

        // Génère la vue en arbre pour le carnet de notes
        generateTreeView();

        // Récupère l'ID de la note
        int noteID = note.getNoteID();

        // Récupère la nouvelle note par son ID
        Note newNote = notebook.getNoteByID(noteID);

        // Met à jour le contenu de la zone de notes avec la nouvelle note
        NoteArea.setContentInNoteArea(newNote);

        // Met à jour le texte des labels de la note
        NoteArea.setLabelsText();
    }


    /**
     * Cette méthode est utilisée pour définir le label d'une note.
     * Elle récupère la note actuelle de NoteArea et vérifie si la source de l'événement est un MenuItem.
     * Si le label sélectionné est déjà dans la note, elle le supprime de la note, sinon elle l'ajoute à la note.
     * Après l'ajout ou la suppression du label, elle met à jour la zone de notes.
     *
     * @param e L'événement qui a déclenché cette méthode (sélection d'un label dans le menu déroulant).
     */
    public void setNoteLabel(ActionEvent e) {

        // Récupère l'objet Note actuel de NoteArea
        Note note = NoteArea.getNote();

        // Vérifie si la source de l'événement est un MenuItem
        if (e.getSource() instanceof MenuItem selectedLabel && note != null) {
            // Récupère le nom du label
            String labelName = selectedLabel.getText();
            System.out.println("Selected label" + labelName);

            System.out.println("Listing labels in note : " + note.getNoteName());
            // Si le label sélectionné est déjà dans la note, le supprime de la note, sinon l'ajoute à la note
            ArrayList<NoteLabel> labels = note.getLabels();
            for (NoteLabel label : labels) {
                System.out.println("Label : " + label.getLabelName());
                if (label.getLabelName().equals(labelName)) {
                    System.out.println("Label already in note. Removing it.");
                    int removeLabelResult = note.detachLabelFromNote(labelName);
                    if (removeLabelResult > 0) {
                        // Supprime le signe de coche avant le label correspondant dans le menu déroulant
                        selectedLabel.setGraphic(null);
                    }

                    updateNoteArea(note);
                    return;
                }
            }

            // Ajoute le label sélectionné à la note
            System.out.println("Adding label " + labelName + " to note " + note.getNoteName());
            int addLabelResult = note.attachLabelToNote(labelName);
            if (addLabelResult > 0) {
                System.out.println("Label added to note.");
                // Ajoute un signe de coche avant le label correspondant dans le menu déroulant
                selectedLabel.setGraphic(new Label("✔"));

                updateNoteArea(note);
            }
        }

    }


    /**
     * Cette méthode est utilisée pour créer une boîte de dialogue pour gérer les labels.
     * Elle crée une boîte de dialogue, un GridPane pour organiser les éléments, des labels et des champs de texte pour le nom du label,
     * une boîte de sélection pour les labels existants, et des boutons pour créer, modifier et supprimer des labels.
     * Elle récupère tous les labels de la base de données et les ajoute à la boîte de sélection.
     * Elle définit également les actions à effectuer lorsque l'utilisateur sélectionne un label, clique sur les boutons ou ferme la boîte de dialogue.
     */
    public void labelManagementDialog() {

        // Création d'une boîte de dialogue
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Étiquettes");
        dialog.setHeaderText("Gestion des étiquettes");

        // Création d'un GridPane pour organiser les éléments de la boîte de dialogue
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Création des labels et des champs de texte pour le nom du label
        Label nameLabel = new Label("Intitulé de l'étiquette :");
        nameField = new TextField();
        nameField.setPrefWidth(400);

        // Création d'une boîte de disposition horizontale pour contenir les boutons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10); // Définit l'espacement entre les boutons

        // Création des boutons pour créer, modifier et supprimer des labels
        btnCreateLabel = new Button("Ajouter");
        btnEditLabel = new Button("Modifier");
        btnDeleteLabel = new Button("Supprimer");

        // Ajout des boutons à la boîte de disposition horizontale
        buttonBox.getChildren().addAll(btnCreateLabel, btnEditLabel, btnDeleteLabel);

        // Désactivation des boutons initialement
        btnDeleteLabel.setDisable(true);
        btnEditLabel.setDisable(true);

        // Création d'un label et d'une boîte de sélection pour les labels existants
        Label categoryLabel = new Label("Liste des étiquettes :");
        categoryBox = new ComboBox<>();

        // Récupération de tous les labels de la base de données et ajout à la boîte de sélection
        Map<Integer, String> labels = LabelManager.getAllLabels();
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {
            categoryBox.getItems().add(entry.getValue());
        }

        // Définition de l'action à effectuer lorsque l'utilisateur sélectionne un label
        categoryBox.setOnAction(event -> {
            String selectedValue = categoryBox.getValue();
            nameField.setText(selectedValue);

            btnEditLabel.setDisable(false);
            btnDeleteLabel.setDisable(false);
        });

        categoryBox.setPrefWidth(400);

        // Ajout de la boîte de disposition horizontale à la grille
        grid.add(buttonBox, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(categoryLabel, 0, 2);
        grid.add(categoryBox, 1, 2);

        // Ajout de la grille à la boîte de dialogue
        dialog.getDialogPane().setContent(grid);

        // Création d'un bouton pour fermer la boîte de dialogue
        ButtonType closeButtonType = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Ajout du bouton à la boîte de dialogue
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);

        // Définition des actions à effectuer lorsque l'utilisateur clique sur les boutons
        btnCreateLabel.setOnAction(e -> actionsLabel(nameField, "create", null));
        btnEditLabel.setOnAction(e -> actionsLabel(nameField, "edit", categoryBox.getValue()));
        btnDeleteLabel.setOnAction(e -> actionsLabel(nameField, "delete", null));

        // Affichage de la boîte de dialogue et attente de la fermeture par l'utilisateur
        dialog.showAndWait();
    }


    /**
     * Cette méthode est utilisée pour effectuer des actions sur les labels.
     * Elle crée une alerte, récupère le texte du champ de texte du label, et effectue l'action spécifiée (créer, modifier, supprimer).
     * Si l'action est réussie, elle met à jour le contenu de la boîte de sélection des labels, le contenu du carnet de notes, et la zone de notes.
     * Si l'action échoue, elle affiche une alerte d'erreur.
     *
     * @param nameField     Le champ de texte contenant le nom du label.
     * @param action        L'action à effectuer ("create", "edit", "delete").
     * @param selectedLabel Le label sélectionné dans la boîte de sélection (seulement pour l'action "edit").
     */
    public void actionsLabel(TextField nameField, String action, String selectedLabel) {

        // Création d'une alerte
        Alert alert = CustomAlert.create(
                Alert.AlertType.INFORMATION,
                "Information",
                null,
                null,
                null
        );

        // Récupération du texte du champ de texte du label
        String labelField = nameField.getText();
        String headerText = null;
        String contentText = null;

        // Vérification du format du label
        if (FormatChecker.checkLabelFormat(labelField)) {
            int result;

            // Effectue l'action spécifiée
            if (action.equals("create") && !categoryBox.getItems().contains(labelField))
                result = LabelManager.createLabel(labelField);
            else if (action.equals("edit") && !categoryBox.getItems().contains(labelField))
                result = LabelManager.updateLabel(selectedLabel, labelField);
            else if (action.equals("delete"))
                result = LabelManager.deleteLabel(labelField);
            else
                result = -1;

            // Si l'action est réussie, met à jour le contenu de la boîte de sélection des labels, le contenu du carnet de notes, et la zone de notes
            if (result > 0) {
                switch (action) {
                    case "create" -> {
                        // Ajoute le nouveau label à la boîte de sélection
                        categoryBox.getItems().add(labelField);
                        headerText = "Ajout effectué";
                        contentText = "L'étiquette " + labelField + " a bien été ajoutée.";
                    }
                    case "edit" -> {
                        // Met à jour le texte dans la boîte de sélection avec le nouveau nom du label
                        categoryBox.getItems().set(categoryBox.getItems().indexOf(selectedLabel), labelField);
                        headerText = "Mise à jour effectuée";
                        contentText = "L'étiquette " + labelField + " a bien été modifiée.";
                    }
                    case "delete" -> {
                        // Supprime le label sélectionné de la boîte de sélection
                        categoryBox.getItems().remove(selectedLabel);
                        headerText = "Suppression effectuée";
                        contentText = "L'étiquette " + labelField + " a bien été supprimée.";
                    }
                }

                // Définit le texte de l'en-tête et du contenu de l'alerte
                CustomAlert.setHeader(headerText);
                CustomAlert.setContent(contentText);

                setLabelFilterDropdownContent(btnFilterLabel, this::setLabelFilter, true);
                setLabelFilterDropdownContent(btnChooseLabel, this::setNoteLabel, false);
                notebook.setNotebookContent();

                // Obtient l'ID de la note dans NoteArea
                Note currentNote = NoteArea.getNote();

                if (currentNote != null) {
                    int noteID = currentNote.getNoteID();
                    // Obtient la note du carnet de notes, définit le contenu dans la zone de notes, et met à jour le texte des labels
                    Note note = notebook.getNoteByID(noteID);
                    NoteArea.setContentInNoteArea(note);
                    NoteArea.setLabelsText();
                }

                // Si l'action échoue, affiche une alerte d'erreur
            } else if (result == -1) {
                CustomAlert.setHeader("Erreur");
                CustomAlert.setContent("L'étiquette " + labelField + " existe déjà dans la liste");
                nameField.clear();
            } else {
                CustomAlert.setHeader("Erreur");
                CustomAlert.setContent("L'étiquette " + labelField + " n'a pas pu être ajoutée.");
            }
            nameField.clear();

        } else {
            System.out.println("Erreur");
            CustomAlert.setHeader("Format incorrect");
            CustomAlert.setContent("La longeur du nom de l'étiquette doit être comprise entre 2 et 16 caractères.");
        }

        // Affiche l'alerte
        alert.show();
    }


    /**
     * Cette méthode est utilisée pour définir l'utilisateur actuel.
     * Elle définit l'utilisateur actuel et affiche les détails de l'utilisateur dans la console.
     *
     * @param pUser L'utilisateur à définir comme utilisateur actuel.
     */
    public static void setUser(User pUser) {
        System.out.println("initUser");
        user = pUser;
        System.out.println(user.toString());
    }


    /**
     * Cette méthode est utilisée pour définir le contenu du menu déroulant de filtrage des labels.
     * Elle met à jour les labels si nécessaire, récupère tous les labels de la base de données,
     * efface les éléments du menu déroulant, ajoute un nouvel élément de menu pour tous les labels,
     * et crée un nouvel élément de menu pour chaque label récupéré.
     *
     * @param menuButton   Le bouton de menu à mettre à jour.
     * @param callback     L'action à effectuer lorsque l'utilisateur sélectionne un label dans le menu déroulant.
     * @param updateLabels Indique si les labels doivent être mis à jour.
     */
    public void setLabelFilterDropdownContent(MenuButton menuButton, EventHandler<ActionEvent> callback, boolean updateLabels) {

        // Met à jour les labels si nécessaire
        if (updateLabels) {
            LabelManager.updateLabels();
        }

        // Récupère tous les labels de la base de données
        Map<Integer, String> labels = LabelManager.getAllLabels();

        // Efface les éléments du menu déroulant
        menuButton.getItems().clear();

        // Ajoute un nouvel élément de menu pour tous les labels
        MenuItem allLabels = new MenuItem("Tous");
        allLabels.setOnAction(callback);
        menuButton.getItems().add(allLabels);

        // Parcourt les labels récupérés
        for (Map.Entry<Integer, String> entry : labels.entrySet()) {

            // Crée un nouvel élément de menu pour chaque label
            MenuItem label = new MenuItem(entry.getValue());

            // Définit l'action de l'élément de menu pour déclencher le callback
            label.setOnAction(callback);

            // Ajoute l'élément de menu au menu déroulant
            menuButton.getItems().add(label);
        }
    }


    /**
     * Cette méthode est utilisée pour générer la vue en arbre du carnet de notes.
     * Elle crée une nouvelle instance de NotebookTreeView avec le carnet de notes et l'arbre de visualisation actuels,
     * puis elle appelle la méthode createTreeView pour générer la vue en arbre.
     */
    public void generateTreeView() {
        NotebookTreeView notebookTreeView = new NotebookTreeView(binderTree, notebook);
        notebookTreeView.createTreeView();
    }


    /**
     * Cette méthode est utilisée pour ajouter un nouveau classeur.
     * Elle crée une boîte de dialogue pour entrer le nom du nouveau classeur et une autre pour choisir sa couleur.
     * Si l'utilisateur confirme, elle crée un nouveau classeur avec le nom et la couleur spécifiés,
     * crée un nouvel élément d'arbre pour le classeur avec un cercle de la couleur du classeur,
     * et ajoute l'élément d'arbre à la racine de l'arbre de visualisation.
     * Si la création du classeur échoue, elle affiche une alerte d'erreur.
     */
    @FXML
    private void addBinderMenu() {

        TextInputDialog dialog = new TextInputDialog("Nouveau classeur");
        dialog.setTitle("Création d'un nouveau Classeur");
        dialog.setHeaderText("Création d'un nouveau Classeur");
        dialog.setContentText("Nom :");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {

            String binderName = result.get();

            List<String> colorNames = NotebookColor.getAllColorNames();
            ChoiceDialog<String> dialogColor = new ChoiceDialog<>(colorNames.getFirst(), colorNames);
            dialogColor.setTitle("Choix de la couleur");
            dialogColor.setHeaderText("Choix de la couleur");
            dialogColor.setContentText("Couleur :");

            Optional<String> colorResult = dialogColor.showAndWait();
            if (colorResult.isPresent()) {
                int binderColorId = NotebookColor.getColorIDByName(colorResult.get());

                Binder newBinder = notebook.createBinder(binderName, binderColorId);

                if (newBinder != null) {
                    CustomAlert.create(Alert.AlertType.INFORMATION, "Information", null, "Classeur " + binderName + " créé", "show");

                    //  code hexadécimal de la couleur du nouveau classeur
                    String colorHex = NotebookColor.getHexColorByID(binderColorId);
                    Node circle = NotebookColor.getColorCircle(colorHex);

                    TreeItem<String> newBinderItem = new TreeItem<>(binderName);
                    newBinderItem.setGraphic(circle);

                    // Ajout du nouveau classeur à la racine de l'arbre
                    binderTree.getRoot().getChildren().add(newBinderItem);
                } else {
                    CustomAlert.create(Alert.AlertType.WARNING, "Erreur", null, "Une erreur s'est produite lors de la création du classeur", "show");
                }
            }
        }
    }


    /**
     * Cette méthode est utilisée pour modifier un classeur existant.
     * Elle récupère le classeur sélectionné dans l'arbre de visualisation, puis affiche une boîte de dialogue pour modifier le nom et la couleur du classeur.
     * Si l'utilisateur confirme, elle met à jour le nom et la couleur du classeur, et met à jour l'élément correspondant dans l'arbre de visualisation.
     * Si le classeur sélectionné n'existe pas, elle affiche une alerte d'erreur.
     * Si aucun classeur n'est sélectionné, elle affiche une alerte demandant à l'utilisateur de sélectionner un classeur à modifier.
     */
    @FXML
    private void editBinderMenu() {
        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() == binderTree.getRoot()) {

            String oldBinderName = selectedItem.getValue();
            Binder selectedBinder = notebook.getBinderByName(oldBinderName);

            if (selectedBinder != null) {

                TextInputDialog nameDialog = new TextInputDialog(selectedBinder.getBinderName());
                nameDialog.setTitle("Modification du nom du Classeur");
                nameDialog.setHeaderText("Modification du nom du Classeur");
                nameDialog.setContentText("Nom :");

                Optional<String> nameResult = nameDialog.showAndWait();
                nameResult.ifPresent(newName -> {
                    selectedBinder.editName(newName);
                    selectedItem.setValue(newName);
                });


                List<String> colorNames = NotebookColor.getAllColorNames();
                String currentColorName = NotebookColor.getColorNameByID(selectedBinder.getBinderColorID());
                ChoiceDialog<String> dialogColor = new ChoiceDialog<>(currentColorName, colorNames);
                dialogColor.setTitle("Choix de la couleur");
                dialogColor.setHeaderText("Choix de la couleur");
                dialogColor.setContentText("Couleur :");

                Optional<String> colorResult = dialogColor.showAndWait();
                colorResult.ifPresent(newColorName -> {
                    int newColorID = NotebookColor.getColorIDByName(newColorName);
                    selectedBinder.editColor(newColorID);

                    String colorHex = NotebookColor.getHexColorByID(newColorID);
                    Node circle = NotebookColor.getColorCircle(colorHex);
                    selectedItem.setGraphic(circle);
                });

                CustomAlert.create(
                        Alert.AlertType.INFORMATION,
                        "Erreur",
                        null,
                        "Nom du classeur modifié",
                        "showAndWait")
                ;

            } else {
                CustomAlert.create(
                        Alert.AlertType.WARNING,
                        "Erreur",
                        null,
                        "Classeur non trouvé",
                        "showAndWait"
                );
            }
        } else {
            CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Erreur",
                    null,
                    "Veuillez sélectionner un classeur à modifier.",
                    "showAndWait"
            );
        }
    }


    /**
     * Cette méthode est utilisée pour supprimer un classeur existant.
     * Elle récupère le classeur sélectionné dans l'arbre de visualisation.
     * Si le classeur sélectionné existe et est à la racine de l'arbre, elle affiche une alerte de confirmation pour demander à l'utilisateur s'il souhaite supprimer le classeur.
     * Si l'utilisateur confirme, elle supprime le classeur, et supprime l'élément correspondant dans l'arbre de visualisation.
     * Si la suppression du classeur échoue, elle affiche une alerte d'erreur.
     * Si aucun classeur n'est sélectionné ou si le classeur sélectionné n'est pas à la racine de l'arbre, elle affiche une alerte demandant à l'utilisateur de sélectionner un classeur à supprimer.
     */
    @FXML
    private void deleteBinderMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() == binderTree.getRoot()) {
            String binderName = selectedItem.getValue();
            Binder selectedBinder = notebook.getBinderByName(binderName);

            Alert alert = CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Confirmation de suppression",
                    null, "Supprimer le classeur \" + binderName + \" ?",
                    "Êtes-vous sûr ? Cette action est irréversible"
            );

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Suppression du classeur
                int binderID = selectedBinder.getBinderID();
                int deleteResult = notebook.deleteBinder(binderID);

                if (deleteResult > 0) {

                    binderTree.getRoot().getChildren().remove(selectedItem);
                } else {
                    CustomAlert.create(
                            Alert.AlertType.WARNING,
                            "Erreur",
                            null,
                            "Erreur lors de la suppression du classeur",
                            "showAndWait"
                    );
                }
            }
        } else {
            CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Erreur",
                    null,
                    "Veuillez sélectionner un classeur à supprimer",
                    "showAndWait"
            );
        }
    }


    /**
     * Cette méthode est utilisée pour ajouter un nouvel onglet à un classeur existant.
     * Elle récupère le classeur sélectionné dans l'arbre de visualisation.
     * Si le classeur sélectionné existe, elle affiche une boîte de dialogue pour entrer le nom du nouvel onglet et une autre pour choisir sa couleur.
     * Si l'utilisateur confirme, elle crée un nouvel onglet avec le nom et la couleur spécifiés,
     * crée un nouvel élément d'arbre pour l'onglet avec un cercle de la couleur de l'onglet,
     * et ajoute l'élément d'arbre au classeur sélectionné dans l'arbre de visualisation.
     * Si la création de l'onglet échoue, elle affiche une alerte d'erreur.
     * Si aucun classeur n'est sélectionné, elle affiche une alerte demandant à l'utilisateur de sélectionner un classeur pour ajouter un onglet.
     */
    @FXML
    private void addTabMenu() {
        System.out.println("Selected item: " + binderTree.getSelectionModel().getSelectedItem());

        TreeItem<String> selectedBinder = binderTree.getSelectionModel().getSelectedItem();

        if (selectedBinder != null) {

            Binder binder = notebook.getBinderByName(selectedBinder.getValue());

            if (binder != null) {

                TextInputDialog dialog = new TextInputDialog("Nouvel Intercalaire");
                dialog.setTitle("Création d'un nouvel Intercalaire");
                dialog.setHeaderText("Création d'un nouvel Intercalaire");
                dialog.setContentText("Nom :");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String tabName = result.get();

                    List<String> colorNames = NotebookColor.getAllColorNames();
                    ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(colorNames.getFirst(), colorNames);
                    choiceDialog.setTitle("Choix de la couleur");
                    choiceDialog.setHeaderText("Choix de la couleur");
                    choiceDialog.setContentText("Couleur :");

                    Optional<String> colorResult = choiceDialog.showAndWait();
                    if (colorResult.isPresent()) {
                        int tabColorID = NotebookColor.getColorIDByName(colorResult.get());

                        if (tabColorID >= 0) {
                            Tab newtab = binder.createTab(tabName, tabColorID);

                            if (newtab != null) {
                                CustomAlert.create(
                                        Alert.AlertType.INFORMATION,
                                        "Information",
                                        null,
                                        "Intercalaire bien créé",
                                        "show");

                                //  code hexadécimal de la couleur du nouveau classeur
                                String colorHex = NotebookColor.getHexColorByID(tabColorID);
                                Node circle = NotebookColor.getColorCircle(colorHex);

                                TreeItem<String> newTabItem = new TreeItem<>(tabName);
                                newTabItem.setGraphic(circle);

                                selectedBinder.getChildren().add(newTabItem);
                            } else {
                                CustomAlert.create(
                                        Alert.AlertType.WARNING,
                                        "Erreur",
                                        null,
                                        "Une erreur s'est produite lors de la création de l'intercalaire",
                                        "show"
                                );
                            }

                        } else {
                            CustomAlert.create(
                                    Alert.AlertType.WARNING,
                                    "Erreur",
                                    null,
                                    "La couleur sélectionnée est invalide",
                                    "show"
                            );
                        }
                    }
                }
            } else {
                CustomAlert.create(
                        Alert.AlertType.WARNING,
                        "Erreur",
                        null,
                        "Veuillez sélectionner un classeur",
                        "show"
                );
            }
        } else {
            CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Erreur",
                    null,
                    "Aucun classeur sélectionné",
                    "show"
            );
        }
    }


    /**
     * Cette méthode est utilisée pour modifier un onglet existant.
     * Elle récupère l'onglet sélectionné dans l'arbre de visualisation.
     * Si l'onglet sélectionné existe et n'est pas à la racine de l'arbre, elle affiche une boîte de dialogue pour modifier le nom et la couleur de l'onglet.
     * Si l'utilisateur confirme, elle met à jour le nom et la couleur de l'onglet, et met à jour l'élément correspondant dans l'arbre de visualisation.
     * Si l'onglet sélectionné n'existe pas, elle affiche une alerte d'erreur.
     * Si aucun onglet n'est sélectionné ou si l'onglet sélectionné est à la racine de l'arbre, elle affiche une alerte demandant à l'utilisateur de sélectionner un onglet à modifier.
     */
    @FXML
    private void editTabMenu() {
        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null) {

            if (!selectedItem.getParent().equals(binderTree.getRoot())) {
                String selectedTabName = selectedItem.getValue();
                Tab selectedTab = notebook.getTabByName(selectedTabName);

                if (selectedTab != null) {

                    TextInputDialog nameDialog = new TextInputDialog(selectedTab.getTabName());
                    nameDialog.setTitle("Modification du nom de l'Intercalaire");
                    nameDialog.setHeaderText("Modification du nom de l'Intercalaire");
                    nameDialog.setContentText("Nom :");

                    Optional<String> nameResult = nameDialog.showAndWait();
                    nameResult.ifPresent(newName -> {
                        selectedTab.editName(newName);
                        selectedItem.setValue(newName);
                    });


                    List<String> colorNames = NotebookColor.getAllColorNames();
                    ChoiceDialog<String> colorDialog = new ChoiceDialog<>(NotebookColor.getColorNameByID(selectedTab.getTabColorID()), colorNames);
                    colorDialog.setTitle("Choix de la couleur");
                    colorDialog.setHeaderText("Choix de la couleur");
                    colorDialog.setContentText("Couleur :");

                    Optional<String> colorResult = colorDialog.showAndWait();
                    colorResult.ifPresent(newColorName -> {
                        int newColorID = NotebookColor.getColorIDByName(newColorName);
                        if (newColorID >= 0) {
                            selectedTab.editColor(newColorID);

                            String colorHex = NotebookColor.getHexColorByID(newColorID);
                            Node circle = NotebookColor.getColorCircle(colorHex);
                            selectedItem.setGraphic(circle);
                        }
                    });
                } else {
                    CustomAlert.create(
                            Alert.AlertType.WARNING,
                            "Information",
                            null,
                            "Intercalaire non trouvé",
                            "showAndWait"
                    );
                }
            }
        } else {
            CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Erreur",
                    null,
                    "Veuillez sélectionner un intercalaire",
                    "showAndWait"
            );
        }
    }


    /**
     * Cette méthode est utilisée pour supprimer un onglet existant.
     * Elle récupère l'onglet sélectionné dans l'arbre de visualisation.
     * Si l'onglet sélectionné existe et n'est pas à la racine de l'arbre, elle affiche une alerte de confirmation pour demander à l'utilisateur s'il souhaite supprimer l'onglet.
     * Si l'utilisateur confirme, elle supprime l'onglet, et supprime l'élément correspondant dans l'arbre de visualisation.
     * Si la suppression de l'onglet échoue, elle affiche une alerte d'erreur.
     * Si aucun onglet n'est sélectionné ou si l'onglet sélectionné est à la racine de l'arbre, elle affiche une alerte demandant à l'utilisateur de sélectionner un onglet à supprimer.
     */
    @FXML
    private void deleteTabMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null && !selectedItem.getParent().equals(binderTree.getRoot())) {

            String selectedTabName = selectedItem.getValue();
            Tab selectedTab = notebook.getTabByName(selectedTabName);

            if ((selectedTab != null)) {
                Alert alert = CustomAlert.create(
                        Alert.AlertType.CONFIRMATION,
                        "Confirmation de suppression",
                        "Supprimer l'intercalaire " + selectedTabName + " ?",
                        "Êtes-vous sûr ? Cette action est irréversible",
                        null
                );

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Binder binder = notebook.getBinderByName(selectedItem.getParent().getValue());
                    if (binder != null) {
                        int TabID = selectedTab.getTabID();
                        int deleteResult = binder.deleteTab(TabID);
                        if (deleteResult > 0) {
                            selectedItem.getParent().getChildren().remove(selectedItem);
                        } else {
                            CustomAlert.create(
                                    Alert.AlertType.WARNING,
                                    "Erreur",
                                    null,
                                    "Erreur lors de la suppression de l'Intercalaire.",
                                    "showAndWait"
                            );
                        }
                    }
                } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                    CustomAlert.create(
                            Alert.AlertType.INFORMATION,
                            "Information",
                            null,
                            "Vous avez annulé la suppressionde l'Intercalaire.",
                            "showAndWait"
                    );
                }

            } else {
                CustomAlert.create(
                        Alert.AlertType.INFORMATION,
                        "Erreur",
                        null,
                        "Veuillez sélectionner un Intercalaire",
                        "showAndWait"
                );
            }
        }
    }


    /**
     * Cette méthode est utilisée pour ajouter une nouvelle note à un onglet existant.
     * Elle récupère l'onglet sélectionné dans l'arbre de visualisation.
     * Si l'onglet sélectionné existe et n'est pas à la racine de l'arbre, elle affiche une boîte de dialogue pour entrer le nom de la nouvelle note.
     * Si l'utilisateur confirme, elle crée une nouvelle note avec le nom spécifié,
     * crée un nouvel élément d'arbre pour la note,
     * et ajoute l'élément d'arbre à l'onglet sélectionné dans l'arbre de visualisation.
     * Si la création de la note échoue, elle affiche une alerte d'erreur.
     * Si aucun onglet n'est sélectionné ou si l'onglet sélectionné est à la racine de l'arbre, elle affiche une alerte demandant à l'utilisateur de sélectionner un onglet pour ajouter une note.
     */
    @FXML
    private void addNoteMenu() {

        System.out.println("Selected item: " + binderTree.getSelectionModel().getSelectedItem());

        TreeItem<String> selectedNameTab = binderTree.getSelectionModel().getSelectedItem();

        if (selectedNameTab != null && selectedNameTab.getParent() != null && selectedNameTab.getParent().getParent() == binderTree.getRoot()) {

            Tab selectedTab = notebook.getTabByName(selectedNameTab.getValue());

            if (selectedTab != null) {
                TextInputDialog dialog = new TextInputDialog("Nouvelle Note");
                dialog.setTitle("Création d'une Note");
                dialog.setHeaderText("Création d'une Note");
                dialog.setContentText("Nom :");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {

                    Note newNote = selectedTab.createNote(name);

                    if (newNote != null) {
                        CustomAlert.create(
                                Alert.AlertType.INFORMATION,
                                "Information",
                                null,
                                "Note bien créée",
                                "show"
                        );

                        // Mettez à jour l'interface utilisateur
                        TreeItem<String> newNoteItem = new TreeItem<>(name);
                        selectedNameTab.getChildren().add(newNoteItem);

                        NoteArea.setContentInNoteArea(newNote);
                        noteArea.setHtmlText(newNote.getNoteContent());
                    } else {
                        CustomAlert.create(
                                Alert.AlertType.WARNING,
                                "Erreur",
                                null,
                                "Une erreur s'est produite lors de la création de la Note",
                                "show"
                        );
                    }
                });
            } else {
                CustomAlert.create(
                        Alert.AlertType.WARNING,
                        "Erreur",
                        null,
                        "Intercalaire non trouvé",
                        "show"
                );
            }
        } else {
            CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Erreur",
                    null,
                    "Veuillez sélectionner un Intercalaire",
                    "show"
            );
        }

    }


    /**
     * Cette méthode est utilisée pour modifier une note existante.
     * Elle récupère la note sélectionnée dans l'arbre de visualisation.
     * Si la note sélectionnée existe et n'est pas à la racine de l'arbre, elle affiche une boîte de dialogue pour modifier le nom de la note.
     * Si l'utilisateur confirme, elle met à jour le nom de la note, et met à jour l'élément correspondant dans l'arbre de visualisation.
     * Si la note sélectionnée n'existe pas, elle affiche une alerte d'erreur.
     * Si aucune note n'est sélectionnée ou si la note sélectionnée est à la racine de l'arbre, elle affiche une alerte demandant à l'utilisateur de sélectionner une note à modifier.
     *
     * @param event L'événement qui a déclenché cette méthode (clic sur le bouton d'édition de note).
     */
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
                    dialog.setTitle("Modification du titre de la Note");
                    dialog.setHeaderText("Modification du titre de la Note");
                    dialog.setContentText("Nom :");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newName -> {

                        int updateResult = selectedNote.editName(newName);
                        if (updateResult > 0) {
                            selectedItem.setValue(newName);
                            NoteArea.noteTitle.setText(newName);

                            CustomAlert.create(
                                    Alert.AlertType.INFORMATION,
                                    "Information",
                                    null,
                                    "Note bien modifiée",
                                    "show"
                            );
                        } else {
                            CustomAlert.create(
                                    Alert.AlertType.WARNING,
                                    "Erreur",
                                    null,
                                    "Une erreur s'est produite lors de la modification de la Note",
                                    "show"
                            );
                        }

                    });
                } else {
                    CustomAlert.create(
                            Alert.AlertType.WARNING,
                            "Erreur",
                            null,
                            "Note non trouvée",
                            "show"
                    );
                }
            } else {
                CustomAlert.create(
                        Alert.AlertType.WARNING,
                        "Erreur",
                        null,
                        "Intercalaire non trouvé",
                        "show"
                );
            }
        } else {
            CustomAlert.create(
                    Alert.AlertType.WARNING,
                    "Erreur",
                    null,
                    "Veuillez sélectionner une Note",
                    "show"
            );
        }
    }


    /**
     * Cette méthode est utilisée pour supprimer une note existante.
     * Elle récupère la note sélectionnée dans l'arbre de visualisation.
     * Si la note sélectionnée existe et n'est pas à la racine de l'arbre, elle affiche une alerte de confirmation pour demander à l'utilisateur s'il souhaite supprimer la note.
     * Si l'utilisateur confirme, elle supprime la note, et supprime l'élément correspondant dans l'arbre de visualisation.
     * Si la suppression de la note échoue, elle affiche une alerte d'erreur.
     * Si aucune note n'est sélectionnée ou si la note sélectionnée est à la racine de l'arbre, elle affiche une alerte demandant à l'utilisateur de sélectionner une note à supprimer.
     */
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

                    Alert confirmAlert = CustomAlert.create(
                            Alert.AlertType.CONFIRMATION,
                            "Confirmation de suppression",
                            "Supprimer la Note " + selectedNote.getNoteName() + " ?",
                            "Êtes-vous sûr ? Cette action est irréversible.",
                            null
                    );

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        int noteID = selectedNote.getNoteID();
                        int deleteResult = selectedTab.deleteNote(noteID);

                        if (deleteResult > 0) {
                            selectedItem.getParent().getChildren().remove(selectedItem);

                            noteSelectedPane.setVisible(false);
                            waitingNoteSelectedPane.setVisible(true);
                            NoteArea.btnChooseLabel.setDisable(true);

                            CustomAlert.create(
                                    Alert.AlertType.INFORMATION,
                                    "Information",
                                    null,
                                    "Note bien supprimée.",
                                    "show"
                            );

                        } else {
                            CustomAlert.create(
                                    Alert.AlertType.WARNING,
                                    "Erreur",
                                    null,
                                    "Erreur lors de la suppression de la Note",
                                    "show"
                            );
                        }
                    } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                        CustomAlert.create(
                                Alert.AlertType.INFORMATION,
                                "Information",
                                null,
                                "Vous avez annulé la suppression de la Note.",
                                "show"
                        );
                    }
                } else {
                    CustomAlert.create(
                            Alert.AlertType.WARNING,
                            "Erreur",
                            null,
                            "Veuillez sélectionner une Note à supprimer",
                            "show"
                    );
                }
            } else {
                CustomAlert.create(
                        Alert.AlertType.WARNING,
                        "Erreur",
                        null,
                        "Veuillez sélectionner une Note à supprimer",
                        "show"
                );
            }
        }
    }

}