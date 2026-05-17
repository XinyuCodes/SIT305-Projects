package com.example.lostandfound;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateAdvertActivity extends AppCompatActivity {

    private OkHttpClient httpClient = new OkHttpClient();
    private ListPopupWindow dropdownPopup;
    private List<String> suggestionsList = new ArrayList<>();
    private List<String> placeIdList = new ArrayList<>();
    private RadioGroup postTypeGroup;
    private EditText enterName, enterPhone, enterDescription, enterDate, enterLocation;
    private Spinner categorySpinner;
    private ImageView imagePreview;
    private Button uploadImageButton, submitButton, currentLocationButton;
    private double selectedLat = 0, selectedLng = 0;

    private Uri selectedImageUri;
    private DatabaseHelper databaseHelper;
    private FusedLocationProviderClient clientLocation;
    private static final int IMAGE_PICK_CODE = 1;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final String API_KEY = "AIzaSyDQ-aiDUDXsvp_hSexSP-G4F61_X3_DmGg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        databaseHelper = new DatabaseHelper(this);

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
        currentLocationButton = findViewById(R.id.currentLocationButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Electronics", "Pets", "Wallets", "Keys", "Accessories", "Clothing", "Other"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        clientLocation = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permission and fetch location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            fetchCurrentLocation();
        }

        currentLocationButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            } else {
                fetchCurrentLocationAndFillAddress();
            }
        });

        // set up dropdown
        dropdownPopup = new ListPopupWindow(this);
        dropdownPopup.setAnchorView(enterLocation);
        dropdownPopup.setWidth(ListPopupWindow.MATCH_PARENT);
        dropdownPopup.setModal(false);
        dropdownPopup.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPlace = suggestionsList.get(position);
            String selectedPlaceId = placeIdList.get(position);
            enterLocation.setText(selectedPlace);
            dropdownPopup.dismiss();
            fetchPlaceDetails(selectedPlaceId);
        });

        // text watcher on location field
        enterLocation.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    searchLocation(s.toString());
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        uploadImageButton.setOnClickListener(v -> {
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

            boolean isValidDate = false;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                sdf.setLenient(false);
                sdf.parse(date);
                isValidDate = true;
            } catch (ParseException e) {
                isValidDate = false;
            }

            if (!isValidDate) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (postType.isEmpty()) {
                Toast.makeText(this, "Please select Lost or Found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (validateInputs(name, phone, description, date, location)) {
                Advert advert = Advert.createNew(postType, name, phone, description, date, location, category, imagePath, selectedLat, selectedLng);
                if (databaseHelper.insertAdvert(advert) != -1) {
                    Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to save advert", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Try last location first for quick response
            clientLocation.getLastLocation().addOnSuccessListener(location -> {
                if (location != null && selectedLat == 0 && selectedLng == 0) {
                    selectedLat = location.getLatitude();
                    selectedLng = location.getLongitude();
                }
            });

            // Request fresh location
            clientLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            selectedLat = location.getLatitude();
                            selectedLng = location.getLongitude();
                            Log.d("LOCATION", "Got lat: " + selectedLat + " lng: " + selectedLng);
                        }
                    });
        }
    }

    private void fetchCurrentLocationAndFillAddress() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        clientLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {
            if (location != null) {
                selectedLat = location.getLatitude();
                selectedLng = location.getLongitude();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(selectedLat, selectedLng, 1, addresses -> {
                        if (addresses != null && !addresses.isEmpty()) {
                            runOnUiThread(() -> enterLocation.setText(addresses.get(0).getAddressLine(0)));
                        }
                    });
                } else {
                    try {
                        List<Address> addresses = geocoder.getFromLocation(selectedLat, selectedLng, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            enterLocation.setText(addresses.get(0).getAddressLine(0));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(this, "Could not get location, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void searchLocation(String query) {
        Log.d("LOCATION", "Searching with lat: " + selectedLat + " lng: " + selectedLng);
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=");
        urlBuilder.append(Uri.encode(query));
        
        // Add 10km bias if we have valid coordinates
        if (selectedLat != 0.0 || selectedLng != 0.0) {
            urlBuilder.append("&locationbias=circle:10000@").append(selectedLat).append(",").append(selectedLng);
        }
        
        urlBuilder.append("&key=").append(API_KEY);

        Request request = new Request.Builder().url(urlBuilder.toString()).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        JSONArray predictions = json.getJSONArray("predictions");

                        suggestionsList.clear();
                        placeIdList.clear();

                        for (int i = 0; i < predictions.length(); i++) {
                            JSONObject prediction = predictions.getJSONObject(i);
                            suggestionsList.add(prediction.getString("description"));
                            placeIdList.add(prediction.getString("place_id"));
                        }

                        runOnUiThread(() -> {
                            dropdownPopup.setAdapter(new ArrayAdapter<>(
                                    CreateAdvertActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    suggestionsList
                            ));
                            if (!suggestionsList.isEmpty()) {
                                dropdownPopup.show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void fetchPlaceDetails(String placeId) {
        String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                + placeId + "&fields=geometry&key=" + API_KEY;

        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        JSONObject location = json.getJSONObject("result")
                                .getJSONObject("geometry")
                                .getJSONObject("location");
                        selectedLat = location.getDouble("lat");
                        selectedLng = location.getDouble("lng");
                        Log.d("LOCATION", "Selected Place Lat: " + selectedLat + " Lng: " + selectedLng);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);
            } catch (SecurityException e) {
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
