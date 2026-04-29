package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        /// get score, topic and quiz history from intent
        int score = getIntent().getIntExtra("score", 0);
        String topic = getIntent().getStringExtra("topic");
        String quizHistory = getIntent().getStringExtra("quizHistory");
        int userId = getIntent().getIntExtra("userId", -1);

        //adding my views
        TextView summaryText = findViewById(R.id.summaryText);
        Button homeButton = findViewById(R.id.homeButton);
        TextView finalscoreText = findViewById(R.id.finalscoreText);
        TextView displayFinalScore = findViewById(R.id.displayFinalScore);

        // display score and topic
        finalscoreText.setText("Final Score:");
        displayFinalScore.setText(score + "/3");

        //generate AI summary
        QuizGenerator.generateSummary(topic, score, quizHistory, new QuizGenerator.QuestionCallback() {
            public void onQuestion(String question) {
                runOnUiThread(() -> summaryText.setText(question));
            }

            public void onError(String error) {
                runOnUiThread(() -> summaryText.setText("Great effort on your quiz - summary cannot be generated right now"));
            }

            ;
        });

        //navigate back to home
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        ;
        });

    }
}
