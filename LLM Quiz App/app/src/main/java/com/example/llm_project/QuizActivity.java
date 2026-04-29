package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity {

    private TextView questionText, questionLabel, scoreText, explainerText;
    private Button optionA, optionB, optionC, optionD, hintButton, nextQuestion;

    private AppDatabase db;
    private int userId;
    private String topic;

    private int currentQuestion = 1;
    private int score = 0;
    private String correctAnswer;

    // this will hold the full current question text for hint/explanation context
    private String currentQuestionFull;

    //holding all the quizhistory
    private StringBuilder quizHistory = new StringBuilder();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        userId = getIntent().getIntExtra("userId", -1);
        topic = getIntent().getStringExtra("topic");

        db = AppDatabase.getInstance(this);

        questionText = findViewById(R.id.questionText);
        questionLabel = findViewById(R.id.questionLabel);
        scoreText = findViewById(R.id.scoreText);
        explainerText = findViewById(R.id.explainerText);

        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        hintButton = findViewById(R.id.hintButton);
        nextQuestion = findViewById(R.id.nextQuestion);
        nextQuestion.setEnabled(false);
        loadQuestion();

        optionA.setOnClickListener(v -> checkAnswer("A"));
        optionB.setOnClickListener(v -> checkAnswer("B"));
        optionC.setOnClickListener(v -> checkAnswer("C"));
        optionD.setOnClickListener(v -> checkAnswer("D"));

        hintButton.setOnClickListener(v -> generateHint());

        nextQuestion.setOnClickListener(v->{
            if(currentQuestion<3)
            {
                nextQuestion.setEnabled(false);
                currentQuestion++;
                optionA.setText("A");
                optionB.setText("B");
                optionC.setText("C");
                optionD.setText("D");
                loadQuestion();
            }
            else
            {
                navigateToResults();
            }
        });


    }

    private void setAnswerButtonsEnabled(boolean enabled) {
        optionA.setEnabled(enabled);
        optionB.setEnabled(enabled);
        optionC.setEnabled(enabled);
        optionD.setEnabled(enabled);
    }
    private void loadQuestion() {
        questionLabel.setText("Question " + currentQuestion + "/3");
        questionText.setText("Loading question");
        explainerText.setText("");
        setAnswerButtonsEnabled(false);

        QuizGenerator.generateQuestion(topic, new QuizGenerator.QuestionCallback() {
            @Override
            public void onQuestion(String question) {
                runOnUiThread(() -> {
                    parseAndDisplayQuestion(question);
                    setAnswerButtonsEnabled(true); // move it in here
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> questionText.setText("Error loading question: " + error));
            }
        });

    }

    private void parseAndDisplayQuestion(String response) {
        currentQuestionFull = response;
        String[] lines = response.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("Question:")) {
                questionText.setText(trimmedLine.substring("Question:".length()).trim());
            } else if (trimmedLine.startsWith("A)")) {
                optionA.setText(trimmedLine);
            } else if (trimmedLine.startsWith("B)")) {
                optionB.setText(trimmedLine);
            } else if (trimmedLine.startsWith("C)")) {
                optionC.setText(trimmedLine);
            } else if (trimmedLine.startsWith("D)")) {
                optionD.setText(trimmedLine);
            } else if (trimmedLine.startsWith("Answer:")) {
                correctAnswer = trimmedLine.substring("Answer:".length()).trim();
                if (correctAnswer.contains(")")) {
                    correctAnswer = correctAnswer.split("\\)")[0].trim();
                }
            }
        }
    }

    private void checkAnswer(String selected) {
        setAnswerButtonsEnabled(false); //locking down all the answers
        if (selected.equalsIgnoreCase(correctAnswer)) {
            score++;
            scoreText.setText("Score: " + score);
            explainerText.setText("You are correct!");
            quizHistory.append("Q").append(currentQuestion)
                    .append(": ").append(currentQuestionFull)
                    .append(" - answered correctly .\n");
            nextQuestion.setEnabled(true);
        } else {
            quizHistory.append("Q").append(currentQuestion)
                    .append(": ").append(currentQuestionFull)
                    .append(" - answered incorrectly (choice").append(selected)
                    .append(", correct answer was ")
                    .append(correctAnswer).append(").\n");
            nextQuestion.setEnabled(true);
            generateExplanation(selected);
        }

    }

    private void generateHint() {
        explainerText.setText("Generating hint...");
        QuizGenerator.generateHint(currentQuestionFull, new QuizGenerator.QuestionCallback() {
            @Override
            public void onQuestion(String hint) {
                runOnUiThread(() -> {explainerText.setText(hint);
                nextQuestion.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> explainerText.setText("Error: " + error));
                nextQuestion.setVisibility(View.VISIBLE);
            }
        });
    }

    private void generateExplanation(String selected) {
        explainerText.setText("Incorrect! Generating explanation...");
        nextQuestion.setEnabled(false);
        QuizGenerator.generateExplanation(currentQuestionFull, selected, correctAnswer, new QuizGenerator.QuestionCallback() {
            @Override
            public void onQuestion(String explanation) {
                runOnUiThread(() -> {
                    explainerText.setText(explanation);
                    nextQuestion.setEnabled(true);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    explainerText.setText("Error: " + error);
                    // Still move on even if explanation fails
                    explainerText.postDelayed(() -> {
                        if (currentQuestion < 3) {
                            currentQuestion++;
                            loadQuestion();
                        } else {
                            navigateToResults();
                        }
                    }, 2000);
                });
            }
        });
    }

    private void navigateToResults() {
        executorService.execute(() -> {
            QuizSession session = new QuizSession();
            session.userId = userId;
            session.topic = topic;
            session.score = score;
            session.date = System.currentTimeMillis();
            db.quizSessionDao().insert(session);

            runOnUiThread(() -> {
                // navigating to resultsactivity class

                Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("topic", topic);
                intent.putExtra("score", score);
                intent.putExtra("quizHistory", quizHistory.toString());
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}