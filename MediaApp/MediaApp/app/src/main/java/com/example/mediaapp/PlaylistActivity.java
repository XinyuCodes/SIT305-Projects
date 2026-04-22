package com.example.mediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_playlist);

        int userId = getIntent().getIntExtra("userId", -1);

        ListView listView = findViewById(R.id.playlistListView);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button homeButton = findViewById(R.id.homeButton);

        List<PlaylistItem> items = AppDatabase.getDatabase(this).userDao().getPlaylistForUser(userId);

        List<String> urls = new ArrayList<>();
        for (PlaylistItem item : items) {
            urls.add(item.url);
        }

        if (urls.isEmpty()) {
            Toast.makeText(this, "Your playlist is empty", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, urls);
        listView.setAdapter(adapter);

        /// Clicking a URL loads it in the home screen player
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String url = urls.get(position);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("videoUrl", url);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        //Navigation Buttons
        ///Logging out
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        ///Home Button
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });
    }
}