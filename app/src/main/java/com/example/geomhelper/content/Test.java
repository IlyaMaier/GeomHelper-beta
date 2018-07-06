package com.example.geomhelper.content;

public class Test {

    private String testName;
    private int numberOfThemes;
    private String[] themes;
    private int background;

    Test(String testName, int numberOfThemes, String[] themes, int background) {
        this.testName = testName;
        this.numberOfThemes = numberOfThemes;
        this.themes = themes;
        this.background = background;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getNumberOfThemes() {
        return numberOfThemes;
    }

    public void setNumberOfThemes(int numberOfThemes) {
        this.numberOfThemes = numberOfThemes;
    }

    public String getTheme(int i) {
        return themes[i];
    }

    public void setThemes(String theme, int i) {
        this.themes[i] = theme;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

}
