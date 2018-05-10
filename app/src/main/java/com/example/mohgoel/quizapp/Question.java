package com.example.mohgoel.quizapp;

/**
 * Created by MOHGOEL on 08-May-18.
 */

public class Question {
    public enum QUESTION_TYPE {SINGLE, MULTIPLE, TEXT};

    private int id;
    private QUESTION_TYPE type;
    private String statement;
    private String[] options ;
    private String correctAnswer;
    private String userAnswer;
    private boolean isAttempted;
    private int  imageId;


    public Question(QUESTION_TYPE type) {
        this.type = type;
    }

    public QUESTION_TYPE getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Set 2 as correct answer index for Single choice question,
     * Set 3 as incorrect answer index for multiple choice questions.
     * @param correctAnswer
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
