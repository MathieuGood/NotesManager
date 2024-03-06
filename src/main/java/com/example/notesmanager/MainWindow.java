package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 * @see NoteLabel
 * @see Button
 * @see MenuButton
 * @see HTMLEditor
 */
public class MainWindow extends Application {


    /**
     * public MenuButton btnCreateAction;
     * The primary stage for this application, onto which the application scene can be set.
     */
    private Stage stage;

    /**
     * The scene for this application. The scene is the container for the visible content in the JavaFX scene graph.
     */
    private Scene scene;

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
     * The logout and save buttons. They are annotated with @FXML so their values can be injected from the FXML file.
     */
    @FXML
    Button btnLogOut, btnSave;

    /**
     * The button for creating a new label. It is annotated with @FXML so its value can be injected from the FXML file.
     */
    @FXML
    MenuButton btnFilterLabel;

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

    /**
     * The current note being displayed or edited in the application.
     */
    Note note;

    /**
     * The area where the content of the current note is displayed and edited.
     */
    NoteArea area;


    /**
     * The initialize method is called after all the FXML fields have been injected.
     * It sets the text of the userNameLabel to greet the user, initializes the notebook with the current user, and gets the binders from the notebook.
     * It also initializes the note area with the current note and the HTMLEditor from the user interface, and sets the content of the note area to the content of the current note.
     */
    @FXML
    public void initialize() {

        // Set the text of the userNameLabel to greet the user
        userNameLabel.setText("Bonjour " + user.getUserName());

        // Initialize the notebook with the current user
        notebook = new Notebook(user);


        // Get binders from the notebook
        binders = notebook.getBinders();

        //
        // FOR DEVELOPMENT PURPOSES ONLY
        //
        // Get the first note from the first tab of the first binder
        // This is the note that will be displayed when the MainWindow is opened
        note = binders.get(0).getTabs().get(0).getNotes().get(1);

        // Initialize NoteArea with the note and the HTMLEditor from the user interface
        area = new NoteArea(note, noteArea);

        // Set the content of the note area to the content of the current note
        setNoteLabelDropdownContent();








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
        area.setContent(content);
        System.out.println("btn save note");
    }


    /**
     * This method is called when a label is saved.
     * It checks if the source of the event is a MenuItem, and if so, retrieves the text of the label and prints it to the console.
     *
     * @param e the action event
     */
    public void setLabelFilter(ActionEvent e) {
        // Check if the source of the event is a MenuItem
        if (e.getSource() instanceof MenuItem selectedLabel) {
            // Retrieve name of the label
            String labelName = selectedLabel.getText();
            System.out.println(labelName);

            // Filter notes by label name and rerender the tree view
            notebook.setNotebookContent(labelName);
            generateTreeView();
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
    public void setNoteLabelDropdownContent() {
        // Fetch all labels from the database
        Map<Integer, String> labelsResult = fetchAllLabels();

        // Loop over the fetched labels
        for (Map.Entry<Integer, String> entry : labelsResult.entrySet()) {

            // Create a new menu item for each label
            MenuItem label = new MenuItem(entry.getValue());

            // Set the action of the menu item to trigger the setFilter method
            label.setOnAction(this::setLabelFilter);

            // Add the menu item to the dropdown menu
            btnFilterLabel.getItems().add(label);
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
        NoteLabel noteLabels = new NoteLabel();

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
    private void addBinderMenu(ActionEvent actionEvent) {

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

    }

    @FXML
    private void deleteBinderMenu() {

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
    private void editTabMenu(ActionEvent event) {

    }

    @FXML
    private void deleteTabMenu(ActionEvent event) {

    }


    /************  Notes  *******************/


    @FXML
    private void addNoteMenu() {

        System.out.println("Selected item: " + binderTree.getSelectionModel().getSelectedItem());

        TreeItem<String> selectedDivider = binderTree.getSelectionModel().getSelectedItem();


        if (selectedDivider != null && selectedDivider.getParent() != null && selectedDivider.getParent().getParent() == binderTree.getRoot()) {

            Tab selectedTab = notebook.getTabByDividerName(selectedDivider.getValue());

            if (selectedTab != null) {
                TextInputDialog dialog = new TextInputDialog("Nom de la Note");
                dialog.setTitle("Création d'une Nouvelle Note");
                dialog.setHeaderText("Entrez le nom de la nouvelle note :");
                dialog.setContentText("Nom :");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {

                    Note newNote = selectedTab.createNote(name, "");

                    // Mettez à jour l'interface utilisateur
                    TreeItem<String> newNoteItem = new TreeItem<>(name);
                    selectedDivider.getChildren().add(newNoteItem);


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

    }

    @FXML
    private void deleteNoteMenu(ActionEvent event) {

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