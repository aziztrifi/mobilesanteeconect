package com.example.santeconnect.Activity.Modules.RendezVous;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.ImageView;

import com.example.santeconnect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Calender extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender); // Ensure this points to your main layout

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set a default fragment to load
        loadFragment(new HomeFragment());

        // Handle navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.navigation_calendar) {
                selectedFragment = new CalendarFragment(); // Load CalendarFragment
            } else if (item.getItemId() == R.id.navigation_dossier) {
                selectedFragment = new DossierFragment(); // Load DossierFragment
            }else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment(); // Load DossierFragment
            }
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) // Smooth transition
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
