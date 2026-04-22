package com.example.mediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        /// get all the fields
        EditText fullNameField = findViewById(R.id.editTextText3);
        EditText usernameField = findViewById(R.id.editTextText4);
        EditText passwordField = findViewById(R.id.editTextText5);
        EditText confirmPasswordField = findViewById(R.id.editTextText6);
        Button createAccountButton = findViewById(R.id.createAccount);

        /// button for creating the account
        createAccountButton.setOnClickListener(v -> {

            /// getting the info
            String fullName = fullNameField.getText().toString().trim();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            String confirmPassword = confirmPasswordField.getText().toString().trim();

            /// doing validation
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            /// checking if password is the same as confirmed password
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            /// initiating the database
            AppDatabase db = AppDatabase.getDatabase(this);
            /// more validation
            if (db.userDao().getUserByUsername(username) != null) {
                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.fullName = fullName;
            user.username = username;
            user.password = password;
            db.userDao().insertUser(user);

            Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class)); ///navigating to login page
            finish();
        });
    }
}