package ru.project.geomhelper.content;

public class Achievement {

    private int image, progress, max;
    private String name, bonus;

    public Achievement(int image, int progress, int max, String name, String bonus) {
        this.image = image;
        this.progress = progress;
        this.max = max;
        this.name = name;
        this.bonus = bonus;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
}
