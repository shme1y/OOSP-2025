package models;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Exponent {
    public static void Exponent(StringBuilder input){
        //System.out.println(input);
        int start, end;

        for (int i = 0; i < input.length(); i++) {
            if (input.substring(i).startsWith("exp")) {
                start = i + 3;
                end = start;

                while (end < input.length() && Pattern.matches("[0-9.]", Character.toString(input.charAt(end)))) {
                    ++end;
                }

                    BigDecimal num = new BigDecimal(input.substring(start, end));
                    BigDecimal result = BigDecimal.valueOf(Math.exp(num.doubleValue()));
                    input.replace(i, end, result.toPlainString());
                    i = 0;
            }
            RecursionPath.recursionPath(input);
        }
    }
}
