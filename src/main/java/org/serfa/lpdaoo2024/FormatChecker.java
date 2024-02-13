package org.serfa.lpdaoo2024;

import java.util.regex.*;

public abstract class FormatChecker {


    public static boolean checkIfStringIsNotEmpty(String string) {
        return string != null && !string.isEmpty();
    }

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

}
