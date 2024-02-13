package org.serfa.lpdaoo2024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame implements ActionListener {

    JButton loginButton;
    JTextField emailField;
    JTextField passwordField;

    public LoginWindow() {


        super("Welcome to NotesManager");

        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Login");

        JLabel emailLabel = new JLabel("E-mail");
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(150, 20));

        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(150, 20));

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        panel.add(titleLabel);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        this.add(panel);
        this.setVisible(true);
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
                if (DBConnector.checkPasswordMatch(email, password)) {
                    System.out.println("E-mail and password match!");
                } else {
                    System.out.println("E-mail and password DO NOT match!");
                }
            } else {
                System.out.println("Incorrect format for either e-mail or password.");
            }
        }
    }
}
