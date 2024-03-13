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



    
    public static void main(String[] args) {
        System.out.println("main login window function");
        launch();
    }


    
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


    
    public void register(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerWindow.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}