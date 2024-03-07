package com.example.sustainablemobileapp.Requestor.RequestorSignUp;


import static android.app.PendingIntent.getActivity;

import com.example.sustainablemobileapp.R;

import com.example.sustainablemobileapp.MainActivity;

import  com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestorSignupActivity extends AppCompatActivity {

    private EditText fullName, email, phoneNumber, username, password, confirmPassword;
    private Button buttonSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Button buttonBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requestorsignupactivity); // Ensure you have a layout file for requestors
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize fields
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        buttonSignUp = findViewById(R.id.signUpButton);

        buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from EditTexts and validate them
                final String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                // Additional validation code

                // Firebase sign up
                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(RequestorSignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success, store additional user data
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    // Create a RequestorUserProfile with the necessary data
                                    RequestorUserProfile requestorProfile = new RequestorUserProfile(
                                            fullName.getText().toString(),
                                            email.getText().toString(),
                                            phoneNumber.getText().toString(),
                                            username.getText().toString(),
                                            "requestor"
                                    );

                                    mDatabase.child("RequestorUsers").child(user.getUid()).setValue(requestorProfile)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Log and navigate to home page
                                                    Log.d("RequestorSignupActivity", "User data stored successfully for: " + user.getUid());
                                                    Intent intent = new Intent(RequestorSignupActivity.this, MainActivity.class); // Change to your main activity for requestors
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Log failure
                                                    Log.e("RequestorSignupActivity", "Failed to store user data", e);
                                                }
                                            });

                                } else {
                                    // If sign up fails, display a detailed error message
                                    if (task.getException() != null) {
                                        Toast.makeText(RequestorSignupActivity.this, "Sign up failed: " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
    private void back() {
        // Navigate back to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // If you want to close the signup activity
    }

}