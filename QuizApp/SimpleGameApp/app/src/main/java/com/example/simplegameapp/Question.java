package com.example.simplegameapp;

public class Question {
    String questionText;
    String[] answers;
    int correctAnswerIndex;

    public Question(String questionText, String[] answers, int correctAnswerIndex)
    {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
