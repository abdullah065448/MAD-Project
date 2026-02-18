package com.example.madfreelancerflow;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FreelancerDashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freelancer_dashboard);

        bottomNavigationView = findViewById(R.id.BottomNav);

        // Set default fragment (Home)
        loadFragment(new FreelancerHomeFragment());

        // Listen to BottomNavigationView item clicks (new API)
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int id = item.getItemId();

            // Using if-else to determine which fragment to load
            if (id == R.id.nav_home) {
                selectedFragment = new FreelancerHomeFragment();
            } else if (id == R.id.nav_messages) {
                selectedFragment = new ChatFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new FreelancerProfileFragment();
            } else if (id == R.id.nav_ai) {
                selectedFragment = new AIChatFragment();
            } else {
                // Fallback in case of unknown ID
                selectedFragment = new FreelancerHomeFragment();
            }

            // Load the selected fragment
            loadFragment(selectedFragment);
            return true; // Returning true to indicate item selection is handled
        });
    }

    /**
     * Helper method to load a fragment into the container.
     *
     * @param fragment The fragment to display
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FragmentContainer, fragment); // Make sure this ID exists in layout
        transaction.commit();
    }
}
