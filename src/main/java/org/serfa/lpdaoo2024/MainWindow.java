package org.serfa.lpdaoo2024;

import javax.swing.*;

public class MainWindow extends JFrame {

    private final String userEmail;

    MainWindow(String userEmail) {

        super();

        this.userEmail = userEmail;
        this.setTitle("NotesManager " + userEmail);
        this.setSize(800, 600);





        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
