package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class RegisterWindow extends Application {

    // The stage of the application
    private Stage stage;

    // The scene of the application
    private Scene scene;

    // The root of the application
    private Parent root;

    // The input field for the user's name
    @FXML
    private TextField inputRegisterName;

    // The input field for the user's email
    @FXML
    private TextField inputRegisterEmail;

    // The input field for the user's password
    @FXML
    private PasswordField inputRegisterPassword;

    // The input field for the user's password confirmation
    @FXML
    private PasswordField inputRegisterConfirmPassword;


    
    public static void main(String[] args) {
        launch();
    }


    
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Set the primary stage for this application
        this.stage = primaryStage;

        try {
            // Load the register window FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("registerWindow.fxml"));

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(fxmlLoader.load());

            // Set the primary stage properties
            primaryStage.setResizable(false);
            stage.setTitle("NotesManager");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }


    
    public void createAccount(ActionEvent e) throws IOException {

        String userEmail = inputRegisterEmail.getText();
        String userName = inputRegisterName.getText();
        String userPassword = inputRegisterPassword.getText();
        String userConfirmPassword = inputRegisterConfirmPassword.getText();

        System.out.println("Create account button clicked");
        System.out.println("Registering user with the following information :" + userEmail + " " + userName + " " + userPassword + " " + userConfirmPassword);

        // If format of all fields is correct and both of the entered passwords match
        if (FormatChecker.checkEmailFormat(userEmail)
                && FormatChecker.checkNameFormat(userName)
                && FormatChecker.checkPasswordFormat(userPassword)
                && userPassword.equals(userConfirmPassword)) {
            // Create user account in database and return the corresponding User object
            int userID = User.createUser(userName, userEmail, userPassword);

            // If userID equals -1, e-mail already exists in database
            if (userID <= 0) {
                String alertContent;
                // If userID is 0, database returned an IntegrityConstraintViolation
                if (userID == 0) {
                    alertContent = "An account has already been registered to " + userEmail + ". Login with this address or specify a different e-mail to create new account.";
                } else {
                    alertContent = "Error occurred during account creation, please try again.";
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account creation");
                alert.setHeaderText("Impossible to create account");
                alert.setContentText(alertContent);
                alert.show();
            } else {
                // If userID > 0, user has been successfully created
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account creation");
                alert.setHeaderText("Account creation");
                alert.setContentText("Account " + userEmail + " successfully created");

                // Wait for the user to click on the OK button to navigate to MainWindow
                if (alert.showAndWait().get() == ButtonType.OK) {
                    MainWindow.setUser(new User(userID, userName, userEmail));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
                    root = loader.load();

                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account creation");
            alert.setHeaderText("Impossible to create account");
            alert.setContentText("""
                    Please verify that :
                     - Your e-mail is correct
                     - Your name contains at least one letter
                     - Your password has at least 8 characters including 1 uppercase letter, 1 lowercase letter, 1 special character and 1 digit
                    """);
            alert.show();
        }
    }


    
    public void backToLogin(ActionEvent e) throws IOException {
        System.out.println("Back to login");

        // Load the login window FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
        root = loader.load();

        // Get the stage from the source of the action event
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create a new scene with the loaded FXML
        scene = new Scene(root);

        // Set the scene on the stage and display the stage
        stage.setScene(scene);
        stage.show();
    }

}