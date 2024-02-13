package org.serfa.lpdaoo2024;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        goBackButton = new JButton("Retour à l'identification");

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
        if (e.getSource() == goBackButton) {
            System.out.println("Go back to LoginWindow");
        } else if (e.getSource() == registerButton) {
            System.out.println("Register button clicked");

        }
    }
}
