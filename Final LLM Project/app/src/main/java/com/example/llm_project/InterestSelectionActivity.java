package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class InterestSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_selection);

        //get userId passed from SignupActivity
        int userId = getIntent().getIntExtra("userId", -1);

        // create the list of preset topics
        List<String> topics = new ArrayList<>();
        topics.add("History");
        topics.add("Math");
        topics.add("Sports");
        topics.add("English Literature");
        topics.add("Science");
        topics.add("Music");

        // create the adapter
        TopicAdapter adapter = new TopicAdapter(topics);

        // set up the RecyclerView with StaggeredGridLayoutManager
        RecyclerView recyclerView = findViewById(R.id.topicsRecyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //on clicklistener
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            List<String> selectedtopics = adapter.getSelectedTopics();
            if (selectedtopics.isEmpty()) {
                Toast.makeText(this, "You haven't selected anything!", Toast.LENGTH_SHORT).show();
            } else { //running everything on a background thread
                AppDatabase db = AppDatabase.getInstance(this);
                Executors.newSingleThreadExecutor().execute(() -> {
                    for (String topic : selectedtopics) {
                        UserInterest interest = new UserInterest();
                        interest.userId = userId;
                        interest.topic = topic;
                        db.userInterestDao().insert(interest);
                    }
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Interests saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    });
                });
            }

        });
    }
}
