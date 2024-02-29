package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    Label userNameLabel;
    @FXML
    Button btnLogOut, btnSave;

    @FXML
    MenuButton btnCreateLabel;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        scene = new Scene(fxmlLoader.load(), 300, 500);
        stage.setTitle("main window");
        stage.setScene(scene);
        stage.show();
    }


    public void logOut(ActionEvent event) throws IOException {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("LogOut");
            alert.setHeaderText("You're about to logout");
            alert.setContentText("Do you want to save before existing");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    root = FXMLLoader.load(getClass().getResource("loginWindow.fxml"));
                    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
    }

    public void saveNote(ActionEvent e) {
        System.out.println("btn save note");
    }

    public void saveLabel(ActionEvent e) {

        if (e.getSource() instanceof MenuItem) {
            MenuItem label = (MenuItem) e.getSource();
            String labelContent = label.getText();

            System.out.println(labelContent);
        }
    }

    public void initUserName(String userName) {

        userNameLabel.setText("user name -> " + userName);
    }
}