package ru.project.geomhelper.content;

public class FirstTask {

    private String textViewName1, textViewTask1, radioButton, radioButton1, radioButton2;
    private int imageView1, correctAnswer;

    FirstTask(String textViewTask1, int imageView1,
              String radioButton, String radioButton1,
              String radioButton2, int correctAnswer) {
        this.textViewTask1 = textViewTask1;
        this.imageView1 = imageView1;
        this.radioButton = radioButton;
        this.radioButton1 = radioButton1;
        this.radioButton2 = radioButton2;
        this.correctAnswer = correctAnswer;
    }

    public String getTextViewName1() {
        return textViewName1;
    }

    public void setTextViewName1(String textViewName1) {
        this.textViewName1 = textViewName1;
    }

    public String getTextViewTask1() {
        return textViewTask1;
    }

    public void setTextViewTask1(String textViewTask1) {
        this.textViewTask1 = textViewTask1;
    }

    public int getImageView1() {
        return imageView1;
    }

    public void setImageView1(int imageView1) {
        this.imageView1 = imageView1;
    }

    public String getRadioButton() {
        return radioButton;
    }

    public void setRadioButton(String radioButton) {
        this.radioButton = radioButton;
    }

    public String getRadioButton1() {
        return radioButton1;
    }

    public void setRadioButton1(String radioButton1) {
        this.radioButton1 = radioButton1;
    }

    public String getRadioButton2() {
        return radioButton2;
    }

    public void setRadioButton2(String radioButton2) {
        this.radioButton2 = radioButton2;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}