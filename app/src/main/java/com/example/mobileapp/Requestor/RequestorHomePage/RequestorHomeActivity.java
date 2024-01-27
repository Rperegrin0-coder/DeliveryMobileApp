package com.example.mobileapp.Requestor.RequestorHomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.mobileapp.R;
import com.example.mobileapp.Requestor.RequestorHomePage.Fragments.DeliveryStatusFragment;
import com.example.mobileapp.Requestor.RequestorHomePage.Fragments.ProfileFragment;
import com.example.mobileapp.Requestor.RequestorHomePage.Fragments.RequestsFragment;
import com.example.mobileapp.Requestor.RequestorHomePage.Fragments.RewardsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RequestorHomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment selectedFragment = null;

        // Replace the switch-case with if-else statements
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_delivery_status) {
            selectedFragment = new DeliveryStatusFragment();
        } else if (itemId == R.id.navigation_requests) {
            selectedFragment = new RequestsFragment();
        } else if (itemId == R.id.navigation_rewards) {
            selectedFragment = new RewardsFragment();
        } else if (itemId == R.id.navigation_profile) {
            selectedFragment = new ProfileFragment();
        }

        // Use a fragment transaction to replace the current fragment in the 'fragment_container' with the selected fragment
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true; // return true to indicate the event was handled
        }

        return false; // return false to indicate the event was not handled
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestorhomeactivity);

        // Find the BottomNavigationView
        BottomNavigationView navigation = findViewById(R.id.bottomNav);

        // Set the listener to the BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set the initial fragment to 'RequestsFragment'
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_requests); // This line selects the requests item, triggering the listener
        }
    }
}
