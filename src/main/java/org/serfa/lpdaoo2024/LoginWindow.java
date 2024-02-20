package org.serfa.lpdaoo2024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginWindow extends JFrame implements ActionListener, FocusListener {

    JButton loginButton;
    JButton registerButton;
    JTextField emailField;
    JTextField passwordField;
    private final String emailFieldPlaceholder = "Votre e-mail";
    private final String passwordFieldPlaceholder = "Mot de passe";
    private final Color defaultTextColor = UIManager.getColor("TextField.foreground");

    public LoginWindow() {

        super("NotesManager");
        this.setSize(300, 500);

        // Create JPanel and set no specific layout manager
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Create JLabel with ImageIcon for header
        JLabel titleLabel = new JLabel("NotesManager", SwingConstants.CENTER);
        ImageIcon appLogo = new ImageIcon("src/main/resources/notes-manager-logo.png");
        titleLabel.setIcon(appLogo);
        titleLabel.setBounds(0, 0, 300, 250);
        titleLabel.setVerticalTextPosition(JLabel.BOTTOM);
        titleLabel.setHorizontalTextPosition(JLabel.CENTER);
        titleLabel.setFont(new Font("Jetbrains Mono", Font.BOLD, 35));

        // Create JTextField for e-mail
        emailField = new JTextField(emailFieldPlaceholder);
        emailField.setBounds(55, 260, 190, 28);
        emailField.setHorizontalAlignment(JTextField.CENTER);
        emailField.setForeground(Color.GRAY);
        emailField.addFocusListener(this);

        // Create JPasswordField for password
        passwordField = new JPasswordField(passwordFieldPlaceholder);
        passwordField.setBounds(55, 295, 190, 28);
        passwordField.setHorizontalAlignment(JPasswordField.CENTER);
        passwordField.setForeground(Color.GRAY);
        passwordField.addFocusListener(this);

        // Create JButton for login
        loginButton = new JButton("S'identifier");
        loginButton.setBounds(70, 335, 160, 30);
        loginButton.addActionListener(this);

        // Create JButton to go to RegisterWindow
        registerButton = new JButton("Créer un compte");
        registerButton.setBounds(70, 375, 160, 30);
        registerButton.addActionListener(this);

        // Add all components to panel
        panel.add(titleLabel);
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // Add panel to LoginWindow frame
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
        // If user clicks on loginButton
        if (e.getSource() == loginButton) {
            String email = emailField.getText();
            String password = passwordField.getText();
            System.out.println("E-mail entry : " + email);
            System.out.println("Password entry : " + password);

            // If email and password format are correct
            if (FormatChecker.checkEmailFormat(email) && FormatChecker.checkPasswordFormat(password)) {

                // Check if e-mail and password match
//                if (DBConnector.checkPasswordMatch(email, password)) {
                if (true) {

                    System.out.println("E-mail and password match!");
                    // Open MainWindow and close LoginWindow
                    MainWindow mainWindow = new MainWindow(email);
                    this.dispose();

                } else {
                    System.out.println("E-mail and password DO NOT match!");
                    JOptionPane.showMessageDialog(null, "E-mail et/ou mot de passe invalides");
                }
            } else {
                System.out.println("Incorrect format for either e-mail or password.");
                JOptionPane.showMessageDialog(null, "Format incorrect de l'e-mail et/ou mot de passe");

            }
        } else if (e.getSource() == registerButton) {
            // Close LoginWindow and open MainWindow
            System.out.println("Open register window");

            RegisterWindow registerWindow = new RegisterWindow();
            this.dispose();
        }
    }

    // Method to use in FocusListener to update text and color of JTextField when focus is gained or lost
    private void updateTextOnFocus(
            FocusEvent e,
            JTextField textField,
            String textToCompare,
            String textToSet,
            Color colorToSet
    ) {
        if (e.getSource() == textField) {
            if (textField.getText().trim().equals(textToCompare)) {
                textField.setText(textToSet);
                textField.setForeground(colorToSet);
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        updateTextOnFocus(e, emailField, emailFieldPlaceholder, "", defaultTextColor);
        updateTextOnFocus(e, passwordField, passwordFieldPlaceholder, "", defaultTextColor);
    }

    @Override
    public void focusLost(FocusEvent e) {
        updateTextOnFocus(e, emailField, "", emailFieldPlaceholder, Color.GRAY);
        updateTextOnFocus(e, passwordField, "", passwordFieldPlaceholder, Color.GRAY);
    }


}
