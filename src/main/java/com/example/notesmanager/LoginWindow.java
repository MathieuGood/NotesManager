package com.example.notesmanager;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Classe LoginWindow qui étend Application.
 * Cette classe représente la fenêtre de connexion de l'application.
 */
public class LoginWindow extends Application {


    /**
     * Le stage principal pour cette application, sur lequel la scène de l'application peut être définie.
     */
    private Stage stage;


    /**
     * La scène pour cette application. La scène est le conteneur pour le contenu visible dans le graphe de scène JavaFX.
     */
    private Scene scene;


    /**
     * Le nœud racine du graphe de scène. C'est un conteneur intermédiaire pour le graphe de scène complexe.
     */
    private Parent root;


    /**
     * Le TextField où l'utilisateur entre son email pour se connecter. Il est annoté avec @FXML pour que sa valeur puisse être injectée à partir du fichier FXML.
     */
    @FXML
    private TextField inputLoginEmail;


    /**
     * Le PasswordField où l'utilisateur entre son mot de passe pour se connecter. Il est annoté avec @FXML pour que sa valeur puisse être injectée à partir du fichier FXML.
     */
    @FXML
    private PasswordField inputLoginPassword;


    /**
     * La méthode principale pour cette application.
     * Elle imprime un message à la console et lance l'application.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        System.out.println("main login window function");
        launch();
    }


    /**
     * Démarre l'application en définissant le stage principal et en chargeant le fichier FXML de la fenêtre de connexion.
     *
     * @param primaryStage Le stage principal pour cette application.
     * @throws Exception Si une exception se produit lors du chargement du fichier FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Définit le stage principal pour cette application
        this.stage = primaryStage;

        try {
            // Charge le fichier FXML de la fenêtre de connexion
            FXMLLoader fxmlLoader = new FXMLLoader(LoginWindow.class.getResource("loginWindow.fxml"));

            // Crée une nouvelle scène avec le FXML chargé
            Scene scene = new Scene(fxmlLoader.load());

            // Définit les propriétés du stage principal
            primaryStage.setResizable(true);
            stage.setTitle("NotesManager");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // Imprime la trace de la pile si une exception se produit
            e.printStackTrace();
        }
    }


    /**
     * Méthode pour gérer l'action de connexion lorsqu'un utilisateur tente de se connecter.
     * Cette méthode récupère l'email et le mot de passe entrés par l'utilisateur, vérifie leur format,
     * puis vérifie si l'email et le mot de passe correspondent à un utilisateur existant.
     * Si c'est le cas, l'utilisateur est redirigé vers la fenêtre principale.
     * Sinon, une alerte est affichée pour informer l'utilisateur que l'email et le mot de passe ne correspondent pas,
     * ou que le format de l'email et/ou du mot de passe est incorrect.
     *
     * @param e L'événement d'action qui a déclenché cette méthode (c'est-à-dire l'utilisateur qui clique sur le bouton de connexion).
     * @throws IOException Si une erreur se produit lors du chargement de la fenêtre principale.
     */
    public void login(ActionEvent e) throws IOException {
        System.out.println("Login button pressed");

        // Récupère l'email et le mot de passe de l'utilisateur à partir des champs
        String userEmail = inputLoginEmail.getText();
        String userPassword = inputLoginPassword.getText();
        System.out.println("User : " + userEmail + "  / Password : " + userPassword);

        // Vérifie si l'email et le mot de passe sont dans le bon format
        if (FormatChecker.checkEmailFormat(userEmail) && FormatChecker.checkPasswordFormat(userPassword)) {

            // Instancie l'utilisateur et l'attribue à l'objet User si l'email et le mot de passe correspondent
            User user = User.checkPasswordMatch(userEmail, userPassword);

            // Si l'utilisateur est un objet User (l'email et le mot de passe correspondent)
            if (user != null) {
                // Utilisateur statique
                MainWindow.setUser(user);


                // Navigue vers MainWindow
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
                root = loader.load();

                stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("E-mail and password DO NOT match!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Impossible to login");
                alert.setHeaderText("Impossible to login");
                alert.setContentText("E-mail and password do not match");
                alert.show();

            }
        } else {
            System.out.println("Incorrect format for either e-mail or password.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Impossible to login");
            alert.setHeaderText("Impossible to login");
            alert.setContentText("Format of e-mail and/or password is incorrect.");
            alert.show();

        }
    }


    /**
     * Méthode pour gérer l'action d'enregistrement lorsqu'un utilisateur tente de s'inscrire.
     * Cette méthode charge le fichier FXML de la fenêtre d'enregistrement et affiche cette fenêtre.
     *
     * @param e L'événement d'action qui a déclenché cette méthode (c'est-à-dire l'utilisateur qui clique sur le bouton d'enregistrement).
     * @throws IOException Si une erreur se produit lors du chargement de la fenêtre d'enregistrement.
     */
    public void register(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerWindow.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}