package org.serfa.lpdaoo2024;

import java.sql.*;

public class Binder {

    private final int binderID;
    private final int userID;
    private String binderName;
    private int binderColorID;


    public Binder(Notebook notebook, int binderID, String binderName, int binderColorID) {
        this.binderID = binderID;
        this.userID = notebook.getUserID();
    }


    public int getBinderID() {
        return this.binderID;
    }


    public int getUserID() {
        return this.userID;
    }


    public String getBinderName() {
        return this.binderName;
    }


    public int getBinderColorID() {
        return this.binderColorID;
    }


    private int updateAttribute(String attributeToUpdate, String newValue) {
        try {
            System.out.println("\n***");
            System.out.println("BINDER updateAttribute() : " + attributeToUpdate + " / newValue " + newValue);

            Connection connection = DatabaseManager.openDatabaseConnection();

            PreparedStatement statement = connection.prepareStatement("""
                                UPDATE binders SET ? = ?
                                ;
                            """,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, binderName);
            statement.setString(2, newValue);

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            System.out.println("Number of rows inserted : " + rowsInserted);

            // Get last inserted ID
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);

            DatabaseManager.closeDatabaseConnection(connection);

            System.out.println("Binder " + binderName + " created with ID #" + lastInsertedID);

            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }

    public int editName(String newName) {
        try {
            System.out.println("\n***");
//            System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + colorID);

            Connection connection = DatabaseManager.openDatabaseConnection();

            PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO binders (binder_name, user_id, binder_color_id)
                                VALUES (?, ?, ?);
                            """,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, binderName);
            statement.setInt(2, userID);
//            statement.setInt(3, colorID);

            // Execute the statement
            int rowsInserted = statement.executeUpdate();
            System.out.println("Number of rows inserted : " + rowsInserted);

            // Get last inserted ID
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);

            DatabaseManager.closeDatabaseConnection(connection);

            System.out.println("Binder " + binderName + " created with ID #" + lastInsertedID);

            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Entry already exists
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            // Other error
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }



}
