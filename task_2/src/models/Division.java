package models;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class Division {
    public void division(StringBuilder input) {
        int start, end;
        for (int i = 0; i < input.length(); ++i) {
            if (input.charAt(0) == '/' || input.charAt(input.length() - 1) == '/') {
                System.out.println("Ошибка в формуле");
                break;
            } else if (input.charAt(i) == '/') {
                start = i - 1;
                end = i + 1;

                while (start > 0 && Pattern.matches("[0-9.]", Character.toString(input.charAt(start)))) start--;
                if (start > 0 && !Pattern.matches("[0-9.]", Character.toString(input.charAt(start)))) start++;

                if (input.charAt(end) == '-' && !Pattern.matches("[0-9.]", Character.toString(input.charAt(end - 1))))
                    end++;
                while (end < input.length() && Pattern.matches("[0-9.]", Character.toString(input.charAt(end))))
                    end++;

                BigDecimal num1 = new BigDecimal(input.substring(start, i));
                BigDecimal num2 = new BigDecimal(input.substring(i + 1, end));

                if (num2.compareTo(BigDecimal.ZERO) == 0) {
                    System.out.println("Ошибка: деление на ноль");
                    break;
                }

                BigDecimal result = num1.divide(num2, 15, BigDecimal.ROUND_HALF_UP);
                input.replace(start, end, result.stripTrailingZeros().toPlainString());
                i = 0;
            }
        }
        RecursionPath.recursionPath(input);
    }
}
