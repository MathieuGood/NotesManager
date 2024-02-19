package org.serfa.lpdaoo2024;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final String userEmail;

    MainWindow(String userEmail) {

        super();

        this.userEmail = userEmail;
        this.setTitle("NotesManager " + userEmail);
        this.setSize(800, 600);
        this.setMinimumSize(new Dimension(640, 400));
        this.setLocationRelativeTo(null);

        JPanel contentPane = (JPanel) this.getContentPane();

        JTree sideTree = new JTree();
        JScrollPane sideTreeScrollPane = new JScrollPane(sideTree);
        sideTreeScrollPane.setMinimumSize(new Dimension(200, 600));

        JTextArea noteText = new JTextArea("Nouvelle note");
        noteText.setBackground(Color.WHITE);
        JScrollPane noteTextScrollPane = new JScrollPane(noteText);
        noteTextScrollPane.setMinimumSize(new Dimension(200, 600));

        JToolBar topToolBar = new JToolBar();
        topToolBar.setMinimumSize(new Dimension(800, 100));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideTreeScrollPane, noteTextScrollPane);
        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(topToolBar, BorderLayout.NORTH);



        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
