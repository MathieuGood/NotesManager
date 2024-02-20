package org.serfa.lpdaoo2024;

import java.sql.*;

public class Binder {

    private final int binderID;
    private final int userID;
//    private final int binderColorID;


    public Binder(Notebook notebook, int binderID) {
        this.binderID = binderID;
        this.userID = notebook.getUserID();

    }


    public int createBinder(String binderName, int colorID) {

        try {
            Connection connection = DatabaseManager.openDatabaseConnection();
            System.out.println("Inserting Binder : " + binderName + " / userID " + userID + " / colorID " + colorID);

            PreparedStatement statement = connection.prepareStatement("""
                                INSERT INTO binders (binder_name, user_id, binder_color_id)
                                VALUES (?, ?, ?);
                            """,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, binderName);
            statement.setInt(2, userID);
            statement.setInt(3, colorID);

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
