package com.example.notesmanager;

import javafx.stage.Stage;

import java.awt.*;
import java.util.Objects;

/**
 * The Main class is the entry point of the application.
 * It calls the main method of the LoginWindow class.
 */
public class Main {

    public void start(Stage primaryStage) {
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