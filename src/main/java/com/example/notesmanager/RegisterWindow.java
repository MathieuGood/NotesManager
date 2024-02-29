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

public class RegisterWindow extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField textFieldEmail, textFieldName, textFieldPassword, textFieldConfirmPassword;


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

    public void createAccount(ActionEvent e)  {
        System.out.println("btn create account");
    }

    public void backToLogin(ActionEvent e) throws IOException {
        System.out.println("back to login");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
        root = loader.load();

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}