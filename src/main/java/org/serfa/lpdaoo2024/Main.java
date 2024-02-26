package org.serfa.lpdaoo2024;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        // Set FlatLaf "Look and Feel" for Swing
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Open Login Window
//        LoginWindow loginWindow = new LoginWindow();
//        MainWindow mainWindow = new MainWindow("bon.mathieu@gmail.com");
//        RegisterWindow registerWindow = new RegisterWindow();


        testDatabaseOperations();

    }

    public static void testDatabaseOperations() {


        // Create new user
        // User.createUser("Mat", "mat@mat.com", "123456*G");

        // Check if user email and password match
        int userID = User.checkPasswordMatch("bon.mathieu@gmail.com", "Mathieu*1");
        System.out.println("User ID : " + userID);

        // Create new Notebook
        Notebook notebook = new Notebook(userID);
        notebook.getContentTree();

        // ArrayList<Binder> binders = notebook.getAllBinders();


        Binder binder = notebook.createBinder("Immobilier", 1);

        Tab tab = binder.createTab("Appartements", 1);

        Note note = tab.createNote("Duplex Neudorf", "Grand appartement de 100 mètres carrés.", 1);
        Note note2 = tab.createNote("Garage sous-sol", "Box pour deux motos.", 1);

        System.out.println(note.getNoteContent());


    }
}