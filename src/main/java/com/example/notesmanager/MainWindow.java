package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;


/**
 * The MainWindow class extends the Application class from the JavaFX library.
 * <p>
 * This class represents the main window of the application. It contains private fields for the stage, scene, root, and various UI elements.
 * The stage represents the top-level container for all JavaFX objects; the scene and root are used to construct the scene graph.
 * The UI elements are annotated with @FXML, which allows them to be injected with values from the FXML markup.
 * The class also contains a static User object, a Notebook object, an ArrayList of Binders, a Note object, and a NoteArea object.
 *
 * @see Application
 * @see Stage
 * @see Scene
 * @see Parent
 * @see LabelMenuBuilder
 * @see Button
 * @see MenuButton
 * @see HTMLEditor
 */
public class MainWindow extends Application {


    /**
     * The root node of the scene graph. It is an intermediate container for complex scene graph.
     */
    private Parent root;

    /**
     * The label displaying the user's name. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    Label userNameLabel;

    /**
     * The label displaying the note's title. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    Label noteTitle;

    /**
     * The label displaying note's labels. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    Label noteLabels;

    /**
     * Pane that integrates the HTMLEditor, the note title, and the labels. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    private Pane noteSelectedPane;

    /**
     * Pane that integrates the message prompting to select a note. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    private Pane waitingNoteSelectedPane;

    /**
     * The logout and save buttons. They are annotated with @FXML so their values can be injected from the FXML file.
     */
    @FXML
    Button btnLogOut, btnSave;

    /**
     * The button for creating a new label. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    MenuButton btnFilterLabel;

    @FXML
    MenuButton btnChooseLabel;

    /**
     * The HTML editor for the note area. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    HTMLEditor noteArea;

    /**
     * The TreeView UI element for displaying the binder structure in the application.
     * It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    private TreeView<String> binderTree;

    /**
     * Button for creating actions. This button is used to trigger the creation of new items in the application.
     * It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    MenuButton btnCreateAction;

    /**
     * The current user of the application. This is a static field, meaning it is shared across all instances of the MainWindow class.
     */
    private static User user;

    /**
     * The notebook associated with the current user. This notebook contains all the binders and notes for the user.
     */
    Notebook notebook;

    /**
     * A list of binders in the notebook. Each binder can contain multiple tabs, and each tab can contain multiple notes.
     */
    ArrayList<Binder> binders;

    static NoteArea areaStatic;


    /**
     * The initialize method is called after all the FXML fields have been injected.
     * It sets the text of the userNameLabel to greet the user, initializes the notebook with the current user, and gets the binders from the notebook.
     * It also initializes the note area with the current note and the HTMLEditor from the user interface, and sets the content of the note area to the content of the current note.
     */
    @FXML
    public void initialize() {

        // Set the text of the userNameLabel to greet the user
        userNameLabel.setText(user.getUserName());

        // Initialize the notebook with the current user
        notebook = new Notebook(user);

        // Initialize blank NoteArea in the HTMLEditor from the user interface
        NoteArea.setNoteArea(noteArea, noteSelectedPane, waitingNoteSelectedPane, noteTitle, noteLabels);

        // Set the content of note filter dropdown menu
        setLabelFilterDropdownContent(btnFilterLabel, this::setLabelFilter);

        // Set the content of note label selector dropdown menu
        setLabelFilterDropdownContent(btnChooseLabel, this::setNoteLabel);

        // Generate the tree view for the notebook
        generateTreeView();
    }


    /**
     * This method is called when the logout button is pressed.
     * It shows a confirmation dialog to the user, asking if they want to save their work before logging out.
     * If the user confirms, the method navigates back to the LoginWindow.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    public void logOut(ActionEvent event) throws IOException {

        // Create a new confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("LogOut");
        alert.setHeaderText("You're about to logout");
        alert.setContentText("Do you want to save before existing");

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


    /**
     * This method is called when the save note button is pressed.
     * It retrieves the HTML content from the note area and sets it as the content of the current note.
     * It also prints a debug message to the console.
     *
     * @param e the action event
     */
    public void saveNote(ActionEvent e) {
        String content = noteArea.getHtmlText();
        NoteArea.note.editContent(content);
        System.out.println("btn save note");
    }


    /**
     * This method is used to set the label filter for the notebook.
     * It is triggered when a MenuItem (representing a label) is selected from the dropdown menu.
     * The method retrieves the name of the selected label, filters the notes in the notebook by this label,
     * and then rerenders the tree view to reflect the filtered notes.
     * It also expands the root of the tree view.
     *
     * @param e the action event triggered when a MenuItem is selected
     */
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


    public void setNoteLabel(ActionEvent e) {
        // Check if the source of the event is a MenuItem
        if (e.getSource() instanceof MenuItem selectedLabel) {
            // Retrieve name of the label
            String labelName = selectedLabel.getText();
            System.out.println(labelName);

            // Get current Note object from NoteArea
            Note note = NoteArea.getNote();

            // If the selected label is already in the note, set the style of the MenuItem to bold
            ArrayList<NoteLabel> labels = note.getLabels();
            for (NoteLabel label : labels) {
                System.out.println("Label in note : " + label.getLabelName());
                if (label.getLabelName().equals(labelName)) {
                    selectedLabel.setStyle("-fx-font-weight: bold");
                }
            }

            // Add the selected label to the note
            note.addNoteLabel(labelName);

            // Change the text of the MenuItem mathcing the selected label in the list to surround it with brackets
            selectedLabel.setStyle("-fx-font-weight: bold");


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


    /**
     * This method is used to set the user for the MainWindow.
     * It prints a debug message to the console, sets the static user field to the provided user, and then prints the user's toString representation to the console.
     *
     * @param pUser the user to set
     */
    public static void setUser(User pUser) {
        System.out.println("initUser");
        user = pUser;
        System.out.println(user.toString());
    }


    /**
     * This method is used to set the content of the note label dropdown menu.
     * It fetches all labels from the database and adds them as menu items to the dropdown menu.
     * Each menu item is set to trigger the setFilter method when selected.
     */
    public void setLabelFilterDropdownContent(MenuButton menuButton, EventHandler<ActionEvent> callback) {
        // Fetch all labels from the database
        Map<Integer, String> labelsResult = fetchAllLabels();

        // Loop over the fetched labels
        for (Map.Entry<Integer, String> entry : labelsResult.entrySet()) {

            // Create a new menu item for each label
            MenuItem label = new MenuItem(entry.getValue());

            // Set the action of the menu item to trigger the callback
            label.setOnAction(callback);

            // Add the menu item to the dropdown menu
            menuButton.getItems().add(label);
        }
    }


    /**
     * This method is used to fetch all labels from the database.
     * It creates a new instance of the NoteLabel class and calls its getAllLabels method to retrieve all labels.
     * The labels are stored in a map where the key is the label's ID and the value is the label's name.
     * The method then iterates over the map and prints each label's ID and name to the console.
     * Finally, it returns the map of labels.
     *
     * @return a map where the key is the label's ID and the value is the label's name
     */
    public Map<Integer, String> fetchAllLabels() {
        LabelMenuBuilder noteLabels = new LabelMenuBuilder();

        // Call the getAllLabels method of the NoteLabel instance to fetch all labels from the database
        // The labels are stored in a map where the key is the label's ID and the value is the label's name
        Map<Integer, String> labelsResult = noteLabels.getAllLabels();

        // Iterate over the map of labels
        for (Map.Entry<Integer, String> entry : labelsResult.entrySet()) {
            // Print each label's ID and name to the console
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        // Return the map of labels
        return labelsResult;
    }

    /**
     * This method is used to generate the tree view for the notebook.
     * It creates an instance of the NotebookTreeView class, passing the binderTree and notebook as parameters.
     * Then, it calls the createTreeView method on the NotebookTreeView instance to generate the tree view.
     */
    public void generateTreeView() {
        NotebookTreeView notebookTreeView = new NotebookTreeView(binderTree, notebook);
        notebookTreeView.createTreeView();
    }


    /**
     * The application initialization method. This method is called after the init method has returned, and after the system is ready for the application to begin running.
     *
     * @param stage the primary stage for this application, onto which the application scene can be set. Applications may create other stages, if needed, but they will not be primary stages.
     * @throws Exception if something goes wrong
     */

    @Override
    public void start(Stage stage) throws Exception {

    }

    /************* Binders ******************/

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

                //  code hexadécimal de la couleur du nouveau classeur
                String colorHex = NotebookColor.getHexColorByID(binderColorId);
                Node circle = getColorCircle(colorHex);

                TreeItem<String> newBinderItem = new TreeItem<>(binderName);
                newBinderItem.setGraphic(circle);

                // Ajout du nouveau classeur à la racine de l'arbre
                binderTree.getRoot().getChildren().add(newBinderItem);
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
            } else {
                showAlert("Classeur non trouvé.");
            }
        } else {
            showAlert("Veuillez sélectionner un classeur à éditer.");
        }
    }

    @FXML
    private void deleteBinderMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() == binderTree.getRoot()) {
            String binderName = selectedItem.getValue();
            Binder selectedBinder = notebook.getBinderByName(binderName);


            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation de suppression");
            confirmAlert.setHeaderText("Supprimer le classeur \"" + binderName + "\" ?");
            confirmAlert.setContentText("Êtes-vous sûr ? Cette action est irréversible et supprimera tous les intercalaires et notes associés.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Suppression du classeur
                int binderID = selectedBinder.getBinderID();
                int deleteResult = notebook.deleteBinder(binderID);

                if (deleteResult > 0) {

                    binderTree.getRoot().getChildren().remove(selectedItem);
                } else {
                    showAlert("Erreur lors de la suppression du classeur.");
                }
            }
        } else {
            showAlert("Veuillez sélectionner un classeur à supprimer.");
        }
    }


    /************* Tabs *******************/
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

                            //  code hexadécimal de la couleur du nouveau classeur
                            String colorHex = NotebookColor.getHexColorByID(tabColorID);
                            Node circle = getColorCircle(colorHex);

                            TreeItem<String> newTabItem = new TreeItem<>(tabName);
                            newTabItem.setGraphic(circle);

                            selectedBinder.getChildren().add(newTabItem);
                        } else {
                            showAlert("La couleur sélectionnée est invalide.");
                        }
                    }
                }
            } else {
                System.out.println("Erreur : Binder correspondant au nom sélectionné introuvable.");
                showAlert("Veuillez sélectionner un classeur pour ajouter un intercalaire.");
            }
        } else {
            System.out.println("Erreur : Aucun classeur sélectionné.");
            showAlert("Aucun classeur sélectionné.");
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
                    ChoiceDialog<String> colorDialog = new ChoiceDialog<>(NotebookColor.getHexColorByID(selectedTab.getTabColorID()), colorNames);
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
                    showAlert("Intercalaire non trouvé.");
                }
            } else {
                showAlert("Veuillez sélectionner un intercalaire à éditer.");
            }
        } else {
            showAlert("Aucun élément sélectionné.");
        }
    }


    @FXML
    private void deleteTabMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null && !selectedItem.getParent().equals(binderTree.getRoot())) {

            String selectedTabName = selectedItem.getValue();
            Tab selectedTab = notebook.getTabByName(selectedTabName);

            if ( (selectedTab != null)) {

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirmation de suppression");
                confirmAlert.setHeaderText("Supprimer l'intercalaire \"" + selectedTabName + "\" ?");
                confirmAlert.setContentText("Êtes-vous sûr ? Cette action est irréversible.");

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Binder binder = notebook.getBinderByName(selectedItem.getParent().getValue());
                    if (binder != null) {
                        int TabID = selectedTab.getTabID();
                        int deleteResult = binder.deleteTab(TabID);
                        if ( deleteResult > 0) {
                            selectedItem.getParent().getChildren().remove(selectedItem);
                        } else {
                            showAlert("Erreur lors de la suppression de l'intercalaire.");
                        }

                    }
                }else  {
                    showAlert("Intercalaire non trouvé.");
                }


            } else {
                showAlert("Veuillez sélectionner un intercalaire à supprimer.");
            }

        }

    }


    /************  Notes  *******************/


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

                    // Mettez à jour l'interface utilisateur
                    TreeItem<String> newNoteItem = new TreeItem<>(name);
                    selectedNameTab.getChildren().add(newNoteItem);


                    // A voir si cette ligne est utile
                    noteArea.setHtmlText(newNote.getNoteContent());
                });
            } else {
                showAlert("Intercalaire non trouvé.");
            }
        } else {
            showAlert("Veuillez sélectionner un intercalaire pour ajouter une note.");
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

                    TextInputDialog dialog = new TextInputDialog(selectedNote.getNoteContent());
                    dialog.setTitle("Modification du nom de la note");
                    dialog.setHeaderText("Modifiez le nom de la note :");
                    dialog.setContentText("Nom :");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(newName -> {

                        int updateResult = selectedNote.editName(newName);
                        if (updateResult > 0) {
                            selectedItem.setValue(newName);
                        } else {
                            showAlert("Erreur lors de la mise à jour du nom de la note.");
                        }

                    });
                } else {
                    showAlert("Note non trouvé.");
                }
            } else {
                showAlert("Intercalaire non trouvé.");
            }
        } else  {
            showAlert("Veuillez sélectionner une note à éditer.");
        }

    }

    @FXML
    private void deleteNoteMenu() {

        TreeItem<String> selectedItem = binderTree.getSelectionModel().getSelectedItem();

        if (selectedItem != null && selectedItem.getParent() != null && selectedItem.getParent().getParent() != null) {

            Tab selectedTab = notebook.getTabByName(selectedItem.getParent().getValue());

            if (selectedTab != null) {
                Note selectedNote = selectedTab.getNotes().stream()
                        .filter(note->note.getNoteName().equals(selectedItem.getValue()))
                        .findFirst().orElse(null);

                if (selectedNote != null) {

                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Confirmation de suppression");
                    confirmAlert.setHeaderText("Supprimer la note \"" + selectedNote.getNoteName() + "\" ?");
                    confirmAlert.setContentText("Êtes-vous sûr ? Cette action est irréversible.");

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if ( result.isPresent() && result.get() == ButtonType.OK) {

                        int noteID = selectedNote.getNoteID();
                        int deleteResult = selectedTab.deleteNote(noteID);

                        if (deleteResult > 0) {
                            selectedItem.getParent().getChildren().remove(selectedItem);
                        } else {
                            showAlert("Erreur lors de la suppression de la note.");
                        }
                    } else {
                        showAlert("Intercalaire non trouvé.");
                    }
                } else {
                    showAlert("Veuillez sélectionner une note à supprimer.");
                }
            }
        }

    }




    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Action Requise");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private Node getColorCircle(String colorHex) {
        Circle circle = new Circle(5, Color.web(colorHex));
        return circle;
    }

}