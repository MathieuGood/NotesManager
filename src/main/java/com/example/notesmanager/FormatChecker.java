package com.example.notesmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The FormatChecker class provides static methods for checking the format of various types of strings.
 * It is an abstract class, meaning it cannot be instantiated.
 */
public abstract class FormatChecker {

    /**
     * Checks if a given string matches a given regular expression pattern.
     *
     * @param stringToCheck the string to check
     * @param regexPattern  the regular expression pattern to match against
     * @return true if the string matches the pattern, false otherwise
     */
    private static boolean regexChecker(String stringToCheck, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(stringToCheck);
        return matcher.matches();
    }


    /**
     * Checks if a given string is in a valid email format.
     *
     * @param email the string to check
     * @return true if the string is in a valid email format, false otherwise
     */
    public static boolean checkEmailFormat(String email) {
        final String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return regexChecker(email, regexPattern);
    }


    /**
     * Checks if a given string is in a valid password format.
     * A valid password must have at least 8 characters, one lowercase letter, one uppercase letter, one digit, one special character, and no spaces.
     *
     * @param password the string to check
     * @return true if the string is in a valid password format, false otherwise
     */
    public static boolean checkPasswordFormat(String password) {
        final String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[^\\s]{8,}$";
        return regexChecker(password, regexPattern);
    }


    /**
     * Checks if a given string contains at least one alphabetic character.
     *
     * @param name the string to check
     * @return true if the string contains at least one alphabetic character, false otherwise
     */
    public static boolean checkNameFormat(String name) {
        final String regexPattern = ".*[a-zA-Z].*";
        return regexChecker(name, regexPattern);
    }


    public static boolean checkLabelFormat(String nameLabel) {
        final String regexPattern = "^(?!$).{4,10}$";
        return regexChecker(nameLabel, regexPattern);
    }
}