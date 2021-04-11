package com.ryoliveira.android.geoquiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ryoliveira.android.geoquiz.R;
import com.ryoliveira.android.geoquiz.model.Question;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    private int currentIndex = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Widgets
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questionTextView = findViewById(R.id.question_text_view);

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



        updateQuestion();
    }

    private void updateQuestion(){
        int questionTextResId = questionList.get(currentIndex).getQuestionResId();
        questionTextView.setText(questionTextResId);
    }

    private void checkAnswer(boolean answer){
        boolean correctAnswer = questionList.get(currentIndex).isAnswer();
        int messageResId = (answer == correctAnswer) ? R.string.correct_toast : R.string.incorrect_toast;
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void increaseIndex(){
        currentIndex = (currentIndex + 1) % questionList.size();
    }

    private void decreaseIndex(){
        currentIndex = ((currentIndex - 1) + questionList.size()) % questionList.size();
    }
}