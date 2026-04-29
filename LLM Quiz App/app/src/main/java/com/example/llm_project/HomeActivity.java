package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private TopicAdapter adapter;
    private AppDatabase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // TODO: get userId from Intent

        userId = getIntent().getIntExtra("userId", -1);
        String firstName = getIntent().getStringExtra("firstName");

        db = AppDatabase.getInstance(this);

        // TODO: get the username from Room and set it on namePlaceHolder TextView
        // remember to use a background thread!
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDAO().getUserById(userId);
//            List<UserInterest> interests = db.userInterestDao().getInterestsForUser(userId);
//
//            List<String> topicNames = new ArrayList<>();
//            for (UserInterest interest : interests) {
//                topicNames.add(interest.topic);
//            }
            List<String> topicNames = db.userInterestDao().getDistinctTopicsForUser(userId);

            runOnUiThread(() -> {
                TextView namePlaceHolder = findViewById(R.id.namePlaceHolder);
                namePlaceHolder.setText(user.firstName);

                RecyclerView recyclerView = findViewById(R.id.topicsRecyclerView);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new TopicAdapter(topicNames);
                recyclerView.setAdapter(adapter);
            });
        });


        Button startQuizButton = findViewById(R.id.startQuizButton);
        startQuizButton.setOnClickListener(v -> {
            //get selected topics from adapter.getSelectedTopics()
            if(adapter.getSelectedTopics().isEmpty()){
                Toast.makeText(this, "Please select a topic!", Toast.LENGTH_SHORT).show();
            }
                else if(adapter.getSelectedTopics().size() > 1){
                    Toast.makeText(this, "Please select only one topic!", Toast.LENGTH_SHORT).show();}
                else
                {
                    String topic = adapter.getSelectedTopics().get(0);
                    Intent intent = new Intent(this, QuizActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("topic", topic);
                    startActivity(intent);
                }
        });
        Button addInterestButton = findViewById(R.id.addInterestButton);
        addInterestButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, InterestSelectionActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    }