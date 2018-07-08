package ru.project.geomhelper;

import java.util.ArrayList;

import ru.project.geomhelper.content.Course;

public class Person {

    public static final String APP_PREFERENCES = "mySettings";

    public static String name;
    public static long id = 0L;
    public static String uId;
    public static String currentLevel = "Неволшебник-младший";
    public static int experience = 0;
    public static int currentLevelExperience = 50;
    public static int leaderBoardPlace = 0;
    public static ArrayList<Course> courses = new ArrayList<>();
    public static String c = "";
    public static Course currentCourse;
    public static int currentTheme = 0;
    public static int currentTest;
    public static int currentTestTheme = 0;
    public static int task = 0;
    public static short backTests = 0;
    public static short backCourses = 0;

}
