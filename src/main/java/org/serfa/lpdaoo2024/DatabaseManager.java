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
     * The method then returns the connection object.
     *
     * @return A Connection object representing the open database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection openDatabaseConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);
    }


    /**
     * This method closes the connection to the database.
     *
     * @param connection The Connection object representing the open database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static void closeDatabaseConnection(Connection connection) throws SQLException {
        connection.close();
    }


    /**
     * This method is used to select data from a table in the database.
     * It constructs a SQL SELECT query using the provided table name, field names, condition field and condition value.
     * It then executes this query and returns the result set.
     *
     * @param table          The name of the table to select from.
     * @param fields         An array of field names to select.
     * @param conditionField The field name to use in the WHERE clause.
     * @param conditionValue The value to use in the WHERE clause.
     * @return A ResultSet object containing the result of the query.
     */
    public static ResultSet select(String table, String[] fields, String conditionField, String conditionValue) {
        // Print a message indicating the start of the selection process
        System.out.println("Selecting from table " + table);

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Create a comma-separated string of field names
            String fieldPlaceholders = String.join(", ", fields);

            // Construct the SQL SELECT query
            String query = "SELECT " + fieldPlaceholders + " FROM " + table + " WHERE " + conditionField + " = ?";

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(query);

            // Set the value for the condition field in the prepared statement
            statement.setString(1, conditionValue);

            // Execute the statement and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the selection process
            System.out.println("Select done");

            // Return the result set
            return resultSet;

        } catch (SQLException e) {
            // Print an error message if a SQL exception occurs
            System.out.println("SQL Error : " + e);
            return null;
        }
    }


    /**
     * This method is used to insert data into a table in the database.
     * It constructs a SQL INSERT query using the provided table name, field names, and values.
     * It then executes this query and returns the ID of the inserted row.
     *
     * @param table  The name of the table to insert into.
     * @param fields An array of field names to insert.
     * @param values An array of values corresponding to the field names to insert.
     * @return The ID of the inserted row.
     */
    public static int insert(String table, String[] fields, String[] values) {
        // Print a message indicating the start of the insertion process
        System.out.println("Inserting into table " + table);

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Create placeholders for field names and values
            String fieldPlaceholders = String.join(", ", fields);
            String valuePlaceholders = String.join(", ", Collections.nCopies(fields.length, "?"));

            // Construct the SQL INSERT query
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

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the insertion process
            System.out.println("Insert done with result " + queryResult + ". ID of inserted row : " + lastInsertedID);

            // Return the ID of the inserted row
            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Print an error message if a SQL integrity constraint violation occurs
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            // Print an error message if a SQL exception occurs
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }


    /**
     * This method is used to update a record in a table in the database.
     * It constructs a SQL UPDATE query using the provided table name, field name, value, condition field and condition value.
     * It then executes this query and returns the number of rows affected.
     *
     * @param table          The name of the table to update.
     * @param field          The field name to update.
     * @param value          The new value for the field.
     * @param conditionField The field name to use in the WHERE clause.
     * @param conditionValue The value to use in the WHERE clause.
     * @return The number of rows affected by the update.
     */
    public static int update(String table, String field, String value, String conditionField, String conditionValue) {
        // Print a message indicating the start of the update process
        System.out.println("Updating table " + table + " with " + field + " = " + value + " where " + conditionField + " = " + conditionValue);

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Construct the SQL UPDATE query
            String query = "UPDATE " + table + " SET " + field + " = ?" + " WHERE " + conditionField + " = ?";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set values for the prepared statement
            statement.setString(1, value);
            statement.setString(2, conditionValue);

            System.out.println(statement);

            // Execute the statement and get the number of rows affected
            int queryResult = statement.executeUpdate();
            System.out.println("Number of rows updated : " + queryResult);

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the update process
            System.out.println("Update done with result " + queryResult);

            // Return the number of rows affected
            return queryResult;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Print an error message if a SQL integrity constraint violation occurs
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            // Print an error message if a SQL exception occurs
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }


    /**
     * This method is used to delete a record from a table in the database.
     * It constructs a SQL DELETE query using the provided table name, condition field and condition value.
     * It then executes this query and returns the number of rows affected.
     *
     * @param table          The name of the table to delete from.
     * @param conditionField The field name to use in the WHERE clause.
     * @param conditionValue The value to use in the WHERE clause.
     * @return The number of rows affected by the delete.
     */
    public static int delete(String table, String conditionField, String conditionValue) {
        // Print a message indicating the start of the deletion process
        System.out.println("Deleting from table " + table + " where " + conditionField + " = " + conditionValue);

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Construct the SQL DELETE query
            String query = "DELETE FROM " + table + " WHERE ? = ?";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set values for the prepared statement
            statement.setString(1, conditionField);
            statement.setString(2, conditionValue);

            // Execute the statement and get the number of rows affected
            int queryResult = statement.executeUpdate();
            System.out.println("Number of rows deleted : " + queryResult);

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the deletion process
            System.out.println("Delete done with result " + queryResult);

            // Return the number of rows affected
            return queryResult;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Print an error message if a SQL integrity constraint violation occurs
            System.out.println("SQL Integrity Constraint Violation : " + e);
            return 0;

        } catch (SQLException e) {
            // Print an error message if a SQL exception occurs
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }


}
