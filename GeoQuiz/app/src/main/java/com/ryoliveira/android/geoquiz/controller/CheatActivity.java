package com.ryoliveira.android.geoquiz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ryoliveira.android.geoquiz.R;

public class CheatActivity extends AppCompatActivity {

    private final static String EXTRA_ANSWER_IS_TRUE = "com.ryoliveira.android.geoquiz.answer_is_true";
    private final static String EXTRA_ANSWER_SHOWN = "com.ryoliveira.android.geoquiz.answer_shown";
    private final static String KEY_ANSWER_SHOWN = "answerShown";

    private final static String API_VERSION_TEXT = "API Level " + Build.VERSION.SDK_INT;

    private boolean answerIsTrue = false;

    private TextView answerTextView;
    private TextView apiVersionTextView;
    private Button showAnswerButton;
    private boolean isAnswerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
            isAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN);
            setAnswerShownResults(isAnswerShown);
        }
        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        answerTextView = findViewById(R.id.answer_text_view);
        showAnswerButton = findViewById(R.id.show_answer_button);
        showAnswerButton.setOnClickListener(view -> {
            int answerText = (answerIsTrue) ? R.string.true_button : R.string.false_button;
            answerTextView.setText(answerText);
            setAnswerShownResults(true);
        });

        apiVersionTextView = findViewById(R.id.text_view_api_version);
        apiVersionTextView.setText(API_VERSION_TEXT);
    }

    public static Intent createAnswerIntent(Context packageContext, boolean answerIsTrue){
        return new Intent(packageContext, CheatActivity.class).putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
    }

    private void setAnswerShownResults(boolean isAnswerShown){
        this.isAnswerShown = isAnswerShown;
        Intent data = new Intent().putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(Activity.RESULT_OK, data);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANSWER_SHOWN, isAnswerShown);
    }
}