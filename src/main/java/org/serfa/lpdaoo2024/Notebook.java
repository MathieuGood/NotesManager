package org.serfa.lpdaoo2024;

import java.sql.*;
import java.util.ArrayList;

public class Notebook {

    private final int userID;

    public Notebook(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void getContentTree() {

        try {
            Connection connection = DatabaseManager.openDatabaseConnection();
            System.out.println("\n***");
            System.out.println("getContentTree() for userID " + userID);

            PreparedStatement statement = connection.prepareStatement("""
                                SELECT
                                	binders.binder_id,
                                    binders.binder_name,
                                    binders.binder_color_id,
                                    tabs.tab_id,
                                    tabs.tab_name,
                                    tabs.tab_color_id,
                                    notes.note_id,
                                    notes.note_name,
                                    notes.note_color_id
                                    FROM binders
                                        INNER JOIN tabs
                                            ON binders.binder_id = tabs.binder_id
                                        INNER JOIN notes
                                            ON tabs.tab_id = notes.tab_id
                                
                                WHERE user_id = ?;
                            """,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // Print out data from resultSet
                int binderID = resultSet.getInt(1);
                String binderName = resultSet.getString(2);
                int binderColorID = resultSet.getInt(3);
                int tabID = resultSet.getInt(4);
                String tabName = resultSet.getString(5);
                int tabColorID = resultSet.getInt(6);
                int noteID = resultSet.getInt(7);
                String noteName = resultSet.getString(8);
                int noteColorID = resultSet.getInt(9);
                System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID + " / " + tabID + " / " + tabName + " / " + tabColorID + " / " + noteID + " / " + noteName + " / " + noteColorID);
            }

            DatabaseManager.closeDatabaseConnection(connection);

        } catch (SQLIntegrityConstraintViolationException e) {
            // Entry already exists
            System.out.println("SQL Integrity Constraint Violation : " + e);

        } catch (SQLException e) {
            // Other error
            System.out.println("SQL Error : " + e);

        }
    }


    public ArrayList<Binder> getAllBinders() {

        try {
            Connection connection = DatabaseManager.openDatabaseConnection();
            System.out.println("\n***");
            System.out.println("getAllBinders() for userID " + this.userID + " :");

            PreparedStatement statement = connection.prepareStatement("""
                                SELECT
                                	binders.binder_id,
                                    binders.binder_name,
                                    binders.binder_color_id
                                FROM binders
                                WHERE user_id = ?;
                            """,
                    Statement.RETURN_GENERATED_KEYS);

            // Add userID to query
            statement.setInt(1, userID);

            // Execute query and store results in ResultSet
            ResultSet resultSet = statement.executeQuery();


            // Create ArrayList to store all Binder objects
            ArrayList<Binder> binders = new ArrayList<>();

            // Parse query results to new Binder object and store it into ArrayList
            while (resultSet.next()) {
                // Print out data from resultSet
                int binderID = resultSet.getInt(1);
                String binderName = resultSet.getString(2);
                int binderColorID = resultSet.getInt(3);
                System.out.println("\t> " + binderID + " / " + binderName + " / " + binderColorID);
                binders.add(new Binder(this, binderID, binderName, binderColorID));
            }

            DatabaseManager.closeDatabaseConnection(connection);

            return binders;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Entry already exists
            System.out.println("SQL Integrity Constraint Violation : " + e);

        } catch (SQLException e) {
            // Other error
            System.out.println("SQL Error : " + e);

        }
        return null;
    }


    public int createBinder(String binderName, int binderColorID) {
        System.out.println("\n***");
        System.out.println("createBinder() : " + binderName + " / userID " + userID + " / colorID " + binderColorID);

        String[] fields = {"binder_name", "user_id", "binder_color_id"};
        String[] values = {binderName, String.valueOf(userID), String.valueOf(binderColorID)};

        return DatabaseManager.insert("binders", fields, values);
    }


    public int deleteBinder(int binderID) {

        try {
            System.out.println("\n***");
            System.out.println("deleteBinder() : " + " binderID " + binderID);

            Connection connection = DatabaseManager.openDatabaseConnection();

            // Delete binder and all associated children (tabs and notes) from database
            // userID is used to prevent deletion of other user's data

            PreparedStatement statement = connection.prepareStatement("""
                        DELETE FROM binders
                        WHERE binder_id = ?
                        AND user_id = ?;
                        ;
                    """);

            statement.setInt(1, binderID);
            statement.setInt(2, userID);

            // Execute the statement
            int rowsDeleted = statement.executeUpdate();
            System.out.println("Rows deleted : " + rowsDeleted);

            DatabaseManager.closeDatabaseConnection(connection);
            return rowsDeleted;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Cannot delete because of foreign key constraint
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            // Other error
            System.out.println("SQL Error : " + e);
            return -1;
        }

    }


}
