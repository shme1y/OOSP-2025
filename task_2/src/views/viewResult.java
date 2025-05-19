package views;

import controllers.Input;
import models.Parenthesis;

import java.util.regex.Pattern;

public class viewResult {
    public static String processInput() {
        StringBuilder s = new StringBuilder(" ");
        String result;

        if (Pattern.matches("^[a-zA-Zа-яА-Я]+$", Input.input())) {
            result = "Формула введена не верно";
        } else {
            result = new Parenthesis().parenthesis(s, new StringBuilder(Input.input())).toString();
        }

        return result;
    }
}
