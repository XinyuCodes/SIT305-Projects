package com.example.lostandfound;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private DatabaseHelper databaseHelper;
    private FusedLocationProviderClient clientLocation;
    private double selectedLat = 0, selectedLng = 0;
    private static final int LOCATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        databaseHelper = new DatabaseHelper(this);
        clientLocation = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Map settings
        myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(true);

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            myMap.setMyLocationEnabled(true);
            fetchCurrentLocation();
        }

        displayMarkers();

        // Listen for when markers get clicked on
        myMap.setOnMarkerClickListener(marker -> {
            Integer advertId = (Integer) marker.getTag();
            if (advertId != null) {
                Intent intent = new Intent(MapActivity.this, ItemDetailActivity.class);
                intent.putExtra("ADVERT_ID", (int) advertId);
                startActivity(intent);
            }
            return true;
        });
    }

    private void displayMarkers() {
        List<Advert> adverts = databaseHelper.getAllAdverts();
        for (Advert advert : adverts) {
            if (advert.getLatitude() != 0 || advert.getLongitude() != 0) {
                LatLng location = new LatLng(advert.getLatitude(), advert.getLongitude());
                myMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(advert.getName())
                                .snippet(advert.getPostType() + ": " + advert.getCategory()))
                        .setTag(advert.getId());
            }
        }
    }

    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Try last location first for quick response
            clientLocation.getLastLocation().addOnSuccessListener(location -> {
                if (location != null && selectedLat == 0 && selectedLng == 0) {
                    selectedLat = location.getLatitude();
                    selectedLng = location.getLongitude();
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedLat, selectedLng), 10));
                }
            });

            // Request fresh location
            clientLocation.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            selectedLat = location.getLatitude();
                            selectedLng = location.getLongitude();
                            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedLat, selectedLng), 12));
                        } else {
                            // Fallback to Melbourne if location is unavailable
                            LatLng melbourne = new LatLng(-37.8136, 144.9631);
                            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 10));
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (myMap != null) {
                        myMap.setMyLocationEnabled(true);
                        fetchCurrentLocation();
                    }
                }
            } else {
                Toast.makeText(this, "Permission denied. Using default location.", Toast.LENGTH_SHORT).show();
                if (myMap != null) {
                    LatLng melbourne = new LatLng(-37.8136, 144.9631);
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 10));
                }
            }
        }
    }
}
