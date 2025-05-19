package models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static models.Between.*;
import static models.Between.getEndModule;

public class NumberProcess {
    public void processNumber(String input, StringBuilder  inputUser){
            //System.out.println(inputUser);
            new choosingAnAction().choosingAnAction(inputUser);
    }
}
//|2+2|
//||2+2|+2|
//(|2+2|*|2+2|)
//(|2+2|)