package org.serfa.lpdaoo2024;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class PanelTree extends JPanel {
    public PanelTree() {
        //create different component
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Espace de travail");
        DefaultMutableTreeNode hBinder = new DefaultMutableTreeNode("Classeur vacances");
        DefaultMutableTreeNode binder = new DefaultMutableTreeNode("Classeur travail");
        DefaultMutableTreeNode divider1 = new DefaultMutableTreeNode("Intercallaire violet");
        DefaultMutableTreeNode divider2 = new DefaultMutableTreeNode("Intercallaire rouge");
        DefaultMutableTreeNode divider3 = new DefaultMutableTreeNode("Intercallaire violet");
        DefaultMutableTreeNode sheet1 = new DefaultMutableTreeNode("Feuille 1");
        DefaultMutableTreeNode sheet2 = new DefaultMutableTreeNode("Feuille 2");

        //connection
        divider1.add(sheet1);
        divider1.add(sheet2);
        binder.add(divider1);
        binder.add(divider2);
        hBinder.add(divider3);
        root.add(hBinder);
        root.add(binder);

        JTree tree = new JTree(root);
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Arbo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PanelTree());
        frame.pack();
        frame.setVisible(true);
    }

    //display tree
    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }*/
}
