package org.serfa.lpdaoo2024;

import java.sql.*;

public abstract class DBConnector {

    // Credentials for database connection
    private static Connection connection;
    final static private String dbHost = "51.91.98.35";
    final static private String dbPort = "3306";
    final static private String dbUsername = "notesmanager";
    final static private String dbPassword = "notesserfa2024";
    final static private String dbName = "NotesManager";


    // Get all users from database
    public static void getUsers() {

        try {
            openDatabaseConnection();

            System.out.println("Getting all users");
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT *
                    FROM users
                    """);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                System.out.println("\t> " + name + " / " + email);
            }

            closeDatabaseConnection();

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
        }
    }


    // Check if user email and password match
    public static boolean checkPasswordMatch(String email, String password) {

        try {
            openDatabaseConnection();

            System.out.println("Checking if user e-mail and password match");
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT COUNT(*)
                    FROM users
                    WHERE user_email = ? AND user_password = ?
                    """);

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            boolean match = false;

            resultSet.next();

            if (resultSet.getInt(1) == 1) {
                match = true;
                System.out.println("Match found");
            }

            closeDatabaseConnection();
            return match;

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
        }
        return false;
    }


    // Create new user in database provided name, email and password
    // Return 1 if user was created, 0 if there was an error (likely that email already exists)
    public static int createUser(String name, String email, String password) {

        try {

            openDatabaseConnection();

            System.out.println("Inserting : " + name + " / " + email + " / " + password);

            PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO users (user_name, user_email, user_password)
                        VALUES (?, ?, ?);
                    """);

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);

            int rowsInserted = statement.executeUpdate();
            System.out.println("Number of rows inserted : " + rowsInserted);

            closeDatabaseConnection();

            return rowsInserted;

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
            return 0;
        }
    }


    // Updated user password based on provided user_email
    public static void updateUserPassword(String email, String newPassword) {

        try {
            openDatabaseConnection();

            System.out.println("Updating password for " + email);

            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE users
                    SET user_password = ?
                    WHERE user_email = ?
                    """);
            statement.setString(1, newPassword);
            statement.setString(2, email);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Row updated : " + rowsUpdated);

            closeDatabaseConnection();
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
        }
    }


    // Updated user name based on provided user_email
    public static void updateUserName(String email, String newName) {

        try {
            openDatabaseConnection();

            System.out.println("Updating username for " + email);

            PreparedStatement statement = connection.prepareStatement("""
                    UPDATE users
                    SET user_name = ?
                    WHERE user_email = ?
                    """);
            statement.setString(1, newName);
            statement.setString(2, email);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Row updated : " + rowsUpdated);

            closeDatabaseConnection();
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
        }
    }


    // Delete user based on provided user_email
    public static void deleteUser(String email) {

        try {
            openDatabaseConnection();

            PreparedStatement statement = connection.prepareStatement("""
                    DELETE FROM users WHERE user_email = ?
                    """);
            System.out.println("Deleting " + email);
            statement.setString(1, email);
            statement.executeUpdate();

            closeDatabaseConnection();
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
        }
    }


    // Open database connection
    private static void openDatabaseConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);
        System.out.println("Connection validity to " + dbName + " : " + connection.isValid(5));
    }


    // Close database connection
    private static void closeDatabaseConnection() throws SQLException {
        System.out.println("Closing connection to " + dbName);
        connection.close();
    }


}
