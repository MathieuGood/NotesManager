package org.serfa.lpdaoo2024;

import java.sql.*;

public class Notebook{

    private final int userID;

    public Notebook(int userID) {
        this.userID = userID;
    }


    public int getUserID() {
        return this.userID;
    }

    public void getContentTree() {

        try {
            Connection connection = DatabaseManager.openDatabaseConnection();
            System.out.println("Getting Notebook Content Tree for userID " + userID);

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






}
