package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.sustainablemobileapp.R;

public class DeliveryStatusDialogFragment extends DialogFragment {

    private static final String ARG_DELIVERY_STATUS = "delivery_status";
    private DeliveryStatus deliveryStatus;

    // Factory method to create a new instance of the fragment with delivery status data
    public static DeliveryStatusDialogFragment newInstance(DeliveryStatus deliveryStatus) {
        DeliveryStatusDialogFragment fragment = new DeliveryStatusDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DELIVERY_STATUS, deliveryStatus); // Ensure DeliveryStatus implements Serializable
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deliveryStatus = (DeliveryStatus) getArguments().getSerializable(ARG_DELIVERY_STATUS);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delivery_details, null);

        // Assuming deliveryStatus is correctly retrieved from arguments beforehand
        DeliveryStatus deliveryStatus = null;
        if (getArguments() != null) {
            deliveryStatus = (DeliveryStatus) getArguments().getSerializable(ARG_DELIVERY_STATUS);
        }

        if (view != null && deliveryStatus != null) {
            // Initialize TextViews from the layout
            TextView parcelDescriptionTextView = view.findViewById(R.id.r_parcelDescriptionTextView); // Ensure correct ID
            TextView deliveryDateTimeTextView = view.findViewById(R.id.deliveryDateTimeTextView); // Ensure correct ID

            // Set text to each TextView
            parcelDescriptionTextView.setText(deliveryStatus.getParcelDescription());
            deliveryDateTimeTextView.setText(deliveryStatus.getDeliveryDateTime());
            // Continue setting text for other details as needed
        } else {
            Log.e("DeliveryStatusDialog", "View or DeliveryStatus is null.");
            // Consider providing a fallback UI or message
        }

        // Setup the dialog
        builder.setView(view)
                .setTitle("Delivery Details")
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    // Handle the positive button action if needed
                });

        // Return the constructed AlertDialog
        return builder.create();
    }
}
