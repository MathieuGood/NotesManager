package org.serfa.lpdaoo2024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.Normalizer;

public class RegisterWindow extends JFrame implements ActionListener {

    JTextField nameField;
    JTextField emailField;
    JTextField password1Field;
    JTextField password2Field;
    JButton registerButton;
    JButton goBackButton;

    RegisterWindow() {

        super("Création d'un compte");

        this.setSize(250, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final Dimension textFieldDimension = new Dimension(150, 20);

        JLabel titleLabel = new JLabel("Création d'un compte");

        JLabel nameLabel = new JLabel("Nom");
        nameField = new JTextField();
        nameField.setPreferredSize(textFieldDimension);

        JLabel emailLabel = new JLabel("E-mail");
        emailField = new JTextField();
        emailField.setPreferredSize(textFieldDimension);

        JLabel password1Label = new JLabel("Mot de passe");
        password1Field = new JPasswordField();
        password1Field.setPreferredSize(textFieldDimension);

        JLabel password2Label = new JLabel("Tapez à nouveau votre mot de passe");
        password2Field = new JPasswordField();
        password1Field.setPreferredSize(textFieldDimension);

        registerButton = new JButton("Créer un compte");
        registerButton.addActionListener(this);
        goBackButton = new JButton("Retour à l'identification");
        goBackButton.addActionListener(this);

        panel.add(titleLabel);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(password1Label);
        panel.add(password1Field);
        panel.add(password2Label);
        panel.add(password2Field);
        panel.add(registerButton);
        panel.add(goBackButton);

        this.add(panel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If user clicks on goBackButton, close RegisterWindow and open LoginWindow
        if (e.getSource() == goBackButton) {
            System.out.println("Go back to LoginWindow");
        } else if (e.getSource() == registerButton) {
            // If user clicks on register Button
            System.out.println("Register button clicked");

            // Get data from text fields and trim leading and trailing spaces
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password1 = password1Field.getText().trim();
            String password2 = password2Field.getText().trim();

            // For debugging
            System.out.println(name);
            System.out.println(FormatChecker.checkNameFormat(name));
            System.out.println(email);
            System.out.println(FormatChecker.checkEmailFormat(email));
            System.out.println(password1);
            System.out.println(FormatChecker.checkPasswordFormat(password1));
            System.out.println(password2);
            System.out.println(FormatChecker.checkIfTwoStringsMatch(password1, password2));

            // Check if name, e-mail and password format are correct
            if (FormatChecker.checkNameFormat(name)
                    && FormatChecker.checkEmailFormat(email)
                    && FormatChecker.checkPasswordFormat(password1)
                    && FormatChecker.checkIfTwoStringsMatch(password1, password2)
            ) {
                // If user entry is correct, create user in database
                System.out.println("User entry correct, sending create query to database");

                // createUserResponse is 0 if OK, 1 if error
                int createUserResponse = DBConnector.createUser(name, email, password1);

                if (createUserResponse == 1) {
                    JOptionPane.showMessageDialog(null, "Compte utilisateur " + email + " créé.");
                } else {
                    JOptionPane.showMessageDialog(null, "L'e-mail choisi est déjà associé à un compte.");
                }
            } else {
                System.out.println("User entry incorrect for registering");
            }

        }
    }
}
