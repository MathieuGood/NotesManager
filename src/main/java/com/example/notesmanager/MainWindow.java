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
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class MainWindow  {

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

    @FXML
    public void initialize(){
        //search if note exist for current userId
        ResultSet resultSet = DatabaseManager.select("notes", new String[]{"note_id", "note_content"}, new String[]{"note_id"}, new String[]{"5"});
        try {
            if (resultSet.next()) {
                int note_id = resultSet.getInt(1);
                String note_content = resultSet.getString(2);

                System.out.println("\t> noteID " + note_id + " / content " + note_content);
                //create note
                //Note note = new Note(note_id)
                noteArea.setHtmlText(note_content);
                //return new User(userID, userName, userEmail);
            }
            else{
                System.out.println("Id is not in DB");
                newNote();
            }
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
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
        System.out.println(content);
        int idNote = 5; 
        try {
            int resultID = DatabaseManager.update("notes", "note_content",content, "note_id", String.valueOf(idNote));
            System.out.println("Save. ID : "+ resultID);
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
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
    public void newNote(){
        try {
            int resultID = DatabaseManager.insert("notes", new String[]{"note_name", "note_color_id", "note_content", "tab_id"}, new String[]{"testSou", "2", "", "1"});
            System.out.println("Insertion réussie. ID de l'élément inséré ID : "+ resultID);
        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
    }
}