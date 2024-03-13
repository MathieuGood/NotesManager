package com.example.notesmanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;


/**
 * La classe DatabaseManager est une classe abstraite qui gère la connexion à la base de données.
 * Elle contient des méthodes pour ouvrir et fermer la connexion, ainsi que pour effectuer des opérations CRUD.
 */
public abstract class DatabaseManager {


    /**
     * Les propriétés de la base de données, chargées à partir d'un fichier de propriétés.
     */
    final static private Properties properties = loadProperties();


    /**
     * L'hôte de la base de données.
     */
    static private String dbHost;


    /**
     * Le numéro de port de la base de données.
     */
    static private String dbPort;


    /**
     * Le nom d'utilisateur utilisé pour se connecter à la base de données.
     */
    static private String dbUsername;


    /**
     * Le mot de passe utilisé pour se connecter à la base de données.
     */
    static private String dbPassword;


    /**
     * Le nom de la base de données.
     */
    static private String dbName;


    /**
     * Charge les propriétés de la base de données à partir d'un fichier de propriétés.
     *
     * @return Les propriétés de la base de données.
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
     * Ouvre une connexion à la base de données.
     *
     * @return La connexion à la base de données.
     * @throws SQLException Si une erreur SQL se produit lors de l'ouverture de la connexion.
     */
    public static Connection openDatabaseConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);
    }

    /**
     * Ferme une connexion à la base de données.
     *
     * @param connection La connexion à fermer.
     * @throws SQLException Si une erreur SQL se produit lors de la fermeture de la connexion.
     */
    public static void closeDatabaseConnection(Connection connection) throws SQLException {
        connection.close();
    }


    /**
     * Exécute une requête SELECT sur la base de données.
     *
     * @param table           Le nom de la table à partir de laquelle sélectionner.
     * @param fields          Les champs à sélectionner.
     * @param conditionFields Les champs de condition pour la clause WHERE.
     * @param conditionValues Les valeurs de condition pour la clause WHERE.
     * @return Un ResultSet contenant les résultats de la requête. Retourne null si une exception SQL se produit.
     */
    public static ResultSet select(String table, String[] fields, String[] conditionFields, String[] conditionValues) {
        System.out.println("Sélection à partir de la table " + table);

        try {
            // Ouvre une connexion à la base de données
            Connection connection = openDatabaseConnection();

            // Crée une chaîne de caractères séparée par des virgules des noms de champs
            String fieldPlaceholders = String.join(", ", fields);

            // Construit la requête SQL SELECT
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT ").append(fieldPlaceholders).append(" FROM ").append(table);

            // Ajoute la clause WHERE si des conditions sont fournies
            if (conditionFields != null && conditionFields.length > 0 && conditionValues != null && conditionValues.length > 0) {
                queryBuilder.append(" WHERE ");
                for (int i = 0; i < Math.min(conditionFields.length, conditionValues.length); i++) {
                    if (i > 0) {
                        queryBuilder.append(" AND ");
                    }
                    queryBuilder.append(conditionFields[i]).append(" = ?");
                }
            }

            // Prépare l'instruction SQL
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            System.out.println("Exécution de la REQUÊTE : " + statement.toString());

            // Définit les valeurs pour les champs de condition dans l'instruction préparée
            if (conditionValues != null && conditionValues.length > 0) {
                for (int i = 0; i < conditionValues.length; i++) {
                    statement.setString(i + 1, conditionValues[i]);
                }
            }

            // Exécute l'instruction et obtient le ResultSet
            ResultSet resultSet = statement.executeQuery();

            // Ferme la connexion à la base de données
            closeDatabaseConnection(connection);

            return resultSet;

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e);
            return null;
        }
    }


    /**
     * Exécute une requête INSERT sur la base de données.
     *
     * @param table  Le nom de la table dans laquelle insérer.
     * @param fields Les champs dans lesquels insérer les valeurs.
     * @param values Les valeurs à insérer.
     * @return L'ID de la ligne insérée. Retourne 0 si une violation de contrainte d'intégrité SQL se produit, -1 si une exception SQL se produit.
     */
    public static int insert(String table, String[] fields, String[] values) {

        try {
            // Ouvre une connexion à la base de données
            Connection connection = openDatabaseConnection();

            // Crée des espaces réservés pour les noms de champs et les valeurs
            String fieldPlaceholders = String.join(", ", fields);
            String valuePlaceholders = String.join(", ", Collections.nCopies(fields.length, "?"));

            // Construit la requête SQL INSERT
            String query = "INSERT INTO " + table + " (" + fieldPlaceholders + ") VALUES (" + valuePlaceholders + ")";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            System.out.println("Exécution de la REQUÊTE : " + statement.toString());

            // Définit les valeurs pour l'instruction préparée
            for (int i = 0; i < values.length; i++) {
                statement.setString(i + 1, values[i]);
            }

            // Exécute l'instruction
            int queryResult = statement.executeUpdate();
            System.out.println("Exécution de la REQUÊTE : " + statement.toString());

            // Obtient le dernier ID inséré
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            int lastInsertedID = rs.getInt(1);

            // Ferme la connexion à la base de données
            closeDatabaseConnection(connection);

            // Affiche un message indiquant la fin du processus d'insertion
            System.out.println("   > Insertion terminée avec le résultat " + queryResult + ". ID de la ligne insérée : " + lastInsertedID);

            // Retourne l'ID de la ligne insérée
            return lastInsertedID;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Violation de contrainte d'intégrité SQL : " + e);
            return 0;

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e);
            return -1;
        }
    }


    /**
     * Exécute une requête UPDATE sur la base de données.
     *
     * @param table          Le nom de la table à mettre à jour.
     * @param field          Le champ à mettre à jour.
     * @param value          La nouvelle valeur pour le champ.
     * @param conditionField Le champ de condition pour la clause WHERE.
     * @param conditionValue La valeur de condition pour la clause WHERE.
     * @return Le nombre de lignes affectées. Retourne 0 si une violation de contrainte d'intégrité SQL se produit, -1 si une exception SQL se produit.
     */
    public static int update(String table, String field, String value, String conditionField, String conditionValue) {

        System.out.println("Mise à jour de la table " + table + " avec " + field + " = " + value + " où " + conditionField + " = " + conditionValue);

        try {
            // Ouvre une connexion à la base de données
            Connection connection = openDatabaseConnection();

            // Construit la requête SQL UPDATE
            String query = "UPDATE " + table + " SET " + field + " = ?" + " WHERE " + conditionField + " = ?";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Définit les valeurs pour l'instruction préparée
            statement.setString(1, value);
            statement.setString(2, conditionValue);

            // Exécute l'instruction et obtient le nombre de lignes affectées
            int queryResult = statement.executeUpdate();
            System.out.println("Exécution de la REQUÊTE : " + statement.toString());

            // Ferme la connexion à la base de données
            closeDatabaseConnection(connection);

            // Affiche un message indiquant la fin du processus de mise à jour
            System.out.println("   > Mise à jour terminée avec le résultat " + queryResult);

            // Retourne le nombre de lignes affectées
            return queryResult;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Violation de contrainte d'intégrité SQL : " + e);
            return 0;

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e);
            return -1;
        }
    }


    /**
     * Exécute une requête DELETE sur la base de données.
     *
     * @param table          Le nom de la table à partir de laquelle supprimer.
     * @param conditionField Le champ de condition pour la clause WHERE.
     * @param conditionValue La valeur de condition pour la clause WHERE.
     * @return Le nombre de lignes affectées. Retourne 0 si une violation de contrainte d'intégrité SQL se produit, -1 si une exception SQL se produit.
     */
    public static int delete(String table, String conditionField, String conditionValue) {

        System.out.println("Suppression de la table " + table + " où " + conditionField + " = " + conditionValue);

        try {
            // Ouvre une connexion à la base de données
            Connection connection = openDatabaseConnection();

            // Construit la requête SQL DELETE
            String query = "DELETE FROM " + table + " WHERE " + conditionField + " = ?";

            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            // Définit les valeurs pour l'instruction préparée
            statement.setString(1, conditionValue);

            // Exécute l'instruction et obtient le nombre de lignes affectées
            int queryResult = statement.executeUpdate();
            System.out.println("Exécution de la REQUÊTE : " + statement.toString());

            // Ferme la connexion à la base de données
            closeDatabaseConnection(connection);

            // Affiche un message indiquant la fin du processus de suppression
            System.out.println("   > Suppression terminée avec le résultat " + queryResult);

            // Retourne le nombre de lignes affectées
            return queryResult;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Violation de contrainte d'intégrité SQL : " + e);
            return 0;

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e);
            return -1;
        }
    }


    /**
     * Exécute une procédure stockée sur la base de données.
     *
     * @param procedureName Le nom de la procédure stockée à exécuter.
     * @param inParameters  Les paramètres d'entrée pour la procédure stockée.
     * @param outParameter  Le paramètre de sortie de la procédure stockée.
     * @return La valeur du paramètre de sortie de la procédure stockée. Retourne -1 si une exception SQL se produit.
     */
    public static int call(String procedureName, String[] inParameters, String outParameter) {

        System.out.println("Appel de " + procedureName + " avec les paramètres " + Arrays.toString(inParameters));

        try {
            // Ouvre une connexion à la base de données
            Connection connection = openDatabaseConnection();

            // Construit la requête SQL CALL
            String query = "{CALL " + procedureName + "(" + String.join(", ", Collections.nCopies(inParameters.length + 1, "?")) + ")}";
            System.out.println("Chaîne de requête avant l'instruction préparée : " + query);
            CallableStatement statement = connection.prepareCall(query);

            // Définit les valeurs pour l'instruction préparée
            for (int i = 0; i < inParameters.length; i++) {
                statement.setString(i + 1, inParameters[i]);
            }

            // Enregistre le paramètre OUT
            statement.registerOutParameter(inParameters.length + 1, Types.INTEGER);

            // Exécute l'instruction
            statement.execute();

            // Récupère la valeur du paramètre OUT
            int success = statement.getInt(inParameters.length + 1);

            // Ferme l'instruction et la connexion à la base de données
            statement.close();
            closeDatabaseConnection(connection);

            // Affiche un message indiquant la fin de l'appel de la procédure
            System.out.println("Procédure appelée et retournée " + success);

            return success;

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e);
            return -1;
        }
    }

}
