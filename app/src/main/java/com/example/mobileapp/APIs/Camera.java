package com.example.mobileapp.APIs;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class Camera {

    private Activity activity;
    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public Camera(Activity activity) {
        this.activity = activity;
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
