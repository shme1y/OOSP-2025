package models;

import static models.Between.between;

public class RecursionPath {
    public static void recursionPath(StringBuilder input){
            //System.out.println(between());
            new Parenthesis().parenthesis(input, between());
    }
}
