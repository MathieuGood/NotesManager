package fr.serfa.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class RegisterWindow extends Application {


    /**
     * Le stage de l'application.
     */
    private Stage stage;


    /**
     * La scène de l'application.
     */
    private Scene scene;


    /**
     * La racine de l'application.
     */
    private Parent root;


    /**
     * Le champ de saisie pour le nom de l'utilisateur.
     */
    @FXML
    private TextField inputRegisterName;


    /**
     * Le champ de saisie pour l'email de l'utilisateur.
     */
    @FXML
    private TextField inputRegisterEmail;


    /**
     * Le champ de saisie pour le mot de passe de l'utilisateur.
     */
    @FXML
    private PasswordField inputRegisterPassword;


    /**
     * Le champ de saisie pour la confirmation du mot de passe de l'utilisateur.
     */
    @FXML
    private PasswordField inputRegisterConfirmPassword;


    /**
     * La méthode principale de l'application.
     * Elle lance l'application.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Cette méthode est appelée lors du lancement de l'application.
     * Elle charge le fichier FXML de la fenêtre d'inscription et l'affiche.
     *
     * @param primaryStage Le stage principal de l'application.
     * @throws Exception Si une exception se produit lors du chargement du fichier FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("registerWindow.fxml"));

            // Crée une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(fxmlLoader.load());

            // Définit les propriétés du stage principal
            primaryStage.setResizable(false);
            stage.setTitle("NotesManager");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Créer un compte".
     * Elle récupère les informations entrées par l'utilisateur, vérifie leur format et tente de créer un compte utilisateur.
     *
     * @param e L'événement d'action qui a déclenché cette méthode.
     * @throws IOException Si une erreur se produit lors du chargement du fichier FXML.
     */
    public void createAccount(ActionEvent e) throws IOException {

        // Récupère les informations entrées par l'utilisateur
        String userEmail = inputRegisterEmail.getText();
        String userName = inputRegisterName.getText();
        String userPassword = inputRegisterPassword.getText();
        String userConfirmPassword = inputRegisterConfirmPassword.getText();

        System.out.println("Create account button clicked");
        System.out.println("Registering user with the following information :" + userEmail + " " + userName + " " + userPassword + " " + userConfirmPassword);

        // Si le format de tous les champs est correct et que les deux mots de passe entrés correspondent
        if (FormatChecker.checkEmailFormat(userEmail)
                && FormatChecker.checkNameFormat(userName)
                && FormatChecker.checkPasswordFormat(userPassword)
                && userPassword.equals(userConfirmPassword)) {
            // Crée un compte utilisateur dans la base de données et renvoie l'objet User correspondant
            int userID = User.createUser(userName, userEmail, userPassword);

            // Si userID est inférieur ou égal à 0, l'e-mail existe déjà dans la base de données
            if (userID <= 0) {
                String alertContent;
                // Si userID est 0, la base de données a renvoyé une IntegrityConstraintViolation
                if (userID == 0) {
                    alertContent = "Un compte a déjà été enregistré à " + userEmail + ". Connectez-vous avec cette adresse ou spécifiez un autre e-mail pour créer un nouveau compte.";
                } else {
                    alertContent = "Une erreur s'est produite lors de la création du compte, veuillez réessayer.";
                }

                CustomAlert.create(Alert.AlertType.INFORMATION, "Création de compte", "Impossible de créer le compte", alertContent, "show");
            } else {
                // Si userID > 0, l'utilisateur a été créé avec succès
               Alert alert =  CustomAlert.create(Alert.AlertType.INFORMATION, "Création de compte", "Création de compte", "Compte " + userEmail + " créé avec succès", null);

                // Attend que l'utilisateur clique sur le bouton OK pour naviguer vers MainWindow
                if (alert.showAndWait().get() == ButtonType.OK) {
                    MainWindow.setUser(new User(userID, userName, userEmail));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
                    root = loader.load();

                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } else {

            CustomAlert.create(
                    Alert.AlertType.INFORMATION,
                    "Création de compte",
                    "Impossible de créer le compte",
                    """
                    Veuillez vérifier que :
                     - Votre e-mail est correct
                     - Votre nom contient au moins une lettre
                     - Votre mot de passe a au moins 8 caractères dont 1 majuscule, 1 minuscule, 1 caractère spécial et 1 chiffre
                    """,
                    "show");
        }
    }


    /**
     * Cette méthode est appelée lorsque l'utilisateur clique sur le bouton "Retour à la connexion".
     * Elle charge le fichier FXML de la fenêtre de connexion et l'affiche.
     *
     * @param e L'événement d'action qui a déclenché cette méthode.
     * @throws IOException Si une erreur se produit lors du chargement du fichier FXML.
     */
    public void backToLogin(ActionEvent e) throws IOException {
        System.out.println("Back to login");

        // Charge le fichier FXML de la fenêtre de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
        root = loader.load();

        // Récupère le stage à partir de la source de l'événement d'action
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Crée une nouvelle scène avec le FXML chargé
        scene = new Scene(root);

        // Définit la scène sur le stage et affiche le stage
        stage.setScene(scene);
        stage.show();
    }

}