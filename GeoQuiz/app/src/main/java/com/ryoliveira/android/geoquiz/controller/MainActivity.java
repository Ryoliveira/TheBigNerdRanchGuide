package com.ryoliveira.android.geoquiz.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ryoliveira.android.geoquiz.R;
import com.ryoliveira.android.geoquiz.viewmodel.QuizViewModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final String KEY_INDEX = "index";
    private final String KEY_ANSWERED_QUESTIONS = "answeredQuestions";
    private final String KEY_TOTAL_ANSWERED = "totalQuestionsAnswered";
    private final String KEY_TOTAL_CORRECT = "totalCorrect";
    private final static String EXTRA_ANSWER_SHOWN = "com.ryoliveira.android.geoquiz.answer_shown";
    private final String KEY_TIMES_CHEATED = "timesCheated";

    private final int REQUEST_CODE_CHEAT = 0;

    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;

    private QuizViewModel getQuizViewModel(){
        return ViewModelProviders.of(this).get(QuizViewModel.class);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle)");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            loadDataFromSavedInstanceState(savedInstanceState);
        }

        //Widgets
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        cheatButton = findViewById(R.id.cheat_button);
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

        cheatButton.setOnClickListener(view -> {
            //start cheat activity
            boolean answerIsTrue = getQuizViewModel().getQuestionAnswer();
            Intent intent = CheatActivity.createAnswerIntent(this, answerIsTrue);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ActivityOptions options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.getWidth(), view.getHeight());
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle());
            }else{
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState()");
        outState.putInt(KEY_INDEX, getQuizViewModel().getCurrentIndex());
        outState.putBooleanArray(KEY_ANSWERED_QUESTIONS, getQuizViewModel().getIsAnswered());
        outState.putInt(KEY_TOTAL_ANSWERED, getQuizViewModel().getTotalAnswered());
        outState.putFloat(KEY_TOTAL_CORRECT, getQuizViewModel().getTotalCorrect());
        outState.putInt(KEY_TIMES_CHEATED, getQuizViewModel().getTimesCheated());
    }

    private void updateQuestion(){
        int questionTextResId = getQuizViewModel().getCurrentQuestionResId();
        questionTextView.setText(questionTextResId);
        if(getQuizViewModel().isCurrentQuestionAnswered()){ // If question was already answered, disable true/false buttons
            setAnswerButtonsActiveState(false);
            cheatButton.setEnabled(false);
            if(getQuizViewModel().didUserCheatOnCurrentQuestion()){
                Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show();
            }
        }else{
            setAnswerButtonsActiveState(true);
            if(getQuizViewModel().getTimesCheated() >= 3){
                cheatButton.setEnabled(false);
            }else{
                cheatButton.setEnabled(true);
            }
        }
    }

    private void checkAnswer(boolean answer){
        int messageResId;

        boolean correctAnswer = getQuizViewModel().getQuestionAnswer();
        if(getQuizViewModel().didUserCheatOnCurrentQuestion()){
            messageResId = R.string.judgment_toast;
        }
        else if(answer == correctAnswer){
            messageResId = R.string.correct_toast;
            getQuizViewModel().increaseTotalCorrect();
        }else{
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        getQuizViewModel().markCurrentQuestionAsAnswered(); // Mark question index as answered
        getQuizViewModel().increaseTotalAnswered(); //Increase total number of questions answered
        setAnswerButtonsActiveState(false); //Disable Buttons for current question
        if(getQuizViewModel().getTotalAnswered() == getQuizViewModel().getQuestionListSize()) { // If all questions are answered, display percentage
            displayFinalScorePercentage();                                            // answered correctly
        }
    }

    private void setAnswerButtonsActiveState(boolean active){
        trueButton.setEnabled(active);
        falseButton.setEnabled(active);
    }

    private void displayFinalScorePercentage(){
        Toast.makeText(this, String.format(Locale.getDefault(), "Score: %.2f%%", getQuizViewModel().getTotalPercentCorrect()), Toast.LENGTH_SHORT).show();
    }

    private void loadDataFromSavedInstanceState(Bundle savedInstanceState) {
        QuizViewModel quizViewModel = getQuizViewModel();
        quizViewModel.setCurrentIndex(savedInstanceState.getInt(KEY_INDEX));
        quizViewModel.setIsAnswered(savedInstanceState.getBooleanArray(KEY_ANSWERED_QUESTIONS));
        quizViewModel.setTotalAnswered(savedInstanceState.getInt(KEY_TOTAL_ANSWERED));
        quizViewModel.setTotalCorrect(savedInstanceState.getFloat(KEY_TOTAL_CORRECT));
        quizViewModel.setTimesCheated(savedInstanceState.getInt(KEY_TIMES_CHEATED));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            boolean isCheater = data.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
            getQuizViewModel().markCheatedQuestion(isCheater);
            if(isCheater) getQuizViewModel().increaseTimesCheated();
        }
    }
}