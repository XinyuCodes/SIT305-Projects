package com.example.mediaapp;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    WebView youtubeWebView;
    EditText youtubeURL;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getIntExtra("userId", -1);
        //getting youtube
        youtubeWebView = findViewById(R.id.youtubeWebView);
        youtubeURL = findViewById(R.id.youtubeURL);
        Button playButton = findViewById(R.id.playButton);
        Button addToPlaylist = findViewById(R.id.addToPlaylist);
        Button myPlaylist = findViewById(R.id.myPlaylist);
        Button logoutButton = findViewById(R.id.logoutButton);

        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        youtubeWebView.setWebChromeClient(new WebChromeClient());

        //getting the incoming url
        String incomingUrl = getIntent().getStringExtra("videoUrl");
        if (incomingUrl != null) {
            youtubeURL.setText(incomingUrl);
            String videoId = extractVideoId(incomingUrl);
            if (videoId != null) loadVideo(videoId);
        }

        //button & action to enable playing the video upon clicking
        playButton.setOnClickListener(v -> {
            String url = youtubeURL.getText().toString().trim();
            String videoId = extractVideoId(url);
            if (videoId == null) {
                Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }
            loadVideo(videoId);
        });

        //function for adding video to playlist
        addToPlaylist.setOnClickListener(v -> {
            String url = youtubeURL.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a URL first", Toast.LENGTH_SHORT).show();
                return;
            }
            if (extractVideoId(url) == null) {
                Toast.makeText(this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                return;
            }
            PlaylistItem item = new PlaylistItem();
            item.userId = userId;
            item.url = url;
            AppDatabase.getDatabase(this).userDao().insertPlaylistItem(item);
            Toast.makeText(this, "Added to playlist!", Toast.LENGTH_SHORT).show();
        });
        //playlist navigation
        myPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlaylistActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
        //logout button
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
    //functon for getting the videoID
    private String extractVideoId(String url) {
        if (url == null || url.isEmpty()) return null;
        if (url.contains("v=")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) return parts[1].split("&")[0];
        } else if (url.contains("youtu.be/")) {
            String[] parts = url.split("youtu.be/");
            if (parts.length > 1) return parts[1].split("\\?")[0];
        }
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String incomingUrl = intent.getStringExtra("videoUrl");
        if (incomingUrl != null) {
            youtubeURL.setText(incomingUrl);
            String videoId = extractVideoId(incomingUrl);
            if (videoId != null) loadVideo(videoId);
        }
    }

    //loading video
    public void loadVideo(String videoId) {
        String html = "<!DOCTYPE html><html><head>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "<style>body{margin:0;padding:0;background:#000;} iframe{width:100%;height:100%;}</style>" +
                "</head><body>" +
                "<iframe src='https://www.youtube-nocookie.com/embed/" + videoId + "?autoplay=1&playsinline=1' " +
                "frameborder='0' allow='autoplay; encrypted-media' allowfullscreen></iframe>" +
                "</body></html>";
        youtubeWebView.loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "utf-8", null);
    }
}