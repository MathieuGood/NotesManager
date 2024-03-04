package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The MainWindow class extends the Application class from the JavaFX library.
 * <p>
 * This class represents the main window of the application. It contains private fields for the stage, scene, root, and various UI elements.
 * The stage represents the top-level container for all JavaFX objects; the scene and root are used to construct the scene graph.
 * The UI elements are annotated with @FXML, which allows them to be injected with values from the FXML markup.
 * The class also contains a static User object, a Notebook object, an ArrayList of Binders, a Note object, and a NoteArea object.
 *
 * @see javafx.application.Application
 * @see javafx.stage.Stage
 * @see javafx.scene.Scene
 * @see javafx.scene.Parent
 * @see javafx.scene.control.Label
 * @see javafx.scene.control.Button
 * @see javafx.scene.control.MenuButton
 * @see javafx.scene.web.HTMLEditor
 */
public class MainWindow extends Application {

    // The primary stage for this application, onto which the application scene can be set.
    private Stage stage;

    // The scene for this application. The scene is the container for the visible content in the JavaFX scene graph.
    private Scene scene;

    // The root node of the scene graph. It is an intermediate container for complex scene graph.
    private Parent root;

    // The label displaying the user's name. It is annotated with @FXML so its value can be injected from the FXML file.
    @FXML
    Label userNameLabel;

    // The logout and save buttons. They are annotated with @FXML so their values can be injected from the FXML file.
    @FXML
    Button btnLogOut, btnSave;

    // The button for creating a new label. It is annotated with @FXML so its value can be injected from the FXML file.
    @FXML
    MenuButton btnCreateLabel;

    // The HTML editor for the note area. It is annotated with @FXML so its value can be injected from the FXML file.
    @FXML
    HTMLEditor noteArea;

    // The TreeView UI element for displaying the binder structure in the application.
    // It is annotated with @FXML so its value can be injected from the FXML file.
    @FXML
    private TreeView<String> binderTree;

    // The static User object for this application.
    private static User user;

    // The Notebook object for this application.
    Notebook notebook;

    // The ArrayList of Binder objects for this application.
    ArrayList<Binder> binders;

    // The Note object for this application.
    Note note;

    // The NoteArea object for this application.
    NoteArea area;


    /**
     * This method is called when the MainWindow is initialized.
     * It sets up the user interface and initializes the notebook, binders, and note.
     * It also sets up the NoteArea with the first note from the first tab of the first binder.
     */
    @FXML
    public void initialize() {
        // Print debug information to the console
        System.out.println("initialize function");
        System.out.println(user.toString());

        // Set the text of the userNameLabel to greet the user
        userNameLabel.setText("Bonjour " + user.getUserName());

        // Initialize the notebook with the current user
        notebook = new Notebook(user);

        // Get the binders from the notebook
        binders = notebook.getBinders();

        // Get the first note from the first tab of the first binder
        // This is the note that will be displayed when the MainWindow is opened
        note = binders.get(0).getTabs().get(0).getNotes().get(0);

        // Initialize the NoteArea with the note and the HTMLEditor from the user interface
        area = new NoteArea(note, noteArea);


        NotebookTreeView notebookTreeView = new NotebookTreeView(binderTree, notebook);
        notebookTreeView.createTreeView();
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
    public void saveLabel(ActionEvent e) {

        if (e.getSource() instanceof MenuItem) {
            MenuItem label = (MenuItem) e.getSource();
            String labelContent = label.getText();

            System.out.println(labelContent);
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
     * The application initialization method. This method is called after the init method has returned, and after the system is ready for the application to begin running.
     *
     * @param stage the primary stage for this application, onto which the application scene can be set. Applications may create other stages, if needed, but they will not be primary stages.
     * @throws Exception if something goes wrong
     */

    @Override
    public void start(Stage stage) throws Exception {

    }
}