package ru.project.geomhelper.content;

import java.util.ArrayList;

import ru.project.geomhelper.R;

public class SecondTasks {

    public ArrayList<SecondTask> getTasks(int test, int theme) {
        ArrayList<SecondTask> secondTasks = new ArrayList<>();
        switch (test) {
            case 0:
                switch (theme) {
                    case 0:
                        secondTasks.add(straightAndCut);
                        secondTasks.add(straightAndCut1);
                        secondTasks.add(straightAndCut2);
                        break;
                    case 1:
                        secondTasks.add(angles1);
                        secondTasks.add(angles2);
                        break;
                    case 2:
                        secondTasks.add(compare1);
                        secondTasks.add(compare2);
                        break;
                    case 3:
                        secondTasks.add(measure);
                        break;
                    case 4:
                        secondTasks.add(perpendicular);
                        break;
                }
                break;
            case 1:
                switch (theme) {
                    case 0:
                        secondTasks.add(triangles_1);
                        secondTasks.add(triangles_2);
                        break;
                    case 1:
                        secondTasks.add(triangles_3);
                        break;
                    case 2:
                        secondTasks.add(triangles_4);
                        break;
                    case 3:
                        secondTasks.add(triangles_5);
                        break;
                }
                break;
        }
        return secondTasks;
    }

    //прямая и отрезок
    private SecondTask straightAndCut = new SecondTask(
            "Сколько отрезков с концами в отмеченных точках изображено на рисунке?",
            R.drawable.straight_and_cut_2,
            "7");

    private SecondTask straightAndCut1 = new SecondTask(
            "Сколько отрезков на рисунке?",
            R.drawable.straight_and_cut_3,
            "11");

    private SecondTask straightAndCut2 = new SecondTask(
            "Можно ли на плоскости провести 4 прямые, у которых имеется 1 точка пересечения?" +
                    "(введи +, если можно или -, если нельзя)",
            R.drawable.sm,
            "+");

    private SecondTask angles1 = new SecondTask(
            "Сколько образовалось углов во внутренней области угла AOD, считая сам угол AOD? ",
            R.drawable.angles_1,
            "6");

    private SecondTask angles2 = new SecondTask(
            "Введи вершину данного угла",
            R.drawable.angles_2,
            "L");

    private SecondTask compare1 = new SecondTask(
            "Для скольких отрезков точка F является серединной точкой? ",
            R.drawable.compare_1,
            "3");

    private SecondTask compare2 = new SecondTask(
            "Из одной точки исходят 3 луча. Известно, что луч p является биссектрисой для угла ∡ny. Можно ли совместить углы np и ny?(Введи +, если да или -, если нет)",
            R.drawable.compare_3,
            "+");

    private SecondTask measure = new SecondTask(
            "Найди длину отрезка CA, если AB=5 и BC=13 (Все отрезки лежат на одной прямой)",
            R.drawable.sm,
            "18");

    private SecondTask perpendicular = new SecondTask(
            "Найди угол 3, если угол 1 = 136",
            R.drawable.perpendicular,
            "136");

    private SecondTask triangles_1 = new SecondTask(
            "Отрезки AB и CD пересекаются в точке О, которая является серединой каждого из отрезков. ОВ = 7, BD = 4. Чему равна длина отрезка АС",
            R.drawable.triangles_1,
            "4");

    private SecondTask triangles_2 = new SecondTask(
            "Отрезки AB и CD пересекаются в точке О, которая является серединой каждого из отрезков. Найти ∠А, если ∠B = 55°, ∠D = 72°.",
            R.drawable.triangles_3,
            "55");

    private SecondTask triangles_3 = new SecondTask(
            "ΔABC — равнобедренный, AB = BC, ∡A + ∡C = 78°. Определи величину ∡A.",
            R.drawable.triangles_3,
            "39");

    private SecondTask triangles_4 = new SecondTask(
            "CB = 8. Чему равно DF?",
            R.drawable.triangles_4,
            "4");

    private SecondTask triangles_5 = new SecondTask(
            "Используя информацию, данную на рисунке, определи величину угла LKN, усли угол LKM = 44°",
            R.drawable.triangles_5,
            "88");

}