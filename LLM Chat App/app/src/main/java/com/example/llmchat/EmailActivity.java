package com.example.llmchat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views - using the updated IDs from activity_main.xml
        emailInput = findViewById(R.id.emailEditText);
        loginButton = findViewById(R.id.loginButton);

        // Set click listener on button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email from input
                // Fix: use getText().toString() instead of toString()
                String email = emailInput.getText().toString().trim();

                // Validate it's not empty
                if (email.isEmpty()) {
                    // Show error
                    Toast.makeText(EmailActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    // Create intent and pass email to ChatActivity
                    Intent intent = new Intent(EmailActivity.this, ChatActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });
    }
}
