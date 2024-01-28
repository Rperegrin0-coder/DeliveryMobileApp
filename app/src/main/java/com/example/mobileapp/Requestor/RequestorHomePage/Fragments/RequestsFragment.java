package com.example.mobileapp.Requestor.RequestorHomePage.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;  // Correct import for Manifest
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileapp.APIs.GooglePlacesAutocomplete;
import com.example.mobileapp.APIs.RandomGenerator;
import com.example.mobileapp.APIs.GPS;

import com.example.mobileapp.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;



public class RequestsFragment extends Fragment {

    // UI components declaration
    private EditText parcelDescription, parcelWeight, pickupAddress, pickupContactName,
            pickupContactPhone, deliveryAddress, deliveryRecipientName,
            deliveryRecipientPhone, pickupDateTime, deliveryDateTime, additionalInstructions;
    private Spinner parcelSize;
    private CheckBox fragileCheckbox;
    private Button submitRequestButton;

    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private static final int REQUEST_CODE = 123;

    private GPS gps;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gps = new GPS(requireContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        AutoCompleteTextView pickupAddressAutoComplete = view.findViewById(R.id.pickupAddressAutoComplete);
        AutoCompleteTextView deliveryAddressAutoComplete = view.findViewById(R.id.deliveryAddressAutoComplete);

        Button addPickupDetailsButton = view.findViewById(R.id.addPickupDetailsButton);
        Button addDeliveryDetailsButton = view.findViewById(R.id.addDeliveryDetailsButton);

        addPickupDetailsButton.setOnClickListener(v -> openAddressDetailsDialog("pickup"));
        addDeliveryDetailsButton.setOnClickListener(v -> openAddressDetailsDialog("delivery"));



        GooglePlacesAutocomplete adapter = new GooglePlacesAutocomplete(getContext(), R.layout.autocomplete_place_item);
        pickupAddressAutoComplete.setAdapter(adapter);
        deliveryAddressAutoComplete.setAdapter(adapter);



        // Initialize EditText fields
        parcelDescription = view.findViewById(R.id.parcelDescription);
        parcelWeight = view.findViewById(R.id.parcelWeight);
        pickupAddress = view.findViewById(R.id.pickupAddressAutoComplete);
        pickupContactName = view.findViewById(R.id.pickupContactName);
        pickupContactPhone = view.findViewById(R.id.pickupContactPhone);
        deliveryAddress = view.findViewById(R.id.deliveryAddressAutoComplete);
        deliveryRecipientName = view.findViewById(R.id.deliveryRecipientName);
        deliveryRecipientPhone = view.findViewById(R.id.deliveryRecipientPhone);
        pickupDateTime = view.findViewById(R.id.pickupDateTime);
        deliveryDateTime = view.findViewById(R.id.deliveryDateTime);
        additionalInstructions = view.findViewById(R.id.additionalInstructions);

        // Initialize Spinner and CheckBox
        parcelSize = view.findViewById(R.id.parcelSize); // Replace with your actual Spinner ID
        fragileCheckbox = view.findViewById(R.id.fragileCheckbox); // Replace with your actual CheckBox ID

        // Initialize Button
        submitRequestButton = view.findViewById(R.id.submitRequestButton);

        Button useGPSButton = view.findViewById(R.id.useGpsButton); // Ensure you have this button in your layout
        useGPSButton.setOnClickListener(v -> {
            if(gps.isPermissionGranted()) {
                fetchCurrentLocation();
            } else {
                requestLocationPermissions();
            }
        });


        // Ensure all other UI components are initialized similarly before this line

        submitRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequest();
            }
        });

        return view;
    }

    private void fetchCurrentLocation() {
        // Ensure the GPS class is initialized
        if (gps == null) {
            gps = new GPS(getContext()); // Adjust this according to how GPS is instantiated
        }

        gps.getCurrentLocation(location -> {
            if (location != null) {
                // Use Geocoder to convert the location to a readable address
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        // Get the first address line or formatted address
                        String address = addresses.get(0).getAddressLine(0);
                        // Update the pickup address EditText
                        pickupAddress.setText(address);
                    }
                } catch (IOException e) {
                    // Handle exception: Geocoder may fail
                    Log.e(TAG, "Geocoder failed", e);
                }
            } else {
                // Handle case where location is null
                Toast.makeText(getContext(), "Unable to fetch location. Please ensure location services are enabled.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void requestLocationPermissions() {
        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION },
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
            } else {
                // Handle permission denial
            }
        }
    }
    // Function to open the address details dialog
    private void openAddressDetailsDialog(String addressType) {
        AddressDetailsDialogFragment dialogFragment = AddressDetailsDialogFragment.newInstance(addressType);
        dialogFragment.setTargetFragment(this, REQUEST_CODE); // Define REQUEST_CODE as a constant
        dialogFragment.show(getParentFragmentManager(), "AddressDetailsDialog");
    }

    // Callback to receive the address details
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String addressType = data.getStringExtra("addressType");
            String detailedAddress = data.getStringExtra("detailedAddress");
            // Handle the detailed address (store in ViewModel or send to the server)
            if ("pickup".equals(addressType)) {
                pickupAddress.setText(detailedAddress);
            } else if ("delivery".equals(addressType)) {
                deliveryAddress.setText(detailedAddress);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void submitRequest() {
        RandomGenerator randomGenerator = new RandomGenerator();
        randomGenerator.getRandomNumber(new RandomGenerator.RandomNumberCallback() {
            @Override
            public void onNumberGenerated(String randomNumber) {
                // Check for null or error responses
                if (randomNumber != null) {
                    if (randomNumber.startsWith("Error:")) {
                        // Log the specific error message received from RandomGenerator
                        Log.e(TAG, randomNumber);
                        Toast.makeText(getContext(), "Failed to generate parcel number. Error: " + randomNumber, Toast.LENGTH_LONG).show();
                    } else {
                        // If randomNumber is valid, proceed with saving the request
                        saveRequestToFirebase(randomNumber);
                    }
                } else {
                    // Handle the case where randomNumber is null
                    Log.e(TAG, "Random number generation returned null.");
                    Toast.makeText(getContext(), "Error generating parcel number. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveRequestToFirebase(String parcelNumber) {
        // Add try-catch for NullPointerException
        try {
            // Collect data from form fields and add debug logs
            Log.d(TAG, "Collecting data from form fields");
            Log.d(TAG, "parcelDescription: " + (parcelDescription == null ? "null" : "initialized"));
            Log.d(TAG, "parcelWeight: " + (parcelWeight == null ? "null" : "initialized"));
            Log.d(TAG, "pickupAddress: " + (pickupAddress == null ? "null" : "initialized"));
            Log.d(TAG, "pickupContactName: " + (pickupContactName == null ? "null" : "initialized"));
            Log.d(TAG, "pickupContactPhone: " + (pickupContactPhone == null ? "null" : "initialized"));
            Log.d(TAG, "deliveryAddress: " + (deliveryAddress == null ? "null" : "initialized"));
            Log.d(TAG, "deliveryRecipientName: " + (deliveryRecipientName == null ? "null" : "initialized"));
            Log.d(TAG, "deliveryRecipientPhone: " + (deliveryRecipientPhone == null ? "null" : "initialized"));
            Log.d(TAG, "pickupDateTime: " + (pickupDateTime == null ? "null" : "initialized"));
            Log.d(TAG, "deliveryDateTime: " + (deliveryDateTime == null ? "null" : "initialized"));
            Log.d(TAG, "additionalInstructions: " + (additionalInstructions == null ? "null" : "initialized"));
            Log.d(TAG, "parcelSize: " + (parcelSize == null ? "null" : "initialized"));
            Log.d(TAG, "fragileCheckbox: " + (fragileCheckbox == null ? "null" : "initialized"));




            String description = parcelDescription == null ? "" : parcelDescription.getText().toString();
            String weight = parcelWeight == null ? "" : parcelWeight.getText().toString();
            String pickupAddr = pickupAddress == null ? "" : pickupAddress.getText().toString();
            String pickupContact = pickupContactName == null ? "" : pickupContactName.getText().toString();
            String pickupPhone = pickupContactPhone == null ? "" : pickupContactPhone.getText().toString();
            String deliveryAddr = deliveryAddress == null ? "" : deliveryAddress.getText().toString();
            String deliveryRecipient = deliveryRecipientName == null ? "" : deliveryRecipientName.getText().toString();
            String deliveryPhone = deliveryRecipientPhone == null ? "" : deliveryRecipientPhone.getText().toString();
            String pickupTime = pickupDateTime == null ? "" : pickupDateTime.getText().toString();
            String deliveryTime = deliveryDateTime == null ? "" : deliveryDateTime.getText().toString();
            String additionalInfo = additionalInstructions == null ? "" : additionalInstructions.getText().toString();
            String size = parcelSize == null ? "" : parcelSize.getSelectedItem() == null ? "" : parcelSize.getSelectedItem().toString();
            boolean isFragile = fragileCheckbox != null && fragileCheckbox.isChecked();




            Map<String, Object> requestData = new HashMap<>();
            requestData.put("parcelDescription", description);
            requestData.put("parcelNumber", parcelNumber);
            requestData.put("parcelWeight", weight);
            requestData.put("pickupAddress", pickupAddr);
            requestData.put("pickupContactName", pickupContact);
            requestData.put("pickupContactPhone", pickupPhone);
            requestData.put("deliveryAddress", deliveryAddr);
            requestData.put("deliveryRecipientName", deliveryRecipient);
            requestData.put("deliveryRecipientPhone", deliveryPhone);
            requestData.put("pickupDateTime", pickupTime);
            requestData.put("deliveryDateTime", deliveryTime);
            requestData.put("additionalInstructions", additionalInfo);
            requestData.put("parcelSize", size);
            requestData.put("isFragile", isFragile);

            databaseReference.child("requests").push().setValue(requestData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Request submitted successfully", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Request data added successfully");
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to submit request: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error adding request data", e);
                    });

        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException caught in saveRequestToFirebase", e);
        }
    }

    // Additional methods (e.g., DatePickerDialog)...
}





