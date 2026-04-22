package com.example.task51c;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //navigation
        bottomNav = findViewById(R.id.bottom_nav);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController=navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNav, navController);

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeFragment) {
                navController.popBackStack(R.id.homeFragment, false);
                return true;
            }
            if (item.getItemId() == R.id.bookmarksFragment) {
                navController.navigate(R.id.bookmarksFragment);
                return true;
            }
            return false;
        });
    }
}