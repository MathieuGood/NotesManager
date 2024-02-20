package org.serfa.lpdaoo2024;

import java.sql.*;

public abstract class DBConnector {

    // Credentials for database connection
    // TO DO : move credentials to a separate file for security
    final static private String dbHost = "51.91.98.35";
    final static private String dbPort = "3306";
    final static private String dbUsername = "notesmanager";
    final static private String dbPassword = "notesserfa2024";
    final static private String dbName = "NotesManager";



    // Open database connection
    public static Connection openDatabaseConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);
        System.out.println("Connection validity to " + dbName + " : " + connection.isValid(5));
        return connection;
    }


    // Close database connection
    public static void closeDatabaseConnection(Connection connection) throws SQLException {
        System.out.println("Closing connection to " + dbName);
        connection.close();
    }


}
