package com.example.treeviewnotes;

import javafx.scene.paint.Color;

public class Binder {
    private String name;
    private int colorId;
    private String colorName;

    // Constructeur
    public Binder(String name, int colorId) {
        this.name = name;
        this.colorId = colorId;
    }

    // Constructeur
    public Binder(String name, String colorName) {
        this.name = name;
        this.colorName = colorName;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }



    // Méthode pour obtenir la couleur en tant qu'objet Color (si vous utilisez le nom de couleur directement)
    public Color getColor() {
        // Cette méthode suppose que la couleur est stockée par son nom dans une forme compatible avec JavaFX,
        // comme "red", "blue", "green", etc.
        return Color.web(colorName);
    }
}
