package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

    public class LoginActivity extends AppCompatActivity {

        private EditText usernameText;
        private EditText passwordText;
        private AppDatabase db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            // TODO: initialise the database instance

            db = AppDatabase.getInstance(this);

            // link your EditTexts and Buttons to their XML ids
            usernameText = findViewById(R.id.usernameText);
            passwordText = findViewById(R.id.passwordText);

            // set an onClickListener on loginButton
            Button loginButton = findViewById(R.id.loginButton);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = usernameText.getText().toString();
                    String password = passwordText.getText().toString();

                    Executors.newSingleThreadExecutor().execute(() -> {
                        User user = db.userDAO().login(username, password);
                        runOnUiThread(() -> {
                            if (user != null) {
                                navigateToInterests(user.id);
                            } else {
                                Toast.makeText(com.example.llm_project.LoginActivity.this, "Please check your login data again!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    });

                }
            });

            // TODO: set an onClickListener on signupButton
            Button signupButton = findViewById(R.id.signupButton);
            signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.example.llm_project.LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            }
            );

        }

        private void navigateToInterests(int userId) {
            // TODO: create an Intent to go to your InterestSelectionActivity
            Intent intent = new Intent(com.example.llm_project.LoginActivity.this, HomeActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
        ;
    }