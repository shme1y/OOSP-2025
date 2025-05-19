package controllers;
import java.util.Scanner;

public class Input {
    private static Scanner scn = new Scanner(System.in);
    private static String inp = scn.nextLine();

    public static String input(){
        return inp.replaceAll(" ", "");
    }
}