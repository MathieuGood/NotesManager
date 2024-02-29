package com.example.notesmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FormatChecker {

    // Checks if stringToCheck matches regexPattern and return true/false
    private static boolean regexChecker(String stringToCheck, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(stringToCheck);
        return matcher.matches();
    }

    // Check if e-mail format is valid and return true/false
    public static boolean checkEmailFormat(String email) {
        final String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return regexChecker(email, regexPattern);
    }

    // Check if password format is valid and return true/false
    // Password must have at least :
    // - 8 characters
    // - one lowercase alpha
    // - one uppercase alpha
    // - one digit
    // - one special character
    // - no space
    public static boolean checkPasswordFormat(String password) {
        final String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[^\\s]{8,}$";
        return regexChecker(password, regexPattern);
    }

    // Check if name has at least one alpha character
    public static boolean checkNameFormat(String name) {
        final String regexPattern = ".*[a-zA-Z].*";
        return regexChecker(name, regexPattern);
    }

    // Check if two strings are the same
    public static boolean checkIfTwoStringsMatch(String string1, String string2) {
        return string1.equals(string2);
    }

}
