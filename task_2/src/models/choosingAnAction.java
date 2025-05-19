package models;

public class choosingAnAction {
    public void choosingAnAction(StringBuilder input) {

         int index = 0;
        while (input.indexOf("^") != -1 || (index = input.indexOf("**")) != -1) {
            if (input.indexOf("**") != -1){
                input.replace(index, index + 2, "^");
            }
            Degree.degree(input);
        }

        while (input.indexOf("log") != -1) {
            Log.Log(input);
        }

        while (input.indexOf("exp") != -1) {
            Exponent.Exponent(input);
        }

        for (int i = 0; i < input.length(); i++) {
            switch (input.charAt(i)) {
                case '!' -> {
                    Factorial.faсtorial(input);
                    i = -1;
                }
                case '/' -> {
                    if (i + 1 < input.length() && input.charAt(i + 1) == '/') {
                        input.replace(i, i + 2, ":");
                        IntegerDivision.integerDivision(input);
                        i = -1;
                    } else {
                        new Division().division(input);
                        i = -1;
                    }
                }
                case '*' -> {
                    Multiplication.multiplication(input);
                    i = -1;
                }
            }
        }


        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '+' -> {
                    Summation.summation(input);
                    i = -1;
                }
                case '-' -> {
                    if (i > 0 && Character.isDigit(input.charAt(i - 1))) {
                        Subtraction.subtraction(input);
                        i = -1;
                    }
                }
            }
        }
    }
}
