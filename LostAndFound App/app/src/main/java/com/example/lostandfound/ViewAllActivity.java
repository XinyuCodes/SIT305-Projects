package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    // UI references
    private RecyclerView recyclerView;
    private EditText searchBar;
    private Spinner categoryFilter;

    // Adapter and data
    private AdvertAdapter adapter;
    private List<Advert> allAdverts;      // full unfiltered list from DB
    private List<Advert> filteredAdverts; // filtered list shown in RecyclerView

    // Database helper
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        // Initialise database helper
        databaseHelper = new DatabaseHelper(this);

        // findViewById for recyclerView, searchBar, categoryFilter
        recyclerView = findViewById(R.id.lostItemList);
        searchBar = findViewById(R.id.searchBar);
        categoryFilter = findViewById(R.id.categoryFilter);

        // Set up RecyclerView with LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Load all adverts from database into allAdverts
        // copy allAdverts into filteredAdverts
        allAdverts = databaseHelper.getAllAdverts();
        filteredAdverts = new ArrayList<>(allAdverts);

        // Set up adapter with filteredAdverts and item click listener
        adapter = new AdvertAdapter(this, filteredAdverts, advert -> {
            Intent intent = new Intent(ViewAllActivity.this, ItemDetailActivity.class);
            intent.putExtra("ADVERT_ID", advert.getId());
            startActivity(intent);
        });

        // Attach adapter to recyclerView
        recyclerView.setAdapter(adapter);

        // Set up category filter Spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Electronics", "Pets", "Wallets", "Keys", "Accessories", "Clothing", "Other"}
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set up Spinner item selected listener
        categoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        categoryFilter.setAdapter(categoryAdapter);

        // Set up search bar text watcher
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        allAdverts = databaseHelper.getAllAdverts();
        applyFilters();

    }

    private void applyFilters() {
        String searchText = searchBar.getText().toString().toLowerCase().trim();
        String selectedCategory = categoryFilter.getSelectedItem().toString();
        filteredAdverts.clear();
        for (Advert advert : allAdverts) {
            if (searchText.isEmpty() || advert.getName().toLowerCase().contains(searchText)) {
                if (selectedCategory.equals("All") || advert.getCategory().equals(selectedCategory)) {
                    filteredAdverts.add(advert);
                }
            }
        }
        adapter.updateList(filteredAdverts);
                }
}