package com.example.notesmanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public abstract class DatabaseManager {


    
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


    
    public static Connection openDatabaseConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);
    }


    
    public static void closeDatabaseConnection(Connection connection) throws SQLException {
        connection.close();
    }


    
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


    
    public static int delete(String table, String conditionField, String conditionValue) {
        // Print a message indicating the start of the deletion process
        System.out.println("Deleting from table " + table + " where " + conditionField + " = " + conditionValue);

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Construct the SQL DELETE query
            String query = "DELETE FROM " + table + " WHERE " + conditionField + " = ?";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Set values for the prepared statement
            statement.setString(1, conditionValue);

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


    public static int call(String procedureName, String[] inParameters, String outParameter) {
        // Print a message indicating the start of the procedure call
        System.out.println("Calling " + procedureName + " with parameters " + Arrays.toString(inParameters));

        try {
            // Open a connection to the database
            Connection connection = openDatabaseConnection();

            // Construct the SQL CALL query
            String query = "{CALL " + procedureName + "(" + String.join(", ", Collections.nCopies(inParameters.length + 1, "?")) + ")}";
            System.out.println("Query string before prepared statement : " + query);
            CallableStatement statement = connection.prepareCall(query);

            // Set values for the prepared statement
            for (int i = 0; i < inParameters.length; i++) {
                statement.setString(i + 1, inParameters[i]);
            }

            // Register the OUT parameter
            statement.registerOutParameter(inParameters.length + 1, Types.INTEGER);

            // Execute the statement
            statement.execute();

            // Retrieve the value of the OUT parameter
            int success = statement.getInt(inParameters.length + 1);

            // Close the statement and the database connection
            statement.close();
            closeDatabaseConnection(connection);

            // Print a message indicating the end of the procedure call
            System.out.println("Procedure called and returned " + success);

            // Return the result
            return success;
        } catch (SQLException e) {
            // Print an error message if a SQL exception occurs
            System.out.println("SQL Error : " + e);
            return -1;
        }
    }


}
