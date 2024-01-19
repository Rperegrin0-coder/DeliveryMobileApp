package com.example.mobileapp.LoginManager;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobileapp.Helper.HelperHomeActivity;
import com.example.mobileapp.Requestor.RequestorHomePage.RequestorHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginManager {
    private FirebaseAuth mAuth;

    public LoginManager() {
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    }

    public void handleLogin(Activity activity, EditText emailEditText, EditText passwordEditText) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the email and password fields (if needed)

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "Login successful");

                            // Check if the user is a Requestor or a Helper
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                            DatabaseReference helperUsersRef = FirebaseDatabase.getInstance().getReference()
                                    .child("HelperUsers")
                                    .child(userId);

                            DatabaseReference requestorUsersRef = FirebaseDatabase.getInstance().getReference()
                                    .child("RequestorUsers")
                                    .child(userId);

                            helperUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot helperSnapshot) {
                                    if (helperSnapshot.exists()) {
                                        // User is a Helper
                                        Log.d("LoginActivity", "Redirecting to HelperHomeActivity");
                                        Intent intent = new Intent(activity, HelperHomeActivity.class);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    } else {
                                        requestorUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot requestorSnapshot) {
                                                if (requestorSnapshot.exists()) {
                                                    // User is a Requestor
                                                    Log.d("LoginActivity", "Redirecting to RequestorHomeActivity");
                                                    Intent intent = new Intent(activity, RequestorHomeActivity.class);
                                                    activity.startActivity(intent);
                                                    activity.finish();
                                                } else {
                                                    // User role not found
                                                    Log.e("LoginActivity", "User role not found");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.e("LoginActivity", "Failed to read user role: " + error.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("LoginActivity", "Failed to read user role: " + error.getMessage());
                                }
                            });
                        } else {
                            Log.e("LoginActivity", "Login failed: " + task.getException().getMessage());
                            Toast.makeText(activity, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
