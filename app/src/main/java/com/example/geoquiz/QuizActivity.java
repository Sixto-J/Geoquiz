package com.example.geoquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
//import androidx.core.graphics.Insets; import androidx.core.view.ViewCompat; import androidx.core.view.WindowInsetsCompat;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWERED = "answered";
    private ImageButton mTrueButton;
    private ImageButton mFalseButton;
    private TextView mQuestionTextView;

    private ArrayList<Integer> mAnsweredQuestions = new ArrayList<>();

    private final Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mNumberOfCorrectAnswers = 0;
    private int mNumberOfIncorrectAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnsweredQuestions = savedInstanceState.getIntegerArrayList(KEY_ANSWERED);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(v -> {
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false); /*Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();*/
            checkAnswer(true);
            mAnsweredQuestions.add(mCurrentIndex); //Does nothing yet, but soon!
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(v -> {
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false); /* Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();*/
            checkAnswer(false);
            mAnsweredQuestions.add(mCurrentIndex); // Does nothing yet, but soon!
        });

        ImageButton mPrevButton = findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex > 0){
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                }else{
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                updateQuestion();
            }
        });

        ImageButton mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putIntegerArrayList(KEY_ANSWERED, mAnsweredQuestions );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if(mAnsweredQuestions.contains(mCurrentIndex)) {
            mTrueButton.setClickable(false);
            mFalseButton.setClickable(false);
        } else {
            mTrueButton.setClickable(true);
            mFalseButton.setClickable(true);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        if (userPressedTrue == answerIsTrue) {
            mNumberOfCorrectAnswers += 1;
            messageResId = R.string.correct_toast;
        } else {
            mNumberOfIncorrectAnswers += 1;
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if((mNumberOfCorrectAnswers + mNumberOfIncorrectAnswers) == mQuestionBank.length) {
            double percentageOfCorrectAnswers = ((double)mNumberOfCorrectAnswers/(double)mQuestionBank.length) * 100;
            Toast.makeText(
                    QuizActivity.this,
                    getString(R.string.amount_of_correct_answers) + mNumberOfCorrectAnswers + "\n" +
                            getString(R.string.percentage_of_correct_answers, percentageOfCorrectAnswers),
                    Toast.LENGTH_LONG).show();
        }
    }
}