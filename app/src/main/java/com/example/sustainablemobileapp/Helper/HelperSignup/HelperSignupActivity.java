
package com.example.sustainablemobileapp.Helper.HelperSignup;

import com.example.sustainablemobileapp.MainActivity;

import com.example.sustainablemobileapp.R;
import com.google.android.gms.tasks.OnFailureListener;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HelperSignupActivity extends AppCompatActivity {

    private EditText fullName, dateOfBirth, email, phoneNumber, address, username, password, confirmPassword;
    private CheckBox checkboxBicycle, checkboxWalking, checkboxElectricScooter;
    private Button buttonSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpersignupactivity);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize fields
        fullName = findViewById(R.id.fullName);

        dateOfBirth = findViewById(R.id.dateOfBirth);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        checkboxBicycle = findViewById(R.id.checkboxBicycle);
        checkboxWalking = findViewById(R.id.checkboxWalking);
        checkboxElectricScooter = findViewById(R.id.checkboxElectricScooter);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from EditTexts and validate them
                final String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                // Additional validation code

                // Firebase sign up
                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(HelperSignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success, store additional user data
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    HelperUserProfile userProfile = new HelperUserProfile(
                                            fullName.getText().toString(),
                                            dateOfBirth.getText().toString(),
                                            email.getText().toString(),
                                            phoneNumber.getText().toString(),
                                            address.getText().toString(),
                                            checkboxBicycle.isChecked(),
                                            checkboxWalking.isChecked(),
                                            checkboxElectricScooter.isChecked(),
                                            "helper"

                                    );



                                    mDatabase.child("HelperUsers").child(user.getUid()).setValue(userProfile)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Log and navigate to home page
                                                    Log.d("HelperSignupActivity", "User data stored successfully for: " + user.getUid());
                                                    Intent intent = new Intent(HelperSignupActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Log failure
                                                    Log.e("HelperSignupActivity", "Failed to store user data", e);
                                                }
                                            });

                                } else {
                                    // If sign up fails, display a detailed error message
                                    if (task.getException() != null) {
                                        Toast.makeText(HelperSignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(HelperSignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }

        });
    }
}
