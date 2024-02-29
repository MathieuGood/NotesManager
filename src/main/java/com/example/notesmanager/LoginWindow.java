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
import javafx.stage.Stage;

import java.io.IOException;

public class LoginWindow extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField TextFieldEmail;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("loginWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            primaryStage.setResizable(false);
            stage.setTitle("NotesManager");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void login(ActionEvent e) throws IOException {
        System.out.println("Login button pressed");

        // Get userEmail and userPassword from fields
        String userEmail = TextFieldEmail.getText();
        String userPassword = "Mathieu*1";
        System.out.println("User : " + userEmail + "  / Password : " + userPassword);

        // Check if email and password are in the right format
        if (FormatChecker.checkEmailFormat(userEmail) && FormatChecker.checkPasswordFormat(userPassword)) {

            // Instantiate user and assign it to User object if email and password match
            User user = User.checkPasswordMatch(userEmail, userPassword);

            // If user is a User object (email and password match)
            if (user != null) {
                // Navigate to MainWindow
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
                root = loader.load();

                MainWindow mainWindow = loader.getController();
                mainWindow.initUserName(userEmail);

                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("E-mail and password DO NOT match!");
            // TODO : Show alert
                Alert alert = new Alert( Alert.AlertType.INFORMATION, "Testing the alert");

            }
        } else {
            System.out.println("Incorrect format for either e-mail or password.");
            // TODO : Show alert
            Alert alert = new Alert( Alert.AlertType.INFORMATION, "Testing the alert 2");

        }


    }

    public void register(ActionEvent e) {
        System.out.println("register");
    }
}