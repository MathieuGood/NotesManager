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

//        ArrayList<Binder> binders = notebook.getAllBinders();
        // notebook.createBinder("Sorties", 4);
        // notebook.createBinder("Recherche d'emploi", 3);

        // Instantiate first Binder of ArrayList binders as binder1
//        Binder binder1 = binders.get(2);
//         binder1.editName("Promenades");
        // binder1.editColor(1)

        Binder binder = notebook.createBinder("Immobilier", 1);

//        Tab tab =


    }
}