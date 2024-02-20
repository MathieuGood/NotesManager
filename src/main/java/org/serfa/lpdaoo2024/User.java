package org.serfa.lpdaoo2024;

import java.sql.*;
import java.sql.SQLException;

public class User {

//    private final int userID;
//    private final String userName;
//    private final String userEmail;


//    public User(int userID, String userName, String userEmail) {
//        super();
//        this.userID = userID;
//        this.userName = userName;
//        this.userEmail = userEmail;
//
//    }

    /**
     * This method creates a new user in the database with the provided username, email, and password.
     * It opens a connection to the database, prepares a SQL statement to insert the new user into the users table.
     * The method then executes the statement and retrieves the generated key for the new user.
     * If the user is successfully created, the generated user_id is returned.
     * If a SQLIntegrityConstraintViolationException occurs (e.g. a duplicate email), 0 is returned.
     * If any other SQLException occurs, -1 is returned.
     * The database connection is closed before the method returns.
     *
     * @param userName     The username of the new user.
     * @param userEmail    The email of the new user.
     * @param userPassword The password of the new user.
     * @return The generated user_id if the user is successfully created, 0 if a SQLIntegrityConstraintViolationException occurs, -1 if any other SQLException occurs.
     */
    public static int createUser(String userName, String userEmail, String userPassword) {

        try {
            Connection connection = DatabaseManager.openDatabaseConnection();

            System.out.println("Inserting : " + userName + " / " + userEmail + " / " + userPassword);

            PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO users (user_name, user_email, user_password)
                                VALUES (?, ?, ?);
                            """,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, userName);
            statement.setString(2, userEmail);
            statement.setString(3, userPassword);

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            System.out.println("Number of rows inserted : " + rowsInserted);

            // Get last inserted ID
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);
            System.out.println("Last inserted ID : " + lastInsertedID);

            DatabaseManager.closeDatabaseConnection(connection);

            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;
        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }


    /**
     * This method checks if the provided email and password match a user in the database.
     * It opens a connection to the database, prepares a SQL statement to select the user_id
     * from the users table where the user_email and user_password match the provided email and password.
     * If a match is found, the user_id is returned, otherwise 0 is returned.
     * The database connection is closed before the method returns.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The user_id if a match is found, 0 otherwise.
     */
    public static int checkPasswordMatch(String email, String password) {

        try {
            Connection connection = DatabaseManager.openDatabaseConnection();

            System.out.println("Checking if user e-mail and password match");
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT user_id
                    FROM users
                    WHERE user_email = ? AND user_password = ?
                    """);

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            // If the returned result is a user
            resultSet.next();
            int userID = resultSet.getInt(1);
            if (userID > 0) {
                System.out.println("Match found : user ID #" + userID);
            }

           DatabaseManager.closeDatabaseConnection(connection);
            return userID;

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
            return 0;
        }
    }





// Unused methods (need refactoring to be used)

//    // Get all users from database
//    public void getUsers() {
//
//        try {
//            openDatabaseConnection();
//
//            System.out.println("Getting all users");
//            PreparedStatement statement = connection.prepareStatement("""
//                    SELECT *
//                    FROM users
//                    """);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                String name = resultSet.getString(2);
//                String email = resultSet.getString(3);
//                System.out.println("\t> " + name + " / " + email);
//            }
//
//            closeDatabaseConnection();
//
//        } catch (SQLException e) {
//            System.out.println("SQL Error : " + e);
//        }
//    }
//
//

//
//    // Updated user password based on provided user_email
//    public static void updateUserPassword(String email, String newPassword) {
//
//        try {
//            openDatabaseConnection();
//
//            System.out.println("Updating password for " + email);
//
//            PreparedStatement statement = connection.prepareStatement("""
//                    UPDATE users
//                    SET user_password = ?
//                    WHERE user_email = ?
//                    """);
//            statement.setString(1, newPassword);
//            statement.setString(2, email);
//            int rowsUpdated = statement.executeUpdate();
//            System.out.println("Row updated : " + rowsUpdated);
//
//            closeDatabaseConnection();
//        } catch (SQLException e) {
//            System.out.println("SQL Error : " + e);
//        }
//    }
//
//
//    // Updated user name based on provided user_email
//    public static void updateUserName(String email, String newName) {
//
//        try {
//            openDatabaseConnection();
//
//            System.out.println("Updating username for " + email);
//
//            PreparedStatement statement = connection.prepareStatement("""
//                    UPDATE users
//                    SET user_name = ?
//                    WHERE user_email = ?
//                    """);
//            statement.setString(1, newName);
//            statement.setString(2, email);
//            int rowsUpdated = statement.executeUpdate();
//            System.out.println("Row updated : " + rowsUpdated);
//
//            closeDatabaseConnection();
//        } catch (SQLException e) {
//            System.out.println("SQL Error : " + e);
//        }
//    }
//
//
//    // Delete user based on provided user_email
//    public static void deleteUser(String email) {
//
//        try {
//            openDatabaseConnection();
//
//            PreparedStatement statement = connection.prepareStatement("""
//                    DELETE FROM users WHERE user_email = ?
//                    """);
//            System.out.println("Deleting " + email);
//            statement.setString(1, email);
//            statement.executeUpdate();
//
//            closeDatabaseConnection();
//        } catch (SQLException e) {
//            System.out.println("SQL Error : " + e);
//        }
//    }


}
