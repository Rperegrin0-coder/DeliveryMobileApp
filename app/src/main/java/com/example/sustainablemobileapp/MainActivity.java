package com.example.sustainablemobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.Manifest;

import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.sustainablemobileapp.R;
import com.example.sustainablemobileapp.Helper.HelperSignup.HelperSignupActivity;
import com.example.sustainablemobileapp.LoginManager.LoginManager;
import com.example.sustainablemobileapp.Requestor.RequestorSignUp.RequestorSignupActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private LoginManager loginManager;

    private Button loginButton;
    private Button signupButton;
    private FirebaseAuth mAuth;
    public static final String CHANNEL_ID = "request_channel_id";
    // You would also have EditTexts for email and password if your design requires them.

    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animateTextViewSideToSide();

        createNotificationChannel();


        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        loginManager = new LoginManager(); // Initialize LoginManager


        // Initialize buttons
        signupButton = findViewById(R.id.signupButton);
        LinearLayout signupOptions = findViewById(R.id.roleToggleGroup);
        loginButton = findViewById(R.id.loginButton);
        final Button innerLoginButton = findViewById(R.id.submitLoginButton); // The button inside the layout


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout loginDetailsLayout = findViewById(R.id.loginDetailsLayout);
                if (loginDetailsLayout.getVisibility() == View.GONE) {
                    loginDetailsLayout.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE); // Hide the original login button
                } else {
                    loginDetailsLayout.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE); // Show the original login button
                }
            }
        });

// Set the OnClickListener for the inner login button
        innerLoginButton.setOnClickListener(new View.OnClickListener() {
            EditText emailEditText = findViewById(R.id.emailEditText);
            EditText passwordEditText = findViewById(R.id.passwordEditText);

            @Override
            public void onClick(View v) {
                // Use LoginManager to handle login
                loginManager.handleLogin(MainActivity.this, emailEditText, passwordEditText);
            }
        });


        // Inside your onCreate method
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the visibility of the signupOptions layout
                if (signupOptions.getVisibility() == View.GONE) {
                    signupOptions.setVisibility(View.VISIBLE);
                } else {
                    signupOptions.setVisibility(View.GONE);
                }
            }
        });

// Get the buttons inside the LinearLayout
        Button requestorOption = findViewById(R.id.requestorOption);
        Button helperOption = findViewById(R.id.helperOption);

// Set onClickListeners for the options
        requestorOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the RequestorSignupActivity
                Intent intent = new Intent(MainActivity.this, RequestorSignupActivity.class);
                startActivity(intent);
            }
        });


        helperOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelperSignupActivity.class);
                startActivity(intent);
            }
        });

        // Request POST_NOTIFICATIONS permission for Android 12 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the request code matches the one you used to request the permission
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed with your operation that requires this permission
                Log.d("MainActivity", "POST_NOTIFICATIONS permission granted");
                // You might want to call createNotificationChannel() here if it relies on this permission
            } else {
                // Permission was denied, handle the denial by informing the user, disabling features, etc.
                Log.d("MainActivity", "POST_NOTIFICATIONS permission denied");
                // You might disable features that require this permission
            }
        }

        // Handle other permission requests by checking other request codes, if applicable
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications";
            String description = "Delivery Notifications.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("reminders", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void animateTextViewSideToSide() {
        TextView taglineTextView = findViewById(R.id.tagline);

        // Calculate the distance to move (e.g., 100 pixels to the left and right from the current position)
        float distance = 100f;

        // Create an ObjectAnimator to animate the 'translationX' property
        ObjectAnimator animator = ObjectAnimator.ofFloat(taglineTextView, "translationX", -distance, distance);

        animator.setDuration(5000); // Duration of the animation (e.g., 1000 milliseconds for 1 second)
        animator.setRepeatCount(ValueAnimator.INFINITE); // Repeat indefinitely
        animator.setRepeatMode(ValueAnimator.REVERSE); // Reverse animation at the end so it goes back and forth

        animator.start(); // Start the animation
    }




}