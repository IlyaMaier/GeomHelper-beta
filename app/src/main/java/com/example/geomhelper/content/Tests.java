package com.example.geomhelper.content;

import com.example.geomhelper.R;

import java.util.ArrayList;
import java.util.List;

public class Tests {

    public List<Test> getCurrentTests() {
        List<Test> tests = new ArrayList<>();
        tests.add(0, basics);
        tests.add(0, second);
        tests.add(0, third);
        tests.add(0, fourth);
        return tests;
    }

    //basics
    private String[] basicsThemes = {"Прямая и отрезок",
            "Луч и угол",
            "Сравнение отрезков и углов",
            "Измерение отрезков и углов",
            "Перпендикулярные прямые"};
    private Test basics = new Test("Начальные геометрические сведения",
            5,
            basicsThemes,
            R.drawable.background_test_1);
    // end basics

    // Second
    private String[] secondThemes = {"Первый признак равенства треугольников",
            "Медианы, биссектрисы и высоты треугольника",
            "Второй признак равенства треугольников",
            "Третий признак равенства треугольников"};
    private Test second = new Test("Треугольники",
            4,
            secondThemes,
            R.drawable.background_test_2);
    // end second

    // Third
    private String[] thirdThemes = {"Признаки параллельности двух прямых",
            "Аксиома параллельных прямых",
            "Практические задачи"};
    private Test third = new Test("Параллельные прямые",
            3,
            thirdThemes,
            R.drawable.background_test_3);
    // end third

    // Fourth
    private String[] fourthThemes = {"Сумма углов треугольника",
            "Соотношения между сторонами и углами",
            "Прямоугольные треугольники",
            "Построение треугольника по трем элементам"};
    private Test fourth = new Test("Соотношения между сторонами и углами ▲",
            4,
            fourthThemes,
            R.drawable.background_test_4);
    // end fourth

}
