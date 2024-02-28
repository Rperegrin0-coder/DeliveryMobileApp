package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sustainablemobileapp.MainActivity;
import com.example.sustainablemobileapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView textViewName;
    private Button buttonHistory, buttonPayments, buttonAccountDetails, buttonContactUs, buttonLogout;

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
        buttonLogout = view.findViewById(R.id.logoutButton);

        // Set user's name (replace "User's Name" with the actual user's name)
        textViewName = view.findViewById(R.id.nameTextView);

        // Set click listeners for menu options
        buttonHistory.setOnClickListener(this);
        buttonPayments.setOnClickListener(this);
        buttonAccountDetails.setOnClickListener(this);
        buttonContactUs.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        fetchAndSetUserName();

        return view;
    }
    private void fetchAndSetUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid(); // Get the unique user ID
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("RequestorUsers").child(currentUserId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Assuming "fullName" is the key for the user's name in the database
                        String fullName = dataSnapshot.child("fullName").getValue(String.class);
                        textViewName.setText(fullName != null ? fullName : "No name provided");
                    } else {
                        textViewName.setText("User data not found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("ProfileFragment", "loadUserName:onCancelled", databaseError.toException());
                    textViewName.setText("Failed to load user data");
                }
            });
        } else {
            textViewName.setText("Not signed in");
        }
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
        } else if (v.getId() == R.id.logoutButton) {
            logout();
        }
    }

    private void logout() {
        // Handle logout action here, such as navigating to the login screen or clearing user session
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Finish the current activity to prevent back navigation
    }
}
