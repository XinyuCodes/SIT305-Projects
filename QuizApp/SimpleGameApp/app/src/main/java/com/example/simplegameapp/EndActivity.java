package com.example.simplegameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    TextView endText;
    Button finishExit, retakeQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_activity);

        int finalScore = getIntent().getIntExtra("finalScore", 0);
        String playerName = getIntent().getStringExtra("playerName");
        endText = findViewById(R.id.endText);
        finishExit = findViewById(R.id.finishExit);
        retakeQuiz = findViewById(R.id.retakeQuiz);

        endText.setText("Well done " + playerName + " you scored " + finalScore);
        finishExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finishAffinity();
            }
        });
        retakeQuiz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent restartIntent = new Intent(EndActivity.this, WelcomeActivity.class);
                restartIntent.putExtra("playerName", playerName);
                startActivity(restartIntent);
            }
        });

    }

}