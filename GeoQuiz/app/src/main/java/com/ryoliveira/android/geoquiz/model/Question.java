package com.ryoliveira.android.geoquiz.model;


public class Question {

    private int questionResId;
    private boolean answer;


    public Question(int questionResId, boolean answer) {
        this.questionResId = questionResId;
        this.answer = answer;
    }

    public int getQuestionResId() {
        return questionResId;
    }

    public void getQuestionResId(int questionResId) {
        this.questionResId = questionResId;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
