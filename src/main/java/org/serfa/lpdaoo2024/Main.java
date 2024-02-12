package org.serfa.lpdaoo2024;

public class Main {

    public static void main(String[] args) {

        runDatabaseOperations();

    }

    public static void runDatabaseOperations() {

        // Create new user
        System.out.println(DBConnector.createUser("Mat", "mathieu@test.com", "123456"));
        System.out.println(DBConnector.createUser("Mat", "test@test.com", "123456"));

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