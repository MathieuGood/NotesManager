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

//        LoginWindow loginWindow = new LoginWindow();
//        MainWindow mainWindow = new MainWindow("bon.mathieu@gmail.com");
//        RegisterWindow registerWindow = new RegisterWindow();


        testDatabaseOperations();

    }

    public static void testDatabaseOperations() {

        // Create new user
//         User user = User.createUser("Mat", "mat@mat.com", "123456*G");


        // Check if user email and password match
        User user = User.checkPasswordMatch("bon.mathieu@gmail.com", "Mathieu*1");
        System.out.println("User ID : " + user.getUserID());

        // Create new Notebook
        Notebook notebook = new Notebook(user);
//        notebook.getContentTree();
//
        ArrayList<Binder> binders = notebook.getBinders();
//
        Binder binder = binders.get(0);
        binder.editName("Formation Développeur ");
//
        Tab games = binder.createTab("Jeux", 1);
        Note bejeweled = games.createNote("Bejeweled", "Clone de Bejeweled en React Native", 1);
        bejeweled.editContent("Mobile app for Android and iOS with React Native : Bejeweled Clone.");


//        Binder binder = notebook.createBinder("Immobilier", 1);

//        Tab tab = binder.createTab("Appartements", 1);

//        Note note = tab.createNote("Duplex Neudorf", "Grand appartement de 100 mètres carrés.", 1);
//        Note note2 = tab.createNote("Garage sous-sol", "Box pour deux motos.", 1);

    }
}