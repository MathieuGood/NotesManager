package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        System.out.println("login");
        String userEmail = TextFieldEmail.getText();
        System.out.println(userEmail);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        root = loader.load();

        MainWindow mainWindow = loader.getController();
        mainWindow.initUserName(userEmail);

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void register(ActionEvent e) {
        System.out.println("register");
    }
}