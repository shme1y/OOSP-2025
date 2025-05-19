package models;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Log {
    public static void Log(StringBuilder input){
        //System.out.println(input);
        int start, end;

        for (int i = 0; i < input.length(); i++) {
            if (input.substring(i).startsWith("log")) {
                start = i + 3;
                end = start;

                while (end < input.length() && Pattern.matches("[0-9.]", Character.toString(input.charAt(end)))) {
                    ++end;
                }
                    BigDecimal num = new BigDecimal(input.substring(start, end));

                    if (num.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ArithmeticException("Ошибка: log(x) не может быть для x <= 0");
                    }

                    BigDecimal result = BigDecimal.valueOf(Math.log(num.doubleValue()));
                    input.replace(i, end, result.toPlainString());
                    i = 0;
                }
            }
        RecursionPath.recursionPath(input);
    }
}
