package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    TextView tvUsername, tvLatestScore, tvtotalquestionsnumber;
    Button btnShare, btnHistory, btnPurchase, StartNewQuizButton;
    int userId;
    String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting IDs
        userId = getIntent().getIntExtra("userId", -1);
        firstName = getIntent().getStringExtra("firstName");

        //get views
        tvUsername = findViewById(R.id.nameText);
        tvLatestScore = findViewById(R.id.correctAnswersLastQuiz);
        tvtotalquestionsnumber = findViewById(R.id.totalquestionsnumber);
        btnShare = findViewById(R.id.shareButton2);
        btnHistory = findViewById(R.id.viewQuizHistory);
        btnPurchase = findViewById(R.id.upgradePlan);
        StartNewQuizButton = findViewById(R.id.StartNewQuizButton);

        //set up user name
        tvUsername.setText(firstName);

        loadLatestSession();

        btnShare.setOnClickListener(v -> shareResults());

        btnHistory.setOnClickListener(v -> {
            // TODO: navigate to HistoryActivity, pass userId
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnPurchase.setOnClickListener(v -> {
            // TODO: navigate to PurchaseActivity, pass userId
            Intent intent = new Intent(this, PurchaseActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        StartNewQuizButton.setOnClickListener(v -> {
            // TODO: navigate to PurchaseActivity, pass userId
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });


    }

    private void loadLatestSession() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // TODO: query quizSessionDao for the latest session by userId
            // hint: you'll want something like getLatestSession(userId)
            QuizSession latest = db.quizSessionDao().getLatestSession(userId); // replace this

            QuizSession finalLatest = latest;
            runOnUiThread(() -> {
                if (finalLatest != null) {
                    // TODO: set tvLatestTopic and tvLatestScore text
//                    tvLatestTopic.setText(finalLatest.topic);
                    tvtotalquestionsnumber.setText(String.valueOf(3));
                    tvLatestScore.setText(String.valueOf(finalLatest.score));
                } else {
//                    tvLatestTopic.setText("No quiz taken yet");
                    tvLatestScore.setText("0");
                }
            });
        });
    }

    private void shareResults() {
        // TODO: build a share message string using the latest session data
        // hint: "I scored X/Y on a {topic} quiz!"
        String shareText = "Hey, I just found a super awesome app, you should try this!"; // replace this

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Share your result via"));
    }
}