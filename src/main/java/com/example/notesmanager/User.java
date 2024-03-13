package com.example.notesmanager;

import java.sql.*;



public class User {


    // The unique identifier for the User.
    private int userID;

    // The name of the User.
    private String userName;

    // The email of the User.
    private String userEmail;


    
    public User(int userID, String userName, String userEmail) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    
    public int getUserID() {
        return userID;
    }


    
    public String getUserName() {
        return userName;
    }


    
    public String getUserEmail() {
        return userEmail;
    }


    
    public void setUserID(int userID) {
        this.userID = userID;
    }

    
    public void setUserName(String userName) {
        this.userName = userName;
    }


    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    
    public static int createUser(String userName, String userEmail, String userPassword) {
        System.out.println("Creating new user " + userName + " / " + userEmail);

        String[] fields = {"user_name", "user_email", "user_password"};
        String[] values = {userName, userEmail, userPassword};
        int userID = DatabaseManager.insert("users", fields, values);
        System.out.println("userID is : " + userID);
        return userID;
    }


    
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
