package fr.serfa.notesmanager;

import java.sql.*;

/**
 * Cette classe représente un utilisateur dans le gestionnaire de notes.
 * Un utilisateur est identifié par son identifiant unique, son nom et son email.
 */
public class User {


    /**
     * L'identifiant unique de l'utilisateur.
     */
    private int userID;


    /**
     * Le nom de l'utilisateur.
     */
    private String userName;


    /**
     * L'email de l'utilisateur.
     */
    private String userEmail;


    /**
     * Constructeur de la classe User.
     * Il initialise un nouvel utilisateur avec les informations fournies.
     *
     * @param userID    L'identifiant unique de l'utilisateur.
     * @param userName  Le nom de l'utilisateur.
     * @param userEmail L'email de l'utilisateur.
     */
    public User(int userID, String userName, String userEmail) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
    }


    /**
     * Récupère l'identifiant unique de cet utilisateur.
     *
     * @return L'identifiant unique de cet utilisateur.
     */
    public int getUserID() {
        return userID;
    }


    /**
     * Récupère le nom de cet utilisateur.
     *
     * @return Le nom de cet utilisateur.
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Crée un nouvel utilisateur avec le nom, l'email et le mot de passe spécifiés, et l'ajoute à la base de données.
     *
     * @param userName     Le nom du nouvel utilisateur.
     * @param userEmail    L'email du nouvel utilisateur.
     * @param userPassword Le mot de passe du nouvel utilisateur.
     * @return L'identifiant unique du nouvel utilisateur.
     */
    public static int createUser(String userName, String userEmail, String userPassword) {
        System.out.println("Creating new user " + userName + " / " + userEmail);

        String[] fields = {"user_name", "user_email", "user_password"};
        String[] values = {userName, userEmail, userPassword};
        int userID = DatabaseManager.insert("users", fields, values);
        System.out.println("userID is : " + userID);
        return userID;
    }


    /**
     * Vérifie si le mot de passe fourni correspond à l'email de l'utilisateur dans la base de données.
     *
     * @param userEmail    L'email de l'utilisateur.
     * @param userPassword Le mot de passe à vérifier.
     * @return Un objet User si le mot de passe correspond, null sinon.
     */
    public static User checkPasswordMatch(String userEmail, String userPassword) {
        System.out.println("Checking password match for " + userEmail);

        String[] fields = {"user_id", "user_name"};
        String[] conditionFields = {"user_email", "user_password"};
        String[] conditionValues = {userEmail, userPassword};

        ResultSet resultSet = DatabaseManager.select("users", fields, conditionFields, conditionValues);

        try {
            if (resultSet.next()) {
                int userID = resultSet.getInt(1);
                String userName = resultSet.getString(2);

                System.out.println("\t> userID " + userID + " / userName " + userName + " / userEmail " + userEmail);

                return new User(userID, userName, userEmail);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e);
        }
        return null;
    }

}
