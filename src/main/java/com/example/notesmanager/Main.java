package com.example.notesmanager;

import com.apple.eawt.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

/**
 * The Main class is the entry point of the application.
 * It calls the main method of the LoginWindow class.
 */
public class Main extends javafx.application.Application{

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("NotesManager");
    }


    /**
     * The main method of the application.
     * It calls the main method of the LoginWindow class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        LoginWindow.main(args);
    }

}