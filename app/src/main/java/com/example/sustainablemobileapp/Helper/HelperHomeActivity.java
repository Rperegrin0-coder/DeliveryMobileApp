package com.example.sustainablemobileapp.Helper;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.sustainablemobileapp.Helper.HelperFragments.FindJobFragment.FindJobFragment;
import com.example.sustainablemobileapp.Helper.HelperFragments.HelperProfileFragment;
import com.example.sustainablemobileapp.Helper.HelperFragments.HelperRewardsFragment;
import com.example.sustainablemobileapp.Helper.HelperFragments.MyDeliveriesFragment;

import com.example.sustainablemobileapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HelperHomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment selectedFragment = null;

        // Replace the switch-case with if-else statements
        int itemId = item.getItemId();
        if (itemId == R.id.helper_navigation_my_deliveries) {
            selectedFragment = new MyDeliveriesFragment();
        } else if (itemId == R.id.helper_navigation_find_job) {
            selectedFragment = new FindJobFragment();
        } else if (itemId == R.id.helper_navigation_rewards) {
            selectedFragment = new HelperRewardsFragment();
        } else if (itemId == R.id.helper_navigation_profile) {
            selectedFragment = new HelperProfileFragment();
        }

        // Use a fragment transaction to replace the current fragment in the 'fragment_container' with the selected fragment
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.helper_fragment_container, selectedFragment)
                    .commit();
            return true; // return true to indicate the event was handled
        }

        return false; // return false to indicate the event was not handled
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helperhomeactivity);

        // Find the BottomNavigationView
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_helper);

        // Set the listener to the BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set the initial fragment to 'RequestsFragment'
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.helper_navigation_find_job); // This line selects the requests item, triggering the listener
        }
    }
}
