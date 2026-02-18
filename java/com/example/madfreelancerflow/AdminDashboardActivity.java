package com.example.madfreelancerflow;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class AdminDashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        bottomNavigationView = findViewById(R.id.BottomNav);

        // Set default fragment
        loadFragment(new AdminHomefragment());

        // Listen to bottom navigation taps
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                int id = item.getItemId();

                // Using if-else instead of switch
                if (id == R.id.nav_home) {
                    selectedFragment = new AdminHomefragment();
                } else if (id == R.id.nav_profile) {
                    selectedFragment = new AdminProfileFragment();
                } else if (id == R.id.nav_messages) {
                    selectedFragment = new ChatFragment();
                }else {
                    selectedFragment = new AIChatFragment();
                }
                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        });
    }

    // Method to load fragment in container
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentContainer, fragment); // Make sure this ID exists in your activity layout
        transaction.commit();
    }
}
