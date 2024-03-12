package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * The LoginWindow class extends the Application class from the JavaFX library.
 * <p>
 * This class represents the login window of the application. It contains private fields for the stage, scene, root, and input fields for login email and password.
 * The stage represents the top-level container for all JavaFX objects; the scene and root are used to construct the scene graph.
 * The inputLoginEmail and inputLoginPassword fields are annotated with @FXML, which allows them to be injected with values from the FXML markup.
 *
 * @see javafx.application.Application
 * @see javafx.stage.Stage
 * @see javafx.scene.Scene
 * @see javafx.scene.Parent
 * @see javafx.scene.control.TextField
 * @see javafx.scene.control.PasswordField
 */
public class LoginWindow extends Application {

    // The primary stage for this application, onto which the application scene can be set.
    private Stage stage;

    // The scene for this application. The scene is the container for the visible content in the JavaFX scene graph.
    private Scene scene;

    // The root node of the scene graph. It is an intermediate container for complex scene graph.
    private Parent root;

    // The TextField where the user enters their email for login. It is annotated with @FXML so its value can be injected from the FXML file.
    @FXML
    private TextField inputLoginEmail;

    // The PasswordField where the user enters their password for login. It is annotated with @FXML so its value can be injected from the FXML file.
    @FXML
    private PasswordField inputLoginPassword;



    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned, and after the system is ready for the application to begin running.
     *
     * @param args the command line arguments passed to the application. An application may get these parameters using the Application.getParameters() method.
     */
    public static void main(String[] args) {
        System.out.println("main login window function");
        launch();
    }


    /**
     * This method is the entry point for the JavaFX application.
     * It is called after the init method has returned, and after the system is ready for the application to begin running.
     * <p>
     * It sets the primary stage for the application, loads the login window FXML file, sets the scene, and displays the stage.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     * @throws Exception if an error occurs during loading the FXML file or setting the scene.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set the primary stage for this application
        this.stage = primaryStage;

        try {
            // Load the login window FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("loginWindow.fxml"));

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(fxmlLoader.load());

            // Set the primary stage properties
            primaryStage.setResizable(true);
            stage.setTitle("NotesManager");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }


    /**
     * This method is called when the login button is pressed.
     * It retrieves the user's email and password from the input fields, checks if they are in the correct format,
     * and if they match the stored user credentials.
     * <p>
     * If the credentials are valid, it sets the static user in the MainWindow to the logged in user and navigates to the MainWindow.
     * If the credentials are invalid, it shows an alert informing the user that the email and password do not match.
     * If the format of the email or password is incorrect, it shows an alert informing the user of the incorrect format.
     *
     * @param e the action event
     * @throws IOException if an I/O error occurs during navigation
     */
    public void login(ActionEvent e) throws IOException {
        System.out.println("Login button pressed");

        // Get userEmail and userPassword from fields
        String userEmail = inputLoginEmail.getText();
        String userPassword = inputLoginPassword.getText();
        System.out.println("User : " + userEmail + "  / Password : " + userPassword);

        // Check if email and password are in the right format
        if (FormatChecker.checkEmailFormat(userEmail) && FormatChecker.checkPasswordFormat(userPassword)) {

            // Instantiate user and assign it to User object if email and password match
            User user = User.checkPasswordMatch(userEmail, userPassword);

            // If user is a User object (email and password match)
            if (user != null) {
                // Static user
                MainWindow.setUser(user);


                // Navigate to MainWindow
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
                root = loader.load();

                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("E-mail and password DO NOT match!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impossible to login");
                alert.setHeaderText("Impossible to login");
                alert.setContentText("E-mail and password do not match");
                alert.show();

            }
        } else {
            System.out.println("Incorrect format for either e-mail or password.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Impossible to login");
            alert.setHeaderText("Impossible to login");
            alert.setContentText("Format of e-mail and/or password is incorrect.");
            alert.show();

        }
    }


    /**
     * This method is called when the register button is pressed. It navigates to the RegisterWindow.
     *
     * @param e the action event
     * @throws IOException if something goes wrong
     */
    public void register(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerWindow.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}