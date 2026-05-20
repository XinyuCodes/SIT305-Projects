package com.example.llm_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText EditPasswordSignup;
    private EditText firstNameEditText;
    private EditText confirmPasswordEditText;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // TODO: initialise the database instance
        db = AppDatabase.getInstance(this);

        // TODO: link your EditTexts and Button to their XML ids
        usernameEditText = findViewById(R.id.usernameTextSignup);
        EditPasswordSignup = findViewById(R.id.EditPasswordSignup);
        firstNameEditText = findViewById(R.id.firstNameSignup);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordSignup);


        // TODO: set an onClickListener on createAccount button
        // Inside it:
        //   - get username and password from the EditTexts
        //   - check neither field is empty, show a Toast if they are
        //   - run in a background thread:
        //       - call findByUsername() to check if username is taken
        //       - if taken, show a Toast saying "Username already taken"
        //       - if not taken, create a new User object, set its fields,
        //         call db.userDAO().insert(user), then navigate to interests

        Button createAccount = findViewById(R.id.createAccount);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = EditPasswordSignup.getText().toString();
                String firstName = firstNameEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();



                Executors.newSingleThreadExecutor().execute(() -> {
                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(SignupActivity.this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                        return;
                    };

                    User user = db.userDAO().findByUsername(username);

                    if (user == null && password.equals(confirmPassword)) {
                        {
                            // username is free & password matches, create the new user
                            User newUser = new User();
                            newUser.userName = username;
                            newUser.passWord = password;
                            newUser.firstName = firstName;
                            long newUserId = db.userDAO().insert(newUser);
                            runOnUiThread(() -> {
                                Toast.makeText(SignupActivity.this, "Account registered!", Toast.LENGTH_SHORT).show();
                                navigateToInterests((int) newUserId, (String)firstName);
                            });
                        }
                    }
                    else if(!password.equals(confirmPassword)){
                        runOnUiThread(()-> Toast.makeText(SignupActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show());
                    }
                    else {
                        runOnUiThread(() -> {
                            Toast.makeText(SignupActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                        });
                    }
                });

            }
        });
    }

    private void navigateToInterests(int userId, String firstName) {
        // TODO: create an Intent to InterestSelectionActivity
        // pass the userId using intent.putExtra()
        Intent intent = new Intent(com.example.llm_project.SignupActivity.this, InterestSelectionActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("firstName", firstName);
        startActivity(intent);
    }
}