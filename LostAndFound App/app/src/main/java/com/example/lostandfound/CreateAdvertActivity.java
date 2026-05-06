package com.example.lostandfound;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private RadioGroup postTypeGroup;
    private EditText enterName, enterPhone, enterDescription, enterDate, enterLocation;
    private Spinner categorySpinner;
    private ImageView imagePreview;
    private Button uploadImageButton, submitButton;

    private Uri selectedImageUri;
    private DatabaseHelper databaseHelper;
    private static final int IMAGE_PICK_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        //get database helper
        databaseHelper = new DatabaseHelper(this);
        //get key info
        enterName = findViewById(R.id.enterName);
        enterPhone = findViewById(R.id.enterPhone);
        enterDescription = findViewById(R.id.enterDescription);
        enterDate = findViewById(R.id.enterDate);
        enterLocation = findViewById(R.id.enterLocation);
        postTypeGroup = findViewById(R.id.postTypeGroup);
        categorySpinner = findViewById(R.id.categorySpinner);
        imagePreview = findViewById(R.id.imagePreview);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        submitButton = findViewById(R.id.submitButton);
        //setting up spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Electronics", "Pets", "Wallets", "Keys", "Accessories", "Clothing", "Other"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        uploadImageButton.setOnClickListener(v -> {
            // tried using photo library and it doesnt work (persistent permission issue)
            // instead using open document for persistent permission
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        submitButton.setOnClickListener(v -> {
            String name = enterName.getText().toString().trim();
            String phone = enterPhone.getText().toString().trim();
            String description = enterDescription.getText().toString().trim();
            String date = enterDate.getText().toString().trim();
            String location = enterLocation.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();
            String imagePath = selectedImageUri != null ? selectedImageUri.toString() : "";
            
            String postType = "";
            int selectedId = postTypeGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.lostButton) postType = "Lost";
            else if (selectedId == R.id.foundButton) postType = "Found";

            /// validation criteria
            ///  test if we can parse date string correctly

            boolean isValidDate = false;

            try{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                sdf.setLenient(false);
                sdf.parse(date); //checking if we are getting crazy dates like 10/20/19
                isValidDate = true;
            }
            catch(ParseException e){
                isValidDate = false;
            }

            if(isValidDate == false)
            {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            /// validation criteria: check for post
            if (postType.isEmpty()) {
                Toast.makeText(this, "Please select Lost or Found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (validateInputs(name, phone, description, date, location)) {
                Advert advert = Advert.createNew(postType, name, phone, description, date, location, category, imagePath);
                if (databaseHelper.insertAdvert(advert) != -1) {
                    Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to save advert", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            
            // Critical: Requesting persistent permission so images load after app restart
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
            } catch (SecurityException e) {
                // Fallback for URIs that don't support persistable permissions
                e.printStackTrace();
            }

            imagePreview.setImageURI(selectedImageUri);
        }
    }

    private boolean validateInputs(String name, String phone, String description, String date, String location) {
        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}