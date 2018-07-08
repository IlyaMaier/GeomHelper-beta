package ru.project.geomhelper.resources;

import android.graphics.Color;

import ru.project.geomhelper.R;

public class Resources {

    public int[] colors = new int[6];
    public int[] colorsTests = new int[6];
    public int[] background = new int[4];

    public Resources() {

        colors[5] = Color.parseColor("#00c853");
        colors[4] = Color.parseColor("#3ec73e");
        colors[3] = Color.parseColor("#67c734");
        colors[2] = Color.parseColor("#8fc425");
        colors[1] = Color.parseColor("#b9c418");
        colors[0] = Color.parseColor("#d9c10d");

        colorsTests[0] = Color.parseColor("#faad07");
        colorsTests[1] = Color.parseColor("#f59314");
        colorsTests[2] = Color.parseColor("#f07c1d");
        colorsTests[3] = Color.parseColor("#ed6724");
        colorsTests[4] = Color.parseColor("#e8522c");
        colorsTests[5] = Color.parseColor("#e64732");

        background[3] = R.drawable.background_test_4;
        background[2] = R.drawable.background_test_3;
        background[1] = R.drawable.background_test_2;
        background[0] = R.drawable.background_test_1;

    }

}
