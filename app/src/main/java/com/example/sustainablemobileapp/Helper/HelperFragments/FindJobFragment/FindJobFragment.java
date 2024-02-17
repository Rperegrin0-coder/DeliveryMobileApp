package com.example.sustainablemobileapp.Helper.HelperFragments.FindJobFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sustainablemobileapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindJobFragment extends Fragment {

    private RecyclerView recyclerView;
    private DeliveryRequestAdapter adapter;
    private List<DeliveryRequest> deliveryRequests;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.helper_fragment_findjob, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter with the onItemClickListener
        adapter = new DeliveryRequestAdapter(deliveryRequests, new DeliveryRequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DeliveryRequest selectedRequest = deliveryRequests.get(position);
                onJobSelected(selectedRequest); // Use the selectedRequest as needed
            }
        });

        recyclerView.setAdapter(adapter);

        // Load delivery requests
        loadDeliveryRequests();

        return view;
    }



    private void loadDeliveryRequests() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (deliveryRequests == null) {
                    deliveryRequests = new ArrayList<>();
                }
                deliveryRequests.clear(); // Clear the current list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DeliveryRequest deliveryRequest = snapshot.getValue(DeliveryRequest.class);
                    deliveryRequests.add(deliveryRequest); // Add the request to the list
                }
                if (adapter == null) {
                    // Initialize the adapter with the onItemClickListener correctly
                    adapter = new DeliveryRequestAdapter(deliveryRequests, new DeliveryRequestAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            DeliveryRequest selectedRequest = deliveryRequests.get(position);
                            onJobSelected(selectedRequest); // Correctly use the position to handle the click event
                        }
                    });
                    recyclerView.setAdapter(adapter); // Set the adapter to the RecyclerView
                } else {
                    adapter.notifyDataSetChanged(); // Notify the adapter if the data set changed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log the error
                Log.e("FirebaseError", "Database operation cancelled, Error: " + databaseError.getMessage());

                // Check for permission denied error
                if (databaseError.getCode() == DatabaseError.PERMISSION_DENIED) {
                    // Handle the permission denied error
                    Toast.makeText(getContext(), "Access denied. You can't view this data.", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
    private void onJobSelected(DeliveryRequest job) {
        // Handler for when a job is selected from the list
        // Show details or confirmation dialog
    }

    // Adapter class and any other necessary classes here
}