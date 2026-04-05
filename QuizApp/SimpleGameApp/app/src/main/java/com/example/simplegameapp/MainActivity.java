package com.example.simplegameapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvQuestion, tvResult, tvProgress;

    Button btnOption1, btnOption2, btnNext;
    ProgressBar progressBar;
    Question[] questions = {
            new Question("What is the capital of France?", new String[]{"Paris", "London"}, 0),
            new Question("What is the capital of United Kingdom?", new String[]{"Paris", "London"}, 1),
            new Question("What is the capital of US?", new String[]{"Washington DC", "New York"}, 0)
    };
    int currentQuestionIndex = 0;
    int displayQuestion = 1;
    int score = 0;
    private void loadQuestion(){
        Question currentQuestion = questions[currentQuestionIndex];
        tvQuestion.setText(currentQuestion.questionText);
        btnOption1.setText(currentQuestion.answers[0]);
        btnOption2.setText(currentQuestion.answers[1]);
        btnOption1.setEnabled(true);
        btnOption2.setEnabled(true);
    };
    private void checkAnswer(int selectedIndex){
        Question currentQuestion = questions[currentQuestionIndex];
        if(selectedIndex==currentQuestion.correctAnswerIndex)
        {
            score ++;
            tvResult.setText("Correct! Your score is " + score);
            btnOption1.setEnabled(false);
            btnOption2.setEnabled(false);
        }
        else
        {
            tvResult.setText("Wrong! Your score is: " + score);
            btnOption1.setEnabled(false);
            btnOption2.setEnabled(false);
        }
    }

    private void changeColour(){
        Question currentQuestion = questions[currentQuestionIndex];
        if(currentQuestion.correctAnswerIndex==0){
            btnOption1.setBackgroundColor(Color.GREEN);
            btnOption2.setBackgroundColor(Color.RED);
        }
        else
        {
            btnOption1.setBackgroundColor(Color.RED);
            btnOption2.setBackgroundColor(Color.GREEN);
        }
    }

    private void moveNext(){
        //reset option color
        btnOption1.setBackgroundColor(Color.MAGENTA);
        btnOption2.setBackgroundColor(Color.MAGENTA);
        //increment index
        currentQuestionIndex++;
        if(currentQuestionIndex < 3)
        {
            displayQuestion++;
        }
        //update progress
        progressBar.setProgress(currentQuestionIndex);
        tvProgress.setText(displayQuestion + "/" + 3);

        //load the next question
        if(currentQuestionIndex < questions.length){
            loadQuestion();
        }
        else{
            String playerName = getIntent().getStringExtra("playerName");
            Intent finishIntent = new Intent(MainActivity.this, EndActivity.class);
            finishIntent.putExtra("finalScore", score);
            finishIntent.putExtra("playerName", playerName);
            startActivity(finishIntent);
        }
    }
;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String playerName = getIntent().getStringExtra("playerName");

        tvQuestion = findViewById(R.id.tvQuestion);
        tvResult = findViewById(R.id.tvResult);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnNext = findViewById(R.id.btnNext);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.tvProgress);

        //setting up progress bar
        progressBar.setMax(3);
        progressBar.setProgress(0);
        progressBar.setProgress(currentQuestionIndex);
        tvProgress.setText(displayQuestion + "/" + 3);
        //doing the initial load of questions
        loadQuestion();
        //main part of quiz
        btnOption1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(0);
                changeColour();
            }
        })
        ;
        btnOption2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(1);
                changeColour();
            }
        })
        ;

        //clicking next
        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moveNext();
            }
        });
    }
}