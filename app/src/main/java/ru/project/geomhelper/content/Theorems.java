package ru.project.geomhelper.content;

import java.util.ArrayList;
import java.util.List;

import ru.project.geomhelper.R;

public class Theorems {

    private ArrayList<String> themes, theorems;
    private ArrayList<Integer> images;

    public Theorems() {
        themes = new ArrayList<>();
        theorems = new ArrayList<>();
        images = new ArrayList<>();

        themes.add("7");
        themes.add("71");
        themes.add("В этом курсе нет теорем");
        themes.add("72");
        themes.add("Первый признак равенства треугольников");
        themes.add("Второй признак равенства треугольников");
        themes.add("Третий признак равенства треугольников");
        themes.add("Теорема о перпендикуляре к прямой");
        themes.add("Медиана делит сторону на два равных отрезка");
        themes.add("В равнобедренном треугольнике углы при основании равны");
        themes.add("В равнобедренном треугольнике биссектриса, проведенная к основанию, является медианой и высотой");
        themes.add("73");
        themes.add("Теорема 1");
        themes.add("Теорема 2");
        themes.add("Теорема 3");
        themes.add("Аксиома параллельных прямых");
        themes.add("Следствие 1");
        themes.add("Следствие 2");
        themes.add("74");
        themes.add("Теорема о сумме углов треугольника");
        themes.add("В любом треугольнике либо все углы острые, либо два угла острые, а третий тупой или прямой");
        themes.add("Теорема о соотношении между сторонами и углами треугольника");
        themes.add("Следствие 1");
        themes.add("Следствие 2");
        themes.add("Каждая сторона треугольника меньше суммы двух других сторон");
        themes.add("Свойство 1");
        themes.add("Свойство 2");
        themes.add("Свойство 3");
        themes.add("Признак 1");
        themes.add("Признак 2");
        themes.add("Теорема 1");
        themes.add("Теорема 2");
        themes.add("Теорема 3");
        themes.add("75");
        themes.add("8");

        theorems.add("7");
        theorems.add("71");
        theorems.add("");
        theorems.add("72");
        theorems.add("Если две стороны и угол между ними одного треугольника соответственно равны двум сторонам и углу между ними другого треугольника, то эти треугольники равны");
        theorems.add("Если сторона и два прилежащих к ней угла одного треугольника соответственно равны стороне и двум прилежащим к ней углам другого треугольника, то эти треугольники равны");
        theorems.add("Если три стороны одного треугольника соответственно равны трём сторонам другого треугольника, то эти треугольники равны");
        theorems.add("Из точки, не лежащей на прямой, можно провести только один перпендикуляр к этой прямой");
        theorems.add("");
        theorems.add("");
        theorems.add("");
        theorems.add("73");
        theorems.add("Если при пересечении двух прямых секущей накрест лежащие углы равны, то данные прямые параллельны");
        theorems.add("Если при пересечении двух прямых секущей соответственные углы равны, то прямые параллельны");
        theorems.add("Если при пересечении двух прямых секущей сумма односторонних углов равна 180, то прямые параллельны");
        theorems.add("Через точку, не лежащую на данной прямой, проходит только одна прямая, параллельная данной");
        theorems.add("Если прямая пересекает одну из двух параллельных прямых, то она пересекает и другую");
        theorems.add("Если две прямые параллельны третьей, то все три прямые параллельны");
        theorems.add("74");
        theorems.add("Сумма углов треугольника равно 180");
        theorems.add("");
        theorems.add("В треугольнике против большей стороны лежит больший угол и против большего угла лежит большая сторона");
        theorems.add("В прямоугольном треугольнике гипотенуза больше катета");
        theorems.add("Если два угла треугольника равны, то треугольник равнобедренный (признак равнобедренного треугольника)");
        theorems.add("");
        theorems.add("Сумма двух острых углов прямоугольного треугольника равна 90");
        theorems.add("Катет прямоугольного треугольника, лежащий напротив угла в 30, равен половине гипотенузы");
        theorems.add("Если катет прямоугольного треугольника равен половине гипотенузы, то угол, лежащий напротив этого катета, равен 30");
        theorems.add("Если катеты одного прямоугольного треугольника соответственно равны двум катетам другого прямоугольного треугольника, то эти треугольники равны");
        theorems.add("Если катет и прилежащий к нему острый угол одного прямоугольного треугольника соответственно равны катету и прилежащему к нему углу другого треугольника, то эти треугольники равны");
        theorems.add("Если гипотенуза и острый угол одного прямоугольного треугольника соответственно равны гипотенузы и острому углу другого прямоугольного треугольника, то эти треугольники равны");
        theorems.add("Если гипотенуза и катет одного прямоугольного треугольника соответственно равны гипотенузе и катету другого прямоугольного треугольника, то такие треугольники равны");
        theorems.add("Все точки каждой из двух параллельных прямых равноудалены от другой прямой");
        theorems.add("75");
        theorems.add("8");

        images.add(7);
        images.add(71);
        images.add(0);
        images.add(72);
        images.add(R.drawable.t_72_1);
        images.add(R.drawable.t_72_2);
        images.add(R.drawable.t_72_3);
        images.add(R.drawable.t_72_4);
        images.add(0);
        images.add(0);
        images.add(0);
        images.add(73);
        images.add(R.drawable.t_73_1);
        images.add(R.drawable.t_73_2);
        images.add(R.drawable.t_73_3);
        images.add(0);
        images.add(0);
        images.add(R.drawable.t_73_6);
        images.add(74);
        images.add(R.drawable.t_74_1);
        images.add(0);
        images.add(0);
        images.add(0);
        images.add(R.drawable.t_74_2);
        images.add(0);
        images.add(0);
        images.add(0);
        images.add(0);
        images.add(R.drawable.t_74_3);
        images.add(R.drawable.t_74_4);
        images.add(R.drawable.t_74_5);
        images.add(R.drawable.t_74_6);
        images.add(R.drawable.t_74_7);
        images.add(75);
        images.add(8);
    }

    public List<String> getThemes(int form) {
        int f = themes.indexOf(String.valueOf(form));
        int f1 = themes.indexOf(String.valueOf(form + 1));
        return themes.subList(f + 1, f1);
    }

    public List<Integer> getImages(int form) {
        int f = images.indexOf(form);
        int f1 = images.indexOf(form + 1);
        return images.subList(f + 1, f1);
    }

    public List<String> getTheorems(int form) {
        int f = theorems.indexOf(String.valueOf(form));
        int f1 = theorems.indexOf(String.valueOf(form + 1));
        return theorems.subList(f + 1, f1);
    }

    public List<String> getThemes(int form, int theme) {
        int f = themes.indexOf(String.valueOf(form) + String.valueOf(theme));
        int f1 = themes.indexOf(String.valueOf(form) + String.valueOf(theme + 1));
        return themes.subList(f + 1, f1);
    }

    public List<Integer> getImages(int form, int theme) {
        int f = images.indexOf(form * 10 + theme);
        int f1 = images.indexOf(form * 10 + theme + 1);
        return images.subList(f + 1, f1);
    }

    public List<String> getTheorems(int form, int theme) {
        int f = theorems.indexOf(String.valueOf(form) + String.valueOf(theme));
        int f1 = theorems.indexOf(String.valueOf(form) + String.valueOf(theme + 1));
        return theorems.subList(f + 1, f1);
    }

    public ArrayList<String> getThemes() {
        return themes;
    }

    public ArrayList<String> getTheorems() {
        return theorems;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

}
