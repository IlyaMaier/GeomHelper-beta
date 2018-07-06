package com.example.geomhelper.content;

import com.example.geomhelper.R;

import java.util.ArrayList;

public class ThirdTasks {

    public ArrayList<ThirdTask> getTasks(int test, int theme) {
        ArrayList<ThirdTask> thirdTasks = new ArrayList<>();
        switch (test) {
            case 0:
                switch (theme) {
                    case 0:
                        thirdTasks.add(cutTest1);
                        thirdTasks.add(cutTest2);
                        thirdTasks.add(cutTest3);
                        break;
                    case 1:
                        thirdTasks.add(anglesTest1);
                        thirdTasks.add(anglesTest2);
                        break;
                    case 2:
                        thirdTasks.add(compareTest1);
                        thirdTasks.add(compareTest2);
                        break;
                    case 3:
                        thirdTasks.add(measureTest);
                        break;
                    case 4:
                        thirdTasks.add(perpendicularTest);
                        break;
                }
                break;
            case 1:
                switch (theme) {
                    case 0:
                        thirdTasks.add(trianglesTest1);
                        thirdTasks.add(trianglesTest2);
                        break;
                    case 1:
                        thirdTasks.add(trianglesTest3);
                        break;
                    case 2:
                        thirdTasks.add(trianglesTest4);
                        break;
                    case 3:
                        thirdTasks.add(trianglesTest5);
                        break;
                }
                break;
        }
        return thirdTasks;
    }

    //test
    private String[] cutTask1 = {"Сколько отрезков изображено на рисунке?",
            "Сколько прямых изображено на рисунке?",
            "Сколько точек изображено на рисунке?"};
    private String[] cutAnswer1 = {"2", "0", "4"};
    private ThirdTask cutTest1 = new ThirdTask(cutTask1[0],
            cutTask1[1], cutTask1[2], R.drawable.straight_and_cut, cutAnswer1);

    private String[] cutTask2 = {"Сколько отрезков изображено на рисунке?",
            "Сколько прямых изображено на рисунке?",
            "Сколько точек изображено на рисунке?"};
    private String[] cutAnswer2 = {"3", "1", "3"};
    private ThirdTask cutTest2 = new ThirdTask(cutTask2[0],
            cutTask2[1], cutTask2[2], R.drawable.straight_and_cut_4, cutAnswer2);

    private String[] cutTask3 = {"Сколько отрезков изображено на рисунке?",
            "Сколько прямых изображено на рисунке?",
            "Сколько точек изображено на рисунке?"};
    private String[] cutAnswer3 = {"5", "1", "5"};
    private ThirdTask cutTest3 = new ThirdTask(cutTask3[0],
            cutTask3[1], cutTask3[2], R.drawable.straight_and_cut_5, cutAnswer3);

    private String[] anglesTask1 = {"Сколько лучей изображено на рисунке?",
            "Сколько точек заключает луч NA?",
            "Какая точка принадлежат лучу LA?"};
    private String[] anglesAnswer1 = {"12", "2", "C"};
    private ThirdTask anglesTest1 = new ThirdTask(anglesTask1[0],
            anglesTask1[1], anglesTask1[2], R.drawable.angles_3, anglesAnswer1);

    private String[] anglesTask2 = {"Сколько лучей изображено на рисунке?",
            "Сколько углов изображено на рисунке?",
            "Сколько лучей заключает угол AOD?"};
    private String[] anglesAnswer2 = {"4", "6", "2"};
    private ThirdTask anglesTest2 = new ThirdTask(anglesTask2[0],
            anglesTask2[1], anglesTask2[2], R.drawable.angles_1, anglesAnswer2);

    private String[] compareTask1 = {"Точки B, C, D, E, F, G и H делят отрезок AI на равные части. Назови серединную точку отрезка BD",
            "Точки B, C, D, E, F, G и H делят отрезок AI на равные части. Назови серединную точку отрезка BF",
            "Точки B, C, D, E, F, G и H делят отрезок AI на равные части. Для скольких отрезков точка H является серединной точкой? "};
    private String[] compareAnswer1 = {"C", "D", "1"};
    private ThirdTask compareTest1 = new ThirdTask(compareTask1[0],
            compareTask1[1], compareTask1[2], R.drawable.compare_1, compareAnswer1);

    private String[] compareTask2 = {"VP > PK? (Введи +, если да или -, если нет)",
            "XV > VK? (Введи +, если да или -, если нет)",
            "VP > XK? (Введи +, если да или -, если нет)"};
    private String[] compareAnswer2 = {"+", "-", "-"};
    private ThirdTask compareTest2 = new ThirdTask(compareTask2[0],
            compareTask2[1], compareTask2[2], R.drawable.compare_2, compareAnswer2);

    private String[] measureTask = {"CG  - биссектриса угла DCE. Вычисли угол ECG, если угол DCE = 64",
            "Угол GCE = 35, угол DCG = 35, чему равен угол DCE?",
            "Угол DCE = 80, угол GCE = 35, чему равен угол DCG?"};
    private String[] measureAnswer = {"32", "70", "45"};
    private ThirdTask measureTest = new ThirdTask(measureTask[0],
            measureTask[1], measureTask[2], R.drawable.measure_2, measureAnswer);

    private String[] perpendicularTask = {"OB перпендикулярен LN ? (Введи +, если да или -, если нет)",
            "OL перпендикулярен LN ? (Введи +, если да или -, если нет)",
            "OC перпендикулярен ML ? (Введи +, если да или -, если нет)"};
    private String[] perpendicularAnswer = {"+", "-", "+"};
    private ThirdTask perpendicularTest = new ThirdTask(perpendicularTask[0],
            perpendicularTask[1], perpendicularTask[2], R.drawable.perpendicular2, perpendicularAnswer);

    private String[] trianglesTask1 = {"Известно, что треугольник ABC равен треугольнику MNK. Верно ли что AB = MN? (Введи +, если да или -, если нет)",
            "Известно, что треугольник ABC равен треугольнику MNK. Верно ли что AC = NK? (Введи +, если да или -, если нет)",
            "Известно, что треугольник ABC равен треугольнику MNK. Верно ли что BC = NK? (Введи +, если да или -, если нет)"};
    private String[] trianglesAnswer1 = {"+", "-", "+"};
    private ThirdTask trianglesTest1 = new ThirdTask(trianglesTask1[0],
            trianglesTask1[1], trianglesTask1[2], R.drawable.sm, trianglesAnswer1);

    private String[] trianglesTask2 = {"Известно, что треугольник FGB равен треугольнику MKL. Верно ли что FG = KL? (Введи +, если да или -, если нет)",
            "Известно, что треугольник FGB равен треугольнику MKL. Верно ли что GB = KL? (Введи +, если да или -, если нет)",
            "Известно, что треугольник FGB равен треугольнику MKL. Верно ли что FB = ML? (Введи +, если да или -, если нет)"};
    private String[] trianglesAnswer2 = {"-", "+", "+"};
    private ThirdTask trianglesTest2 = new ThirdTask(trianglesTask2[0],
            trianglesTask2[1], trianglesTask2[2], R.drawable.sm, trianglesAnswer2);

    private String[] trianglesTask3 = {"Вычисли сторону AB треугольника BAC, если CF — медиана, FB = 300",
            "Вычисли периметр треугольника BAC, если CF — медиана, AC = CB = 800 и FB = 300",
            "Периметр равнобедренного треугольника ACB с основанием AC равен 30, а периметр равностороннего треугольника ACD равен 18. Найди длину боковой стороны равнобедренного треугольника"};
    private String[] trianglesAnswer3 = {"600", "2200", "12"};
    private ThirdTask trianglesTest3 = new ThirdTask(trianglesTask3[0],
            trianglesTask3[1], trianglesTask3[2], R.drawable.sm, trianglesAnswer3);

    private String[] trianglesTask4 = {"Сколько пар равных треугольников изображено на рисунке?",
            "Периметр треугольника OFA равен 8, OF = 3. Вычисли периметр трегольника BOC.",
            "Периметр треугольника OFD равен 12, OF = 5. Вычисли периметр трегольника ABC."};
    private String[] trianglesAnswer4 = {"8", "10", "28"};
    private ThirdTask trianglesTest4 = new ThirdTask(trianglesTask4[0],
            trianglesTask4[1], trianglesTask4[2], R.drawable.triangles_4, trianglesAnswer4);

    private String[] trianglesTask5 = {"Дополни данные условия необходимым равенством для выполнения равенста треугольников TVU и ZPG по первому признаку (знак угла и пробелы ставить не нужно): TV = ZP, VU = PG, ...",
            "Дополни данные условия необходимым равенством для выполнения равенста треугольников TVU и ZPG по второму признаку (знак угла и пробелы ставить не нужно): TU = ZG, T = Z, ...",
            "Дополни данные условия необходимым равенством для выполнения равенста треугольников TVU и ZPG по третьему признаку (пробелы ставить не нужно): TV = ZP, VU = PG, ..."};
    private String[] trianglesAnswer5 = {"V=P", "U=G", "UT=GZ"};
    private ThirdTask trianglesTest5 = new ThirdTask(trianglesTask5[0],
            trianglesTask5[1], trianglesTask5[2], R.drawable.triangles_6, trianglesAnswer5);

}