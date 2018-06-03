package com.example.geomhelper.content;

import java.util.ArrayList;
import java.util.List;

public class Definitions {

    private ArrayList<String> defs, definitions;

    public Definitions() {
        defs = new ArrayList<>();
        definitions = new ArrayList<>();

        defs.add("7");
        defs.add("71");
        defs.add("Геометрия");
        defs.add("Отрезок");
        defs.add("Угол");
        defs.add("Развернутый угол");
        defs.add("Равные фигуры");
        defs.add("Середина отрезка");
        defs.add("Биссектриса угла");
        defs.add("Прямой угол");
        defs.add("Острый угол");
        defs.add("Тупой угол");
        defs.add("Смежные углы");
        defs.add("Вертикальные углы");
        defs.add("72");
        defs.add("Треугольник");
        defs.add("Теорема");
        defs.add("Медиана треугольника");
        defs.add("Биссектриса треугольника");
        defs.add("Высота треугольника");
        defs.add("Равнобедренный треугольник");
        defs.add("Окружность");
        defs.add("Радиус окружности");
        defs.add("Хорда");
        defs.add("Диаметр");
        defs.add("Круг");
        defs.add("73");
        defs.add("Параллельные прямые");
        defs.add("Аксиомы");
        defs.add("Теорема, обратная данной");
        defs.add("74");
        defs.add("Внешний угол");
        defs.add("Остроугольный треугольник");
        defs.add("Прямоугольный треугольник");
        defs.add("Тупоугольный треугольник");
        defs.add("Гипотенуза");
        defs.add("Катет");
        defs.add("Растояние от точки до прямой");
        defs.add("Расстояние между параллельными прямыми");
        defs.add("75");
        defs.add("8");

        definitions.add("7");
        definitions.add("71");
        definitions.add("наука, занимающаяся изучением геометрических фигур (в переводе с греческого слово «геометрия» означает «землемерие»)");
        definitions.add("это часть прямой, ограниченная двумя точками. Эти точки называются концами отрезка");
        definitions.add("это геометрическая фигура, которая состоит из точки и двух лучей, исходящих из этой точки");
        definitions.add("угол, обе его стороны которого лежат на одной прямой");
        definitions.add("это фигуры, которые можно совместить наложением");
        definitions.add("это точка отрезка, делящая его пополам, т.е. на два равных отрезка");
        definitions.add("это луч, исходящий из вершины угла и делящий его на два равных угла");
        definitions.add("угол, который равен 90°");
        definitions.add("угол, который меньше 90°");
        definitions.add("угол, который больше 90°, но меньше 180°");
        definitions.add("это два угла, у которых одна сторона общая, а две другие являются продолжениями одна другой");
        definitions.add("это углы, стороны одного из которых являются продолжениями сторон другого");
        definitions.add("72");
        definitions.add("это геометрическая фигура, которая состоит из трех точек, не лежащих на одной прямой и трех отрезков, соединяющих эти точки");
        definitions.add("утверждение, справедливость которого устанавливается путём рассуждений. Сами рассуждения называются доказательством теоремы");
        definitions.add("это отрезок, соединяющий вершину треугольника с серединой противоположной стороны");
        definitions.add("это отрезок биссектрисы угла треугольника, соединяющий вершину треугольника с точкой противоположной стороны");
        definitions.add("это перпендикуляр, проведенный из вершины треугольника к прямой, содержащей противоположную сторону");
        definitions.add("это треугольник, две стороны которого равны");
        definitions.add("этогеометрическая фигура, состоящая из всех точек, расположенных на заданном расстоянии от данной точки");
        definitions.add("отрезок, соединяющий центр окружности с какой-либо её точкой");
        definitions.add("отрезок, соединяющий две точки окружности");
        definitions.add("хорда, проходящая через центр окружности");
        definitions.add("это часть плоскости, ограниченная окружностью");
        definitions.add("73");
        definitions.add("две прямые, кторые не пересекаются");
        definitions.add("это утверждения о свойствах геометрических фигур, которые принимаются в качестве исходных положений, на основе которых доказываются теоремы и строится вся геометрия");
        definitions.add("это такая теорема, в которой условием является заключение данной теоремы, а заключением – условие данной теоремы");
        definitions.add("74");
        definitions.add("угол, смежный с каким-нибудь углом этого треугольника");
        definitions.add("треугольник, у которого все углы острые");
        definitions.add("треугольник, у которого один угол прямой");
        definitions.add("треугольник, у которого один угол тупой");
        definitions.add("сторона прямоугольного треугольника, лежащая против прямого угла");
        definitions.add("две стороны, образующие прямой угол");
        definitions.add("длина перпендикуляра, проведённого из этой точки к прямой");
        definitions.add("расстояние от произвольной точки одной из параллельных прямых до другой прямой");
        definitions.add("75");
        definitions.add("8");
    }

    public List<String> getDefs(int form) {
        int f = defs.indexOf(String.valueOf(form));
        int f1 = defs.indexOf(String.valueOf(form + 1));
        return defs.subList(f + 1, f1);
    }

    public List<String> getDefinitions(int form) {
        int f = definitions.indexOf(String.valueOf(form));
        int f1 = definitions.indexOf(String.valueOf(form + 1));
        return definitions.subList(f + 1, f1);
    }

    public List<String> getDefs(int form, int theme) {
        int f = defs.indexOf(String.valueOf(form) + String.valueOf(theme));
        int f1 = defs.indexOf(String.valueOf(form) + String.valueOf(theme + 1));
        return defs.subList(f + 1, f1);
    }

    public List<String> getDefinitions(int form, int theme) {
        int f = definitions.indexOf(String.valueOf(form) + String.valueOf(theme));
        int f1 = definitions.indexOf(String.valueOf(form) + String.valueOf(theme + 1));
        return definitions.subList(f + 1, f1);
    }

    public ArrayList<String> getDefs() {
        return defs;
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }
}
