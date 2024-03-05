package com.example.notesmanager;

import java.sql.*;


/**
 * Represents a user in the system.
 * It contains fields for the user's ID, name, and email, and methods for getting and setting these values.
 * It also contains methods for creating a new user and checking if a user's password matches the one stored in the database.
 */
public class User {


    // The unique identifier for the User.
    private int userID;

    // The name of the User.
    private String userName;

    // The email of the User.
    private String userEmail;


    /**
     * Constructs a new User object with the given ID, name, and email.
     *
     * @param userID    The unique identifier for the user.
     * @param userName  The name of the user.
     * @param userEmail The email of the user.
     */
    public User(int userID, String userName, String userEmail) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    /**
     * Returns the user's ID.
     *
     * @return The user's ID.
     */
    public int getUserID() {
        return userID;
    }


    /**
     * Returns the user's name.
     *
     * @return The user's name.
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Returns the user's email.
     *
     * @return The user's email.
     */
    public String getUserEmail() {
        return userEmail;
    }


    /**
     * Sets the user's ID.
     *
     * @param userID The user's new ID.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Sets the user's name.
     *
     * @param userName The user's new name.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * Sets the user's email.
     *
     * @param userEmail The user's new email.
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    /**
     * Creates a new user in the database.
     * <p>
     * This method takes in a user's name, email, and password, and inserts these values into the 'users' table in the database.
     * The fields for the new user are 'user_name', 'user_email', and 'user_password'.
     * The method uses the DatabaseManager's insert method to perform the database operation.
     * After the new user is created, the method prints a message to the console indicating the user's ID.
     * The user's ID is then returned.
     *
     * @param userName     The name of the new user.
     * @param userEmail    The email of the new user.
     * @param userPassword The password of the new user.
     * @return The ID of the new user.
     */
    public static int createUser(String userName, String userEmail, String userPassword) {
        System.out.println("Creating new user " + userName + " / " + userEmail);

        String[] fields = {"user_name", "user_email", "user_password"};
        String[] values = {userName, userEmail, userPassword};
        int userID = DatabaseManager.insert("users", fields, values);
        System.out.println("userID is : " + userID);
        return userID;
    }


    /**
     * Checks if the provided password matches the one stored in the database for the given email.
     * <p>
     * This method first prints a message to the console indicating that a password match is being checked for the given email.
     * It then defines the fields to be retrieved and the conditions for the database selection.
     * The DatabaseManager's select method is called to retrieve the user data from the database.
     * If a match is found, a new User object is created with the retrieved user ID and name, and the provided email, and this new User is returned.
     * If no match is found, or if an exception occurs during the database operation, null is returned.
     *
     * @param userEmail    The email of the user for whom the password match is being checked.
     * @param userPassword The password to be checked.
     * @return The User object if a match is found, null otherwise.
     */
    public static User checkPasswordMatch(String userEmail, String userPassword) {
        System.out.println("Checking password match for " + userEmail);

        String[] fields = {"user_id", "user_name"};
        String[] conditionFields = {"user_email", "user_password"};
        String[] conditionValues = {userEmail, userPassword};

        ResultSet resultSet = DatabaseManager.select("users", fields, conditionFields, conditionValues);

        try {
            if (resultSet.next()) {
                int userID = resultSet.getInt(1);
                String userName = resultSet.getString(2);

                // Print out data from resultSet
                System.out.println("\t> userID " + userID + " / userName " + userName + " / userEmail " + userEmail);

                return new User(userID, userName, userEmail);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        return null;
    }

}
