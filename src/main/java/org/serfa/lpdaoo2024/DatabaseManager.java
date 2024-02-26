package org.serfa.lpdaoo2024;

import java.sql.*;
import java.util.Collections;

public abstract class DatabaseManager {

    // Credentials for database connection
    // TO DO : move credentials to a separate file for security
    final static private String dbHost = "51.91.98.35";
    final static private String dbPort = "3306";
    final static private String dbUsername = "notesmanager";
    final static private String dbPassword = "notesserfa2024";
    final static private String dbName = "NotesManager";


    /**
     * This method opens a connection to the database using the provided database credentials.
     * It uses the DriverManager to get a connection to the MariaDB database at the specified host and port.
     * The method then checks the validity of the connection by calling the isValid method on the connection object.
     * If the connection is valid, it prints a message indicating the validity and the name of the database.
     * The method then returns the connection object.
     *
     * @return A Connection object representing the open database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection openDatabaseConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);
        System.out.println("Connection validity to " + dbName + " : " + connection.isValid(5));
        return connection;
    }


    /**
     * This method closes the connection to the database.
     * It prints a message indicating that the connection to the database is being closed.
     * Then, it calls the close method on the provided Connection object.
     *
     * @param connection The Connection object representing the open database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static void closeDatabaseConnection(Connection connection) throws SQLException {
        System.out.println("Closing connection to " + dbName);
        connection.close();
    }


    public static int insert(String table, String[] fields, String[] values) {
        System.out.println("Inserting into table " + table);

        try {
            Connection connection = openDatabaseConnection();

            // Create placeholders for field names and values
            String fieldPlaceholders = String.join(", ", fields);
            String valuePlaceholders = String.join(", ", Collections.nCopies(fields.length, "?"));

            // Construct the SQL query string
            String query = "INSERT INTO " + table + " (" + fieldPlaceholders + ") VALUES (" + valuePlaceholders + ")";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set values for the prepared statement
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }

            // Execute the statement
            int queryResult = statement.executeUpdate();
            System.out.println("Number of rows inserted : " + queryResult);

            // Get last inserted ID
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);

            closeDatabaseConnection(connection);

            System.out.println("Insert done with result " + queryResult + ". ID of inserted row : " + lastInsertedID);

            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }


    // On the same model as insert() method, create a method to update a row in a table
    public static int update(String table, String[] fields, String[] values, String condition) {
        System.out.println("Updating table " + table);

        try {
            Connection connection = openDatabaseConnection();

            // Create placeholders for field names and values
            String fieldPlaceholders = String.join(" = ?, ", fields) + " = ?";
            String valuePlaceholders = String.join(", ", values);

            // Construct the SQL query string
            String query = "UPDATE " + table + " SET " + fieldPlaceholders + " WHERE " + condition;

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set values for the prepared statement
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }

            // Execute the statement
            int queryResult = statement.executeUpdate();
            System.out.println("Number of rows updated : " + queryResult);

            // Get last inserted ID
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);

            closeDatabaseConnection(connection);

            System.out.println("Update done with result " + queryResult + ". ID of updated row : " + lastInsertedID);

            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }

}
