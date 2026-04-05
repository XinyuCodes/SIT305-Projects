package com.example.simplegameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    EditText etEnterName;
    Button btnStart;
    TextView tvName;

    //    private void getName(){
//        Intent getName = new Intent(WelcomeActivity.this, EndActivity.class);
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        etEnterName = findViewById(R.id.etEnterName);
        btnStart = findViewById(R.id.btnStart);
        tvName = findViewById(R.id.tvName);

        String playerName = getIntent().getStringExtra("playerName");
        if (playerName !=null) {
            etEnterName.setText(playerName);
            tvName.setText("Welcome back " + playerName);
            btnStart.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("playerName", playerName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
        else {
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etEnterName.getText().toString();
                    tvName.setText("Welcome " + name);
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("playerName", name);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    }
}