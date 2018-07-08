package ru.project.geomhelper.content;

public class Achievements {

    private int courses = 0, tests = 0, fabs = 0, shared = 0;
    private boolean coursesB = false, testsB = false, fabsB = false, sharedB = false, xp = false;

    public Achievements() {
    }

    public int getCourses() {
        return courses;
    }

    public void setCourses(int courses) {
        this.courses = courses;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getFabs() {
        return fabs;
    }

    public void setFabs(int fabs) {
        this.fabs = fabs;
    }

    public int getShared() {
        return shared;
    }

    public void setShared(int shared) {
        this.shared = shared;
    }

    public boolean isCoursesB() {
        return coursesB;
    }

    public void setCoursesB(boolean coursesB) {
        this.coursesB = coursesB;
    }

    public boolean isTestsB() {
        return testsB;
    }

    public void setTestsB(boolean testsB) {
        this.testsB = testsB;
    }

    public boolean isFabsB() {
        return fabsB;
    }

    public void setFabsB(boolean fabsB) {
        this.fabsB = fabsB;
    }

    public boolean isSharedB() {
        return sharedB;
    }

    public void setSharedB(boolean sharedB) {
        this.sharedB = sharedB;
    }

    public boolean isXp() {
        return xp;
    }

    public void setXp(boolean xp) {
        this.xp = xp;
    }
}
