package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sustainablemobileapp.R;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView textViewName;
    private Button buttonHistory, buttonPayments, buttonAccountDetails, buttonContactUs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requestor_fragment_profile, container, false);

        // Initialize views
        textViewName = view.findViewById(R.id.nameTextView);
        buttonHistory = view.findViewById(R.id.buttonHistory);
        buttonPayments = view.findViewById(R.id.buttonPayments);
        buttonAccountDetails = view.findViewById(R.id.buttonAccountDetails);
        buttonContactUs = view.findViewById(R.id.buttonContactUs);

        // Set user's name (replace "User's Name" with the actual user's name)
        textViewName.setText("User's Name");

        // Set click listeners for menu options
        buttonHistory.setOnClickListener(this);
        buttonPayments.setOnClickListener(this);
        buttonAccountDetails.setOnClickListener(this);
        buttonContactUs.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        // Handle click events for menu options
        if (v.getId() == R.id.buttonHistory) {
            // Handle History option
        } else if (v.getId() == R.id.buttonPayments) {
            // Handle Payments option
        } else if (v.getId() == R.id.buttonAccountDetails) {
            // Handle Account Details option
        } else if (v.getId() == R.id.buttonContactUs) {
            // Handle Contact Us option
        }
    }
}

