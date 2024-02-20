package org.serfa.lpdaoo2024;

import java.sql.*;

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

}
