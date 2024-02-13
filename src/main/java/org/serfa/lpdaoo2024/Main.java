package org.serfa.lpdaoo2024;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // Set FlatLaf "Look and Feel" for Swing
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Open Login Window
        LoginWindow loginWindow = new LoginWindow();

    }

    public static void testDatabaseOperations() {

        // Create new user
        // System.out.println(DBConnector.createUser("Mat", "mathieu@test.com", "123456"));

        // Update user password
        // DBConnector.updateUserPassword("mathieu@mathieu.com", "newpassword");

        // Update user name
        // DBConnector.updateUserName("mathieu@mathieu.com", "NouveauMathieu");

        // Check if username and password match
        // DBConnector.checkPasswordMatch("mathieu@mathieu.com", "newpassword");

        // Delete user
        // DBConnector.deleteUser("mathieu@mathieu.com");
    }
}