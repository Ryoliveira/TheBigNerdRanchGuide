package com.ryoliveira.android.geoquiz.viewmodel;

import androidx.lifecycle.ViewModel;

import com.ryoliveira.android.geoquiz.R;
import com.ryoliveira.android.geoquiz.model.Question;

import java.util.Arrays;
import java.util.List;

public class QuizViewModel extends ViewModel {

    private final String TAG = "QuizViewModel";

    private List<Question> questionList = Arrays.asList(
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true));

    private boolean[] isAnswered = new boolean[questionList.size()];

    private int currentIndex = 0;
    private int totalAnswered = 0;
    private float totalCorrect = 0;


    public boolean isCurrentQuestionAnswered() {
        return isAnswered[currentIndex];
    }

    public void markCurrentQuestionAsAnswered() {
        this.isAnswered[currentIndex] = true;
    }

    public int getCurrentQuestionResId(){
        return questionList.get(currentIndex).getQuestionResId();
    }

    public boolean getQuestionAnswer(){
        return questionList.get(currentIndex).getAnswer();
    }

    public boolean[] getIsAnswered(){
        return isAnswered;
    }

    public int getCurrentIndex(){
        return currentIndex;
    }

    public void setCurrentIndex(int index){
        this.currentIndex = index;
    }

    public void increaseCurrentIndex() {
        this.currentIndex = (currentIndex + 1) % questionList.size();
    }

    public void decreaseCurrentIndex(){
        currentIndex = ((currentIndex - 1) + questionList.size()) % questionList.size();
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered){
        this.totalAnswered = totalAnswered;
    }

    public void increaseTotalAnswered() {
        this.totalAnswered++;
    }

    public float getTotalCorrect() {
        return totalCorrect;
    }

    public void increaseTotalCorrect() {
        this.totalCorrect++;
    }

    public int getQuestionListSize() {
        return questionList.size();
    }

    public double getTotalPercentCorrect(){
        return (totalCorrect / questionList.size()) * 100f;
    }
}
