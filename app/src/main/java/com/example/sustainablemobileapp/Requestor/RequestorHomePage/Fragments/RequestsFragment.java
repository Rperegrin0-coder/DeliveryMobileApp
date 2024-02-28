package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import static android.content.ContentValues.TAG;

import android.Manifest;  // Correct import for Manifest
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.sustainablemobileapp.APIs.Camera;
import com.example.sustainablemobileapp.APIs.GooglePlacesAutocomplete;
import com.example.sustainablemobileapp.APIs.RandomGenerator;
import com.example.sustainablemobileapp.APIs.GPS;


import com.example.sustainablemobileapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.DatePickerDialog;

import java.util.Calendar;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RequestsFragment extends Fragment {
    private static final String TAG = "RequestFragment";

    //constants for requests
    private static final int PICKUP_DATE_REQUEST = 1;
    private static final int DELIVERY_DATE_REQUEST = 2;
    private static final int REQUEST_CODE = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    private static final int PICK_IMAGE_REQUEST = 124;

    // UI components declaration
    private EditText parcelDescription, parcelWeight, pickupAddress, pickupContactName,
            pickupContactPhone, deliveryAddress, deliveryRecipientName,
            deliveryRecipientPhone, pickupDateTime, deliveryDateTime, additionalInstructions;
    private Spinner parcelSize;
    private CheckBox fragileCheckbox;
    private Button submitRequestButton;

    private FirebaseFirestore db;
    private DatabaseReference databaseReference;
    private GPS gps;
    private Bitmap parcelImage;

    private Camera camera; // De


    //Method to show the DatePickerDialog
    private void showDatePickerDialog(int dateType) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog listens for when the user sets the date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    // Update the EditText with the chosen date
                    String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    // Check which dateType it is and set the text on the correct EditText
                    if (dateType == PICKUP_DATE_REQUEST) {
                        pickupDateTime.setText(selectedDate);
                        showTimePickerDialog(PICKUP_DATE_REQUEST);
                    } else if (dateType == DELIVERY_DATE_REQUEST) {
                        deliveryDateTime.setText(selectedDate);
                        showTimePickerDialog(DELIVERY_DATE_REQUEST);
                    }
                },
                year,
                month,
                day
        );

        // Set the DatePickerDialog to not allow past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog(int dateType) {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // TimePickerDialog listens for when the user sets the time
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute1) -> {
                    // Format and set the time on the correct EditText
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    if (dateType == PICKUP_DATE_REQUEST) {
                        String existingDate = pickupDateTime.getText().toString();
                        pickupDateTime.setText(String.format("%s %s", existingDate, selectedTime));
                    } else if (dateType == DELIVERY_DATE_REQUEST) {
                        String existingDate = deliveryDateTime.getText().toString();
                        deliveryDateTime.setText(String.format("%s %s", existingDate, selectedTime));
                    }
                },
                hour,
                minute,
                true // or false for 12 hour time
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the Camera object
        camera = new Camera(getActivity());

        gps = new GPS(requireContext());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requestor_fragment_requests, container, false);

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


        Button addPhotoButton = view.findViewById(R.id.attachImage);


        GooglePlacesAutocomplete adapter = new GooglePlacesAutocomplete(getContext(), R.layout.requestor_autocomplete_place_item);
        pickupAddressAutoComplete.setAdapter(adapter);
        deliveryAddressAutoComplete.setAdapter(adapter);

        EditText pickupDateTimeEditText = view.findViewById(R.id.pickupDateTime);
        pickupDateTimeEditText.setOnClickListener(v -> showDatePickerDialog(PICKUP_DATE_REQUEST));

        EditText deliveryDateTimeEditText = view.findViewById(R.id.deliveryDateTime);
        deliveryDateTimeEditText.setOnClickListener(v -> showDatePickerDialog(DELIVERY_DATE_REQUEST));

        Camera camera = new Camera(getActivity());





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
        //parcelSize = view.findViewById(R.id.parcelSize); // Replace with your actual Spinner ID
        fragileCheckbox = view.findViewById(R.id.fragileCheckbox); // Replace with your actual CheckBox ID

        // Initialize Button
        submitRequestButton = view.findViewById(R.id.submitRequestButton);



        ImageView useGpsImageView = view.findViewById(R.id.gpsImageView);
        useGpsImageView.setOnClickListener(v -> {
            if(gps.isPermissionGranted()) {
                fetchCurrentLocation();
            } else {
                requestLocationPermissions();
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
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
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            Log.d("CameraPermission", "Permission not granted. Requesting permission...");
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted, proceed to take a photo
            Log.d("CameraPermission", "Permission already granted. Taking photo...");
            camera.takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: requestCode=" + requestCode);

        // Handle permissions result...
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Camera permission granted");

                // Check if camera is not null before using
                if (camera != null) {
                    Log.d(TAG, "Taking photo with camera");
                    camera.takePhoto();


                } else {
                    // Handle the case where camera is null
                    Log.e(TAG, "Camera is null");
                    Toast.makeText(getContext(), "Camera not initialized", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission denied, handle the case
                Log.d(TAG, "Camera permission denied");
                Toast.makeText(getContext(), "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImageAttachedMessage() {
        Log.d(TAG, "Attempting to show 'Image Attached' message.");

        // Find the TextView and set its text
        TextView imageAttachedTextView = getView().findViewById(R.id.imageAttachedTextView);
        imageAttachedTextView.setText("Image Attached");

        // Make the TextView visible
        imageAttachedTextView.setVisibility(View.VISIBLE);

        Log.d(TAG, "'Image Attached' message displayed successfully.");
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
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        // Handle the result from the address details request
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String addressType = data.getStringExtra("addressType");
            String detailedAddress = data.getStringExtra("detailedAddress");

            Log.d(TAG, "Received address details. Type: " + addressType + ", Address: " + detailedAddress);

            if ("pickup".equals(addressType)) {
                pickupAddress.setText(detailedAddress);
            } else if ("delivery".equals(addressType)) {
                deliveryAddress.setText(detailedAddress);
            }
        }

        // Handle the result from the camera intent
        else if (requestCode == Camera.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                if (imageBitmap != null) {
                    Log.d(TAG, "Image captured successfully.");
                    handleImage(imageBitmap);
                } else {
                    Log.e(TAG, "Image data received from the camera is null. Image not attached to Bitmap.");
                }
            } else {
                Log.e(TAG, "Camera intent returned null extras.");
            }
        }

        // Handle the result from selecting an image from the gallery
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Log.d(TAG, "Image selected from gallery.");
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    handleImage(imageBitmap);
                } catch (IOException e) {
                    Log.e(TAG, "Exception in converting URI to Bitmap", e);
                }
            } else {
                Log.e(TAG, "Selected image URI is null.");
            }
        } else {
            Log.d(TAG, "Unhandled requestCode or resultCode in onActivityResult.");
        }
    }

    private void handleImage(Bitmap imageBitmap) {
        // Example dimensions for a thumbnail
        int width = 100; // Adjust width as needed
        int height = 100; // Adjust height as needed

        // Create a thumbnail from the original Bitmap
        Bitmap thumbnail = Bitmap.createScaledBitmap(imageBitmap, width, height, false);

        // Assuming parcelImage is a class variable where you store the thumbnail
        parcelImage = thumbnail;

        // Assuming there's an ImageView with ID capturedImageView in your layout
        ImageView capturedImageView = getView().findViewById(R.id.capturedImageView);
        if (capturedImageView != null) {
            capturedImageView.setImageBitmap(thumbnail); // Set the thumbnail to ImageView
            Log.d(TAG, "Thumbnail image set to ImageView.");
        } else {
            Log.e(TAG, "ImageView not found in the layout.");
        }

        // Show a Toast message to indicate the image is attached
        Toast.makeText(getActivity(), "Image attached successfully", Toast.LENGTH_SHORT).show();

        // Update the status TextView to indicate the image is attached
        TextView statusTextView = getView().findViewById(R.id.attachImage); // Make sure this ID matches your layout
        if (statusTextView != null) {
            statusTextView.setText("Image attached");
            Log.d(TAG, "Status text updated to indicate image is attached.");
        } else {
            Log.e(TAG, "Status TextView not found in the layout.");
        }
    }




    private void submitRequest() {
        RandomGenerator randomGenerator = new RandomGenerator();
        randomGenerator.getRandomNumber(new RandomGenerator.RandomNumberCallback() {
            @Override
            public void onNumberGenerated(String randomNumber) {
                if (randomNumber != null && !randomNumber.startsWith("Error:")) {
                    // If randomNumber is valid and image is available, proceed with saving the request
                    if (parcelImage != null) {
                        uploadImageAndSaveRequest(randomNumber, parcelImage);
                    } else {
                        // If no image, proceed without image
                        saveRequestToFirebase(randomNumber, null);
                    }
                    // Delay showing the notification
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showNotificationForRequest();
                        }
                    }, 5000); // Delay for 3 seconds
                } else {
                    // Handle error or null cases
                    String error = randomNumber != null ? randomNumber : "Error generating parcel number. Please try again.";
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void uploadImageAndSaveRequest(String parcelNumber, Bitmap image) {
        Log.d(TAG, "Initializing Firebase Storage for image upload.");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + parcelNumber + ".jpg");

        Log.d(TAG, "Converting Bitmap to ByteArrayOutputStream.");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        Log.d(TAG, "Starting image upload.");
        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Log.d(TAG, "Image upload successful. Attempting to retrieve download URL.");
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(downloadUri -> {
                String imageUrl = downloadUri.toString();
                Log.d(TAG, "Image URL retrieved successfully: " + imageUrl);
                saveRequestToFirebase(parcelNumber, imageUrl);
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to retrieve image download URL.", e);
            });
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Image upload failed.", e);
        });

        uploadTask.addOnProgressListener(taskSnapshot -> {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
            Log.d(TAG, String.format("Upload progress: %.2f%% done", progress));
        });
    }



    private void saveRequestToFirebase(String parcelNumber, @Nullable String imageUrl) {
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


            // Create a HashMap to store the data
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

            if (imageUrl != null) {
                requestData.put("imageUrl", imageUrl);
            }

            databaseReference.child("requests").push().setValue(requestData)
                    .addOnSuccessListener(aVoid -> {
                        // Call showNotificationForRequest() here after successful submission

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

        // Additional methods (e.g., DatePickerDialog)...
    }

    private void showNotificationForRequest() {
        int importance = NotificationManager.IMPORTANCE_HIGH; // For the channel creation
        Log.d("NotificationDebug", "Attempting to show notification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Consider requesting the permission here or informing the user
            Log.d(TAG, "POST_NOTIFICATIONS permission not granted.");
            return;
        }

        // Proceed with showing the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "reminders")
                .setSmallIcon(R.drawable.deliveroo)
                .setContentTitle("Request Submitted")
                .setContentText("Your request is awaiting a driver to pick up.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH); // For the notification itself

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request permission logic here
        } else {
            notificationManager.notify(1, builder.build());
        }
    }

    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showImageSourceDialog() {
        CharSequence[] items = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // Use getActivity() here
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // User chose "Take Photo"
                    // Check for camera permission before taking a photo
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted, request it
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                    } else {
                        // Permission is already granted, proceed to take a photo
                        camera.takePhoto();
                    }
                } else if (which == 1) {
                    // User chose "Choose from Gallery"
                    openImagePicker();
                }
            }
        });
        builder.show();
    }

}