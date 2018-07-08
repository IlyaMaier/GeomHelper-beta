package ru.project.geomhelper.content;

import java.util.ArrayList;

import ru.project.geomhelper.R;

public class FirstTasks {

    public ArrayList<FirstTask> getTasks(int test, int theme) {
        ArrayList<FirstTask> firstTasks = new ArrayList<>();
        switch (test) {
            case 0:
                switch (theme) {
                    case 0:
                        firstTasks.add(straightAndCut);
                        firstTasks.add(straightAndCut1);
                        firstTasks.add(straightAndCut2);
                        break;
                    case 1:
                        firstTasks.add(angles1);
                        firstTasks.add(angles2);
                        break;
                    case 2:
                        firstTasks.add(compare1);
                        firstTasks.add(compare2);
                        break;
                    case 3:
                        firstTasks.add(measure);
                        break;
                    case 4:
                        firstTasks.add(perpendicular);
                        break;
                }
                break;
            case 1:
                switch (theme) {
                    case 0:
                        firstTasks.add(triangles_1);
                        firstTasks.add(triangles_2);
                        break;
                    case 1:
                        firstTasks.add(triangles_3);
                        break;
                    case 2:
                        firstTasks.add(triangles_4);
                        break;
                    case 3:
                        firstTasks.add(triangles_5);
                        break;
                }
                break;
        }
        return firstTasks;
    }

    //Прямая и отрезок
    private FirstTask straightAndCut = new FirstTask(
            "Пересекаются ли отрезки AB и CD?",
            R.drawable.straight_and_cut,
            "Да",
            "Нет",
            "Невозможно определить",
            1);

    private FirstTask straightAndCut1 = new FirstTask(
            "Пересекаются ли прямые АВ и CD?",
            R.drawable.straight_and_cut,
            "Да",
            "Нет",
            "Невозможно определить",
            2);

    private FirstTask straightAndCut2 = new FirstTask(
            "Если две прямые на плоскости не параллельны, то они не пересекаются",
            R.drawable.sm,
            "Верно",
            "Неверно",
            "Невозможно определить",
            1);

    private FirstTask angles1 = new FirstTask(
            "Назови угол, который не имеет общей стороны с углом AOB",
            R.drawable.angles_1,
            "COD",
            "AOC",
            "AOD",
            0);

    private FirstTask angles2 = new FirstTask(
            "Назови угол, который не имеет общей внутренней области (не считая сторон) с углом AOB",
            R.drawable.angles_1,
            "Такого угла нет",
            "AOC",
            "COD",
            2);

    private FirstTask compare1 = new FirstTask(
            "Верно ли, что AD > DI ?",
            R.drawable.compare_1,
            "Да",
            "Нет",
            "Невозможно определить",
            1);

    private FirstTask compare2 = new FirstTask(
            "Верно ли, что XV < VP ?",
            R.drawable.compare_2,
            "Да",
            "Нет",
            "Невозможно определить",
            0);

    private FirstTask measure = new FirstTask(
            "Верно ли, что JK = 5 см, если JL = 9 см ?",
            R.drawable.measure_1,
            "Да",
            "Нет",
            "Невозможно определить",
            1);

    private FirstTask perpendicular = new FirstTask(
            "Смежные углы относятся как 1:4 (угол B больше угла A)Верно ли, что угол A = 25 ?",
            R.drawable.sm,
            "Да",
            "Нет",
            "Невозможно определить",
            1);

    private FirstTask triangles_1 = new FirstTask(
            "Какие треугольники на рисунке равны?",
            R.drawable.triangles_1,
            "AOC и COB",
            "AOC и ODB",
            "CDB и COB",
            1);

    private FirstTask triangles_2 = new FirstTask(
            "AB = CD, ∠ABC = ∠DCB. Какие треугольники будут равны по первому признаку равенства треугольников?",
            R.drawable.triangles_2,
            "ABC и DCB",
            "ABO и CBO",
            "ABC и COD",
            0);

    private FirstTask triangles_3 = new FirstTask(
            "Дан треугольник HIG. JH — биссектриса угла GHI. Вычисли угол IHJ, если ∢GHI = 156°",
            R.drawable.sm,
            "56",
            "24",
            "78",
            2);

    private FirstTask triangles_4 = new FirstTask(
            "Какие из треугольников на рисунке равны?",
            R.drawable.triangles_4,
            "AOB и COB",
            "DOA и COB",
            "DFO и ABC",
            1);

    private FirstTask triangles_5 = new FirstTask(
            "Треугольник MPK = треугольнику KLM по третьему признаку. Назови соответсвенно равные стороны.",
            R.drawable.triangles_4,
            "MP = KL",
            "PK = KL",
            "ML = MK",
            0);

}