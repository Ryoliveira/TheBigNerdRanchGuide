package com.ryoliveira.android.geoquiz.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ryoliveira.android.geoquiz.R;
import com.ryoliveira.android.geoquiz.model.Question;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;

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
            increaseIndex();
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
            increaseIndex();
            updateQuestion();
        });

        prevButton.setOnClickListener(view -> {
            decreaseIndex();
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
        int questionTextResId = questionList.get(currentIndex).getQuestionResId();
        questionTextView.setText(questionTextResId);
        if(isAnswered[currentIndex]){ // If question was already answered, disable true/false buttons
            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        }else{
            trueButton.setEnabled(true);
            falseButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean answer){
        int messageResId;

        boolean correctAnswer = questionList.get(currentIndex).isAnswer();
        if(answer == correctAnswer){
            messageResId = R.string.correct_toast;
            totalCorrect++;
        }else{
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        isAnswered[currentIndex] = true; // Mark question index as answered
        totalAnswered++;
        //Disable Buttons for current question
        trueButton.setEnabled(false);
        falseButton.setEnabled(false);
        if(totalAnswered == questionList.size()) { // If all questions are answered, display percentage
            displayFinalScorePercentage();         // answered correctly
        }
    }

    private void increaseIndex(){
        currentIndex = (currentIndex + 1) % questionList.size();
    }

    private void decreaseIndex(){
        currentIndex = ((currentIndex - 1) + questionList.size()) % questionList.size();
    }

    private void displayFinalScorePercentage(){
        float totalPercentCorrect = (totalCorrect / questionList.size()) * 100f;
        Toast.makeText(this, String.format(Locale.getDefault(), "Score: %.2f%%", totalPercentCorrect), Toast.LENGTH_SHORT).show();
    }

}