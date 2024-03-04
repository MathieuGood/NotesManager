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
    @FXML
    HTMLEditor noteArea;

    private User user;
    Notebook notebook;
    ArrayList<Binder> binders;
    Note note;
    NoteArea area;

//    MainWindow() {
//        System.out.println("constructor function");
//    }

    @FXML
    public void initialize(){
        System.out.println("initialize function");
        //retrieve User's content
//        User userLog = new User(2, "Soundouce", "soundouce.chibani@gmail.com");
//        initUser(userLog);
        System.out.println(user.toString());

//        notebook = new Notebook(user);
//        binders = notebook.getBinders();
//        //note clicked
//        note = binders.get(0).getTabs().get(0).getNotes().get(0);
//
//        area = new NoteArea(note, noteArea);

    }
    public static void main(String[] args) {
        System.out.println("main Main window function");

        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("start function");
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
        String content = noteArea.getHtmlText();
        area.setContent(content);
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
    public void setUser(User user){
        System.out.println("initUser");
        this.user = user;
        System.out.println(user.toString());
    }
}