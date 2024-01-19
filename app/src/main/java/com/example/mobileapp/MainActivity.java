package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;


import android.widget.LinearLayout;

import com.example.mobileapp.Helper.HelperSignupActivity;
import com.example.mobileapp.LoginManager.LoginManager;
import com.example.mobileapp.Requestor.RequestorSignUp.RequestorSignupActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private LoginManager loginManager;

    private Button loginButton;
    private Button signupButton;
    private FirebaseAuth mAuth;
    // You would also have EditTexts for email and password if your design requires them.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        loginManager = new LoginManager(); // Initialize LoginManager

        // Initialize buttons
        signupButton = findViewById(R.id.signupButton);
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
        final LinearLayout signupOptions = findViewById(R.id.signupOptions);

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



    }}