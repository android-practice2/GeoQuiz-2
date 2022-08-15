package com.bignerdranch.android.geoquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private final Map<Question,UserAnswer> userAnswerMap =new HashMap<>(mQuestionBank.length);
    private static boolean mIsCheater=false;
    private TextView remain_tokens_text_view;
    private static int remain_tokens=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() is called");
        setContentView(R.layout.activity_main);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CheatActivity.newIntent(MainActivity.this, MainActivity.this.mQuestionBank[MainActivity.this.mCurrentIndex].isAnswer());
                remain_tokens--;
                if (remain_tokens <= 0) {
                    mCheatButton.setEnabled(false);
                }
                refreshRemainToken();

                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        showQuestion();

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mCurrentIndex;
                showQuestion();

            }
        });
        mNPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mNPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --mCurrentIndex;
                if (mCurrentIndex < 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                showQuestion();

            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mCurrentIndex;
                showQuestion();

            }
        });
        remain_tokens_text_view = findViewById(R.id.remain_tokens_text_view);
        refreshRemainToken();

    }

    private void refreshRemainToken() {
        remain_tokens_text_view.setText(remain_tokens+"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }



    private void disableAnswerButton() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }
    private void enableAnswerButton() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void showQuestion() {
        Question question = mQuestionBank[mCurrentIndex % mQuestionBank.length];
        UserAnswer userAnswer = userAnswerMap.get(question);
        if (userAnswer != null) {
            disableAnswerButton();
        }else {
            enableAnswerButton();
        }
        int textId = question.getTextResId();
        mQuestionTextView.setText(textId);
    }

    private void checkAnswer(boolean userAnswer) {
        disableAnswerButton();
        if (mIsCheater) {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show();
        }

        Question question = mQuestionBank[mCurrentIndex % mQuestionBank.length];
        UserAnswer value = new UserAnswer(userAnswer);
        userAnswerMap.put(question, value);
        boolean answer = question.isAnswer();
        if (answer == userAnswer) {
            UserAnswer.incCorrectNum();
            Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
        }
        if (userAnswerMap.size() == mQuestionBank.length) {
            Toast.makeText(this, "score: "+UserAnswer.getCorrectNum()+"/"+mQuestionBank.length, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() is called");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() is called");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() is called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() is called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() is called");
    }
}