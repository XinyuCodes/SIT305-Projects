package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.libraries.places.api.Places;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext()
                    , "AIzaSyBIWa2vQYpQS75pGM7pywizWydMMKbN-GE");

            Button showAdvertButton = findViewById(R.id.showButton);
            showAdvertButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, ViewAllActivity.class);
                startActivity(intent);
            });

            Button createAdvertButton = findViewById(R.id.createAdvert);
            createAdvertButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, CreateAdvertActivity.class);
                startActivity(intent);
            });

            /// additional button to show the map
            Button mapButton = findViewById(R.id.showMapButton);
            mapButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}