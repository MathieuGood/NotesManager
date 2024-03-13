package com.example.notesmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe abstraite FormatChecker qui fournit des méthodes pour vérifier les formats de différentes chaînes.
 */
public abstract class FormatChecker {


    /**
     * Vérifie si une chaîne donnée correspond à un motif regex donné.
     *
     * @param stringToCheck La chaîne à vérifier.
     * @param regexPattern  Le motif regex à utiliser pour la vérification.
     * @return true si la chaîne correspond au motif regex, false sinon.
     */
    private static boolean regexChecker(String stringToCheck, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(stringToCheck);
        return matcher.matches();
    }


    /**
     * Vérifie si une chaîne donnée est un format d'e-mail valide.
     *
     * @param email La chaîne à vérifier.
     * @return true si la chaîne est un format d'e-mail valide, false sinon.
     */
    public static boolean checkEmailFormat(String email) {
        final String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return regexChecker(email, regexPattern);
    }


    /**
     * Vérifie si une chaîne donnée est un format de mot de passe valide.
     *
     * @param password La chaîne à vérifier.
     * @return true si la chaîne est un format de mot de passe valide, false sinon.
     */
    public static boolean checkPasswordFormat(String password) {
        final String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[^\\s]{8,}$";
        return regexChecker(password, regexPattern);
    }


    /**
     * Vérifie si une chaîne donnée est un format de nom valide.
     *
     * @param name La chaîne à vérifier.
     * @return true si la chaîne est un format de nom valide, false sinon.
     */
    public static boolean checkNameFormat(String name) {
        final String regexPattern = ".*[a-zA-Z].*";
        return regexChecker(name, regexPattern);
    }


    /**
     * Vérifie si une chaîne donnée est un format d'étiquette valide.
     *
     * @param nameLabel La chaîne à vérifier.
     * @return true si la chaîne est un format d'étiquette valide, false sinon.
     */
    public static boolean checkLabelFormat(String nameLabel) {
        final String regexPattern = "^(?!$).{2,16}$";
        return regexChecker(nameLabel, regexPattern);
    }

}