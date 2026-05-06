package com.example.lostandfound;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    // UI references
    private ImageView detailImage;
    private TextView detailPostType, detailName, detailPhone;
    private TextView detailDescription, detailDate, detailLocation;
    private TextView detailCategory, detailTimestamp;
    private Button removeButton;

    // Database helper
    private DatabaseHelper databaseHelper;

    // The advert being displayed
    private Advert currentAdvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Initialise database helper
        databaseHelper = new DatabaseHelper(this);

        // findViewById for all views
        detailImage = findViewById(R.id.detailImage);
        detailPostType = findViewById(R.id.detailPostType);
        detailName = findViewById(R.id.detailName);
        detailDate = findViewById(R.id.detailDate);
        detailCategory = findViewById(R.id.detailCategory);
        detailTimestamp = findViewById(R.id.detailTimestamp);
        detailLocation = findViewById(R.id.detailLocation);
        detailDescription = findViewById(R.id.detailDescription);
        detailPhone = findViewById(R.id.detailPhone);
        removeButton = findViewById(R.id.removeButton);

        // Get the ADVERT_ID from the incoming Intent
        int advertId = getIntent().getIntExtra("ADVERT_ID", -1);

        // Check if advertId is valid
        if (advertId == -1) {
            Toast.makeText(this, "Invalid advert ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch the advert from the database
        currentAdvert = databaseHelper.getAdvertById(advertId);

        // Check if currentAdvert is not null
        if (currentAdvert == null) {
            Toast.makeText(this, "Advert not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        populateViews();

        // Set up removeButton click listener
        removeButton.setOnClickListener(v -> {
            databaseHelper.deleteAdvert(currentAdvert.getId());
            Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
            finish();
        });

        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void populateViews() {
        detailPostType.setText(currentAdvert.getPostType());
        detailName.setText(currentAdvert.getName());
        detailDate.setText(currentAdvert.getDate());
        detailCategory.setText(currentAdvert.getCategory());
        detailTimestamp.setText(currentAdvert.getTimestamp());
        detailLocation.setText(currentAdvert.getLocation());
        detailDescription.setText(currentAdvert.getDescription());
        detailPhone.setText(currentAdvert.getPhone());

        if (currentAdvert.getImagePath() != null && !currentAdvert.getImagePath().isEmpty()) {
            try {
                detailImage.setImageURI(Uri.parse(currentAdvert.getImagePath()));
            } catch (Exception e) {
                // Fallback if permission is denied or URI is invalid
                detailImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            detailImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }
}