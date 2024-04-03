package com.example.triviagame;

class Question {
    private String text;
    private String[] options;
    private int correctAnswerIndex;
    private String imagePath; // Optional image path

    // Constructor
    public Question(String text, String[] options, int correctAnswerIndex, String imagePath) {
        this.text = text;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.imagePath = imagePath;
    }

    // Getters
    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getImagePath() {
        return imagePath;
    }
}