package com.example.notesmanager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public abstract class FormatChecker {

    
    private static boolean regexChecker(String stringToCheck, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(stringToCheck);
        return matcher.matches();
    }


    
    public static boolean checkEmailFormat(String email) {
        final String regexPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return regexChecker(email, regexPattern);
    }


    
    public static boolean checkPasswordFormat(String password) {
        final String regexPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-+=])[^\\s]{8,}$";
        return regexChecker(password, regexPattern);
    }


    
    public static boolean checkNameFormat(String name) {
        final String regexPattern = ".*[a-zA-Z].*";
        return regexChecker(name, regexPattern);
    }


    public static boolean checkLabelFormat(String nameLabel) {
        final String regexPattern = "^(?!$).{2,16}$";
        return regexChecker(nameLabel, regexPattern);
    }
}