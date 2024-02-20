package org.serfa.lpdaoo2024;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

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

        Binder binder1 = new Binder(notebook, 1);
        // binder1.editName("Nouveau nom de classeur");
        // binder1.delete()
        // binder1.editColor(1)
// To  create new binder in database, use Binder class or Notebook class ?


    }
}