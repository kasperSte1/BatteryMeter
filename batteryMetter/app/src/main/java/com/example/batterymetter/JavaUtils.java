package com.example.batterymetter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {
    public static boolean Password_Validation(String password)
    {

        if(password.length()>=8)
        {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            //Pattern eight = Pattern.compile (".{8}");


            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        }
        else
            return false;

    }
}
