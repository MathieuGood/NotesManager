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

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField inputRegisterName, inputRegisterEmail;

    @FXML
    private PasswordField inputRegisterPassword, inputRegisterConfirmPassword;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("registerWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            primaryStage.setResizable(false);
            stage.setTitle("NotesManager");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccount(ActionEvent e) throws IOException {
        System.out.println("Create account button clicked");

        String userEmail = inputRegisterEmail.getText();
        String userName = inputRegisterName.getText();
        String userPassword = inputRegisterPassword.getText();
        String userConfirmPassword = inputRegisterConfirmPassword.getText();

        System.out.println(userEmail);
        System.out.println(userName);
        System.out.println(userPassword);
        System.out.println(userConfirmPassword);

        System.out.println(FormatChecker.checkEmailFormat(userEmail));
        System.out.println(FormatChecker.checkNameFormat(userName));
        System.out.println(FormatChecker.checkPasswordFormat(userPassword));
        System.out.println(userPassword.equals(userConfirmPassword));

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}