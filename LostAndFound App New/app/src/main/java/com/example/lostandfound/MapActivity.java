package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    private GoogleMap myMap;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<Advert> adverts = databaseHelper.getAllAdverts();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap = googleMap;

                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);

                //focusing on Australia (in this case Melbourne - because we are based in )
                LatLng australia = new LatLng(-37.8136, 144.9631);
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(australia, 10));

                for (Advert advert : adverts) {
                    if (advert.getLatitude() != 0 && advert.getLongitude() != 0) {
                        LatLng location = new LatLng(advert.getLatitude(), advert.getLongitude());
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(advert.getName())
                                .snippet(advert.getPostType() + " - " + advert.getCategory()))
                                .setTag(advert.getId());
                    }
                }

                /// listen for when markers get clicked on
                googleMap.setOnMarkerClickListener(marker -> {
                    //navigate to item detail activity
                    int advertId = (int) marker.getTag();
                    Intent intent =  new Intent(MapActivity.this, ItemDetailActivity.class);
                    intent.putExtra("ADVERT_ID", advertId);
                    startActivity(intent);
                    return(true);
                    });

            }

    });
}
}

