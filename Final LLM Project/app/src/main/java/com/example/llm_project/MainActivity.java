package com.example.llm_project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        questionTextView = findViewById(R.id.questionTextView);

        // Use the static method with the topic
        QuizGenerator.generateQuestion("sports", new QuizGenerator.QuestionCallback() {
            @Override
            public void onQuestion(String question) {
                runOnUiThread(() -> questionTextView.setText(question));
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> questionTextView.setText("Error: " + error));
            }
        });
    }
}