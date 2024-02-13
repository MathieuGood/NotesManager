package org.serfa.lpdaoo2024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.Normalizer;

public class RegisterWindow extends JFrame implements ActionListener, FocusListener {
    JTextField nameField;
    JTextField emailField;
    JTextField password1Field;
    JTextField password2Field;
    JButton registerButton;
    JButton goBackButton;

    private final String namePlaceholder = "Nom";
    private final String emailPlaceholder = "E-mail";
    private final String password1Placeholder = "Mot de passe";
    private final String password2Placeholder = "Confirmation mot de passe";
    private final Color defaultTextColor = UIManager.getColor("TextField.foreground");
    private final Font defaultTextFont = UIManager.getFont("TextField.font");

    RegisterWindow() {

        super("NotesManager");

        this.setSize(320, 300);


        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Créer un compte");
        titleLabel.setBounds(30, 15, 260, 20);
        titleLabel.setFont(new Font("Jetbrains Mono", Font.BOLD, 15));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);


        nameField = new JTextField(namePlaceholder);
        nameField.setBounds(65, 45, 190, 28);
        nameField.setForeground(Color.GRAY);
        nameField.addFocusListener(this);

        emailField = new JTextField(emailPlaceholder);
        emailField.setBounds(65, 85, 190, 28);
        emailField.setForeground(Color.GRAY);
        emailField.addFocusListener(this);

        password1Field = new JPasswordField(password1Placeholder);
        password1Field.setBounds(65, 120, 190, 28);
        password1Field.setForeground(Color.GRAY);
        password1Field.addFocusListener(this);

        password2Field = new JPasswordField(password2Placeholder);
        password2Field.setBounds(65, 155, 190, 28);
        password2Field.setForeground(Color.GRAY);
        password2Field.addFocusListener(this);


        registerButton = new JButton("Créer un compte");
        registerButton.addActionListener(this);
        registerButton.setBounds(80, 195, 160, 30);

        goBackButton = new JButton("Retour");
        goBackButton.addActionListener(this);
        goBackButton.setBounds(80, 235, 160, 30);


        panel.add(titleLabel);
        panel.add(nameField);
        panel.add(emailField);
        panel.add(password1Field);
        panel.add(password2Field);
        panel.add(registerButton);
        panel.add(goBackButton);

        // Add panel to RegisterWindow frame
        this.add(panel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);

        // Set focus on titleLabel so no TextField gets focus and placeholder appears when frame loads
        titleLabel.grabFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If user clicks on goBackButton, close RegisterWindow and open LoginWindow
        if (e.getSource() == goBackButton) {
            System.out.println("Go back to LoginWindow");
            this.dispose();
            LoginWindow loginWindow = new LoginWindow();

        } else if (e.getSource() == registerButton) {
            // If user clicks on register Button
            System.out.println("Register button clicked");

            // Get data from text fields and trim leading and trailing spaces
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password1 = password1Field.getText().trim();
            String password2 = password2Field.getText().trim();

            // For debugging
            System.out.println(name + " " + FormatChecker.checkNameFormat(name));
            System.out.println(email + " " + FormatChecker.checkEmailFormat(email));
            System.out.println(password1 + " " + FormatChecker.checkPasswordFormat(password1));
            System.out.println(password2 + " " + FormatChecker.checkIfTwoStringsMatch(password1, password2));

            // Check if name, e-mail and password format are correct
            if (FormatChecker.checkNameFormat(name)
                    && FormatChecker.checkEmailFormat(email)
                    && FormatChecker.checkPasswordFormat(password1)
                    && FormatChecker.checkIfTwoStringsMatch(password1, password2)) {

                // If user entry is correct, create user in database
                System.out.println("User entry correct, sending create query to database");

                // createUserResponse is 0 if user has been created, 1 if e-mail already exists in database
                int createUserResponse = DBConnector.createUser(name, email, password1);

                if (createUserResponse == 1) {
                    JOptionPane.showMessageDialog(null, "Compte utilisateur " + email + " créé.");
                } else {
                    JOptionPane.showMessageDialog(null, "L'e-mail choisi est déjà associé à un compte.");
                }
            } else {
                System.out.println("User entry incorrect for registering");
                JOptionPane.showMessageDialog(null, "Les informations saisies sont incorrectes.\nVeuillez saisir un mot de passe de 8 caractères minimum avec au moins une majuscule, une minuscule, un chiffre et un caractère spécial.");
            }

        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == nameField) {
            if (nameField.getText().trim().equals(namePlaceholder)) {
                nameField.setText("");
                nameField.setForeground(defaultTextColor);
            }
        } else if (e.getSource() == emailField) {
            if (emailField.getText().trim().equals(emailPlaceholder)) {
                emailField.setText("");
                emailField.setForeground(defaultTextColor);
            }
        } else if (e.getSource() == password1Field) {
            if (password1Field.getText().trim().equals(password1Placeholder)) {
                password1Field.setText("");
                password1Field.setForeground(defaultTextColor);
            }
        } else if (e.getSource() == password2Field) {
            if (password2Field.getText().trim().equals(password2Placeholder)) {
                password2Field.setText("");
                password2Field.setForeground(defaultTextColor);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == nameField) {
            if (nameField.getText().trim().isEmpty()) {
                nameField.setText(namePlaceholder);
                nameField.setForeground(Color.GRAY);
            }
        } else if (e.getSource() == emailField) {
            if (emailField.getText().trim().isEmpty()) {
                emailField.setText(emailPlaceholder);
                emailField.setForeground(Color.GRAY);
            }
        } else if (e.getSource() == password1Field) {
            if (password1Field.getText().trim().isEmpty()) {
                password1Field.setText(password1Placeholder);
                password1Field.setForeground(Color.GRAY);
            }
        } else if (e.getSource() == password2Field) {
            if (password2Field.getText().trim().isEmpty()) {
                password2Field.setText(password2Placeholder);
                password2Field.setForeground(Color.GRAY);
            }
        }
    }

}