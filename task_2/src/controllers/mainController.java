package controllers;

import views.viewResult;

public class mainController {
    public static void mainWork() {System.out.println("В данном калькуляторе реализованны функции: \n" +
            " + - сложение, - - вычитание, * - умножение, / - деление, // - деление без остатка, % остаток от деления, ^ или ** - возведение в степень, exp(), log() и  ! факториал числа" +
            "() - скобки.\n" + "Вывод ^ происходит только целочисленный, а ввод, и целочисленный, и дробный");
        System.out.println(viewResult.processInput());
    }
}
