package com.ryoliveira.android.geoquiz.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ryoliveira.android.geoquiz.R;
import com.ryoliveira.android.geoquiz.viewmodel.QuizViewModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;

    private QuizViewModel getQuizViewModel(){
        return ViewModelProviders.of(this).get(QuizViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle)");
        setContentView(R.layout.activity_main);

        //Widgets
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questionTextView = findViewById(R.id.question_text_view);

        //Button Listeners
        questionTextView.setOnClickListener(view -> {
            getQuizViewModel().increaseCurrentIndex();
            updateQuestion();
        });

        trueButton.setOnClickListener( view -> {
            //This is where the code goes that you want to execute when the true button is clicked
            checkAnswer(true);
        });

        falseButton.setOnClickListener(view -> {
            checkAnswer(false);
        });

        nextButton.setOnClickListener(view -> {
            getQuizViewModel().increaseCurrentIndex();
            updateQuestion();
        });

        prevButton.setOnClickListener(view -> {
            getQuizViewModel().decreaseCurrentIndex();
            updateQuestion();
        });

        //Set initial question
        updateQuestion();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() Called");
    }

    private void updateQuestion(){
        int questionTextResId = getQuizViewModel().getCurrentQuestionResId();
        questionTextView.setText(questionTextResId);
        if(getQuizViewModel().isCurrentQuestionAnswered()){ // If question was already answered, disable true/false buttons
            setAnswerButtonsActiveState(false);
        }else{
            setAnswerButtonsActiveState(true);
        }
    }

    private void checkAnswer(boolean answer){
        int messageResId;

        boolean correctAnswer = getQuizViewModel().getQuestionAnswer();
        if(answer == correctAnswer){
            messageResId = R.string.correct_toast;
            getQuizViewModel().increaseTotalCorrect();
        }else{
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        getQuizViewModel().markCurrentQuestionAsAnswered(); // Mark question index as answered
        getQuizViewModel().increaseTotalAnswered(); //Increase total number of questions answered
        setAnswerButtonsActiveState(false); //Disable Buttons for current question
        Log.d(TAG, getQuizViewModel().getTotalAnswered()+"");
        if(getQuizViewModel().getTotalAnswered() == getQuizViewModel().getQuestionListSize()) { // If all questions are answered, display percentage
            displayFinalScorePercentage();                                            // answered correctly
        }
    }

    private void setAnswerButtonsActiveState(boolean active){
        trueButton.setEnabled(active);
        falseButton.setEnabled(active);
    }

    private void displayFinalScorePercentage(){
        float totalPercentCorrect = (getQuizViewModel().getTotalCorrect() / getQuizViewModel().getQuestionListSize()) * 100f;
        Toast.makeText(this, String.format(Locale.getDefault(), "Score: %.2f%%", totalPercentCorrect), Toast.LENGTH_SHORT).show();
    }

}