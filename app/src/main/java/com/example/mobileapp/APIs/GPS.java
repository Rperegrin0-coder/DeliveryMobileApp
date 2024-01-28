package com.example.mobileapp.APIs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GPS {
    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;

    public GPS(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Check if location permissions are granted
    public boolean isPermissionGranted() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Get the current location of the device
    public void getCurrentLocation(OnSuccessListener<Location> onSuccessListener) {
        if (isPermissionGranted()) {
            try {
                fusedLocationClient.getLastLocation().addOnSuccessListener(onSuccessListener);
            } catch (SecurityException e) {
                // Handle the SecurityException
                // Log the exception, inform the user, etc.
            }
        } else {
            // Handle the case where permission is not granted
            // You might want to show a dialog or notification asking the user to grant permissions
        }
    }

    // Add any additional GPS-related methods you need
}