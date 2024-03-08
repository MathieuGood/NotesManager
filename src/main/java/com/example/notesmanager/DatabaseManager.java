package com.example.notesmanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * The DatabaseManager class provides methods for interacting with a database.
 * It contains methods to open and close a database connection, as well as to perform select, insert, update, and delete operations.
 * This class is abstract and cannot be instantiated.
 */
public abstract class DatabaseManager {


    /**
     * A Properties object that holds the properties loaded from the properties file.
     * The properties file contains the database credentials such as host, port, username, password, and database name.
     * These properties are loaded using the loadProperties() method.
     */
    final static private Properties properties = loadProperties();

    // The host of the database.
    static private String dbHost;

    // The port number of the database.
    static private String dbPort;

    // The username used to connect to the database.
    static private String dbUsername;

    // The password used to connect to the database.
    static private String dbPassword;

    // The name of the database.
    static private String dbName;


    /**
     * This method is used to load the properties from a properties file and set the database credentials.
     * It creates a new Properties object and attempts to load the properties from a file named "database.properties" located in the project directory.
     * If the properties file is found and can be read, the properties are loaded into the Properties object and the database credentials are set.
     * If an IOException occurs during this process, an error message is printed to the console.
     * The method then returns the Properties object, whether or not the properties were successfully loaded.
     *
     * @return A Properties object containing the properties loaded from the properties file, or an empty Properties object if the properties could not be loaded.
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/main/java/com/example/notesmanager/database.properties")) {
            properties.load(fileInputStream);
            dbHost = properties.getProperty("dbHost");
            dbPort = properties.getProperty("dbPort");
            dbUsername = properties.getProperty("dbUsername");
            dbPassword = properties.getProperty("dbPassword");
            dbName = properties.getProperty("dbName");
        } catch (IOException e) {
            System.out.println("Error : " + e);
        }
        return properties;
    }


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
     * It constructs a SQL SELECT query using the provided table name, field names, condition fields and condition values.
     * It then executes this query and returns the result set.
     *
     * @param table           The name of the table to select from.
     * @param fields          An array of field names to select.
     * @param conditionFields An array of field names to use in the WHERE clause.
     * @param conditionValues An array of values corresponding to the condition fields to use in the WHERE clause.
     * @return A ResultSet object containing the result of the select operation, or null if a SQL exception occurs.
     */
    public static ResultSet select(String table, String[] fields, String[] conditionFields, String[] conditionValues) {
        // Print a message indicating the start of the selection process
        System.out.println("Selecting from table " + table);

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Create a comma-separated string of field names
            String fieldPlaceholders = String.join(", ", fields);

            // Construct the SQL SELECT query
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ").append(fieldPlaceholders).append(" FROM ").append(table);

            // Append WHERE clause if conditions are provided
            if (conditionFields != null && conditionFields.length > 0 && conditionValues != null && conditionValues.length > 0) {
                queryBuilder.append(" WHERE ");
                for (int i = 0; i < Math.min(conditionFields.length, conditionValues.length); i++) {
                    if (i > 0) {
                        queryBuilder.append(" AND ");
                    }
                    queryBuilder.append(conditionFields[i]).append(" = ?");
                }
            }

            // Prepare the SQL statement
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            System.out.println("Executing QUERY : " + statement.toString());

            // Set the values for the condition fields in the prepared statement
            if (conditionValues != null && conditionValues.length > 0) {
                for (int i = 0; i < conditionValues.length; i++) {
                    statement.setString(i + 1, conditionValues[i]);
                }
            }

            // Execute the statement and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Close the database connection
            closeDatabaseConnection(connection);

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

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Create placeholders for field names and values
            String fieldPlaceholders = String.join(", ", fields);
            String valuePlaceholders = String.join(", ", Collections.nCopies(fields.length, "?"));

            // Construct the SQL INSERT query
            String query = "INSERT INTO " + table + " (" + fieldPlaceholders + ") VALUES (" + valuePlaceholders + ")";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Executing QUERY : " + statement.toString());

            // Set values for the prepared statement
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }

            // Execute the statement
            int queryResult = statement.executeUpdate();
            System.out.println("Executing QUERY : " + statement.toString());

            // Get last inserted ID
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the insertion process
            System.out.println("   > Insert done with result " + queryResult + ". ID of inserted row : " + lastInsertedID);

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

            // Execute the statement and get the number of rows affected
            int queryResult = statement.executeUpdate();
            System.out.println("Executing QUERY : " + statement.toString());

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the update process
            System.out.println("   > Update done with result " + queryResult);

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
            System.out.println("Executing QUERY : " + statement.toString());

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the deletion process
            System.out.println("   > Delete done with result " + queryResult);

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


    public static int call(String procedureName, String[] parameters) {
        // Print a message indicating the start of the deletion process
        System.out.println("Calling " + procedureName + " with parameters " + Arrays.toString(parameters));

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Construct the SQL DELETE query
            String query = "CALL " + procedureName + "(" + String.join(", ", Collections.nCopies(parameters.length, "?")) + ")";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set values for the prepared statement
            for (int i = 0; i < parameters.length; i++) {
                statement.setString(i + 1, parameters[i]);
            }

            // Execute the statement and get the number of rows affected
            int queryResult = statement.executeUpdate();
            System.out.println("Executing QUERY : " + statement.toString());

            // Close the database connection
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the deletion process
            System.out.println("   > Procedure called and returned " + queryResult);

            // Return the result (1 if success, 0 if failure)
            return queryResult;

        } catch (SQLException e) {
            // Print an error message if a SQL exception occurs
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }

}
