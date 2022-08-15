package com.bignerdranch.android.geoquiz;

public class UserAnswer {
    private static int correctNum;
    private Boolean answer;

    public UserAnswer(Boolean answer) {
        this.answer = answer;
    }


    public static int getCorrectNum() {
        return correctNum;
    }

    public static void incCorrectNum() {
        UserAnswer.correctNum++;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }
}
