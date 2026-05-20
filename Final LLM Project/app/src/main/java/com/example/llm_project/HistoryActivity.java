package com.example.llm_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        userId = getIntent().getIntExtra("userId", -1);

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadHistory();
    }

    private void loadHistory() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            //getting the user's test history
            List<QuizQuestion> questions = db.quizQuestionDao().getUserTestHistory(userId); // replace this
            runOnUiThread(() -> {
                if (questions != null) {
                    HistoryAdapter adapter = new HistoryAdapter(questions);
                    recyclerView.setAdapter(adapter);
                }
            });

        });
    }
}