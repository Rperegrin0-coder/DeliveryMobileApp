package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.sustainablemobileapp.R;

public class AddressDetailsDialogFragment extends DialogFragment {
    // Declare EditText fields for detailed address input
    private EditText addressLine1;
    private EditText addressLine2;
    private EditText city;
    private EditText state;
    private EditText postcode;
    private EditText country;

    public static AddressDetailsDialogFragment newInstance(String addressType) {
        AddressDetailsDialogFragment fragment = new AddressDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString("addressType", addressType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requestor_dialog_address_details, container, false);

        // Initialize EditText fields
        addressLine1 = view.findViewById(R.id.addressLine1);
        addressLine2 = view.findViewById(R.id.addressLine2);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state); // Assuming you've added an EditText with id state in your XML
        postcode = view.findViewById(R.id.postcode);
        country = view.findViewById(R.id.country); // Assuming you've added an EditText with id country in your XML
        Button saveAddressDetailsButton = view.findViewById(R.id.saveAddressDetailsButton);

        saveAddressDetailsButton.setOnClickListener(v -> saveAddressDetails());

        return view;
    }

    private void saveAddressDetails() {
        // Gather data from EditText fields
        String line1 = addressLine1.getText().toString();
        String line2 = addressLine2.getText().toString();
        String cityText = city.getText().toString();
        String stateText = state.getText().toString();
        String postcodeText = postcode.getText().toString();
        String countryText = country.getText().toString();

        // Concatenate your address details
        StringBuilder detailedAddressBuilder = new StringBuilder();
        detailedAddressBuilder.append(line1);
        if (!line2.isEmpty()) detailedAddressBuilder.append(", ").append(line2);
        detailedAddressBuilder.append(", ").append(cityText);
        detailedAddressBuilder.append(", ").append(stateText);
        detailedAddressBuilder.append(", ").append(postcodeText);
        detailedAddressBuilder.append(", ").append(countryText);
        String detailedAddress = detailedAddressBuilder.toString();

        // Prepare data to send back to the RequestsFragment
        Intent intent = new Intent();
        Bundle args = getArguments();
        if (args != null) {
            intent.putExtra("addressType", args.getString("addressType"));
        }
        intent.putExtra("detailedAddress", detailedAddress);

        // Send the data back to the RequestsFragment
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }

        // Close the dialog
        dismiss();
    }

    // Rest of your AddressDetailsDialogFragment...
}
