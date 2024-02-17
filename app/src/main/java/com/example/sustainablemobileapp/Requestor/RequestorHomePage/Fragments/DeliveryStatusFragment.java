package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

public class DeliveryStatusFragment extends Fragment {
    private RecyclerView deliveryStatusRecyclerView;
    private DeliveryStatusAdapter adapter;

    private List<DeliveryStatus> deliveryStatusList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.requestor_fragment_delivery_status, container, false);
        deliveryStatusRecyclerView = view.findViewById(R.id.deliveryStatusRecyclerView);
        deliveryStatusRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your adapter with some dummy data or actual data if available
        //deliveryStatusList = createDummyData();



        adapter = new DeliveryStatusAdapter(deliveryStatusList, new DeliveryStatusAdapter.OnDetailsButtonClickListener() {
            @Override
            public void onViewDetailsButtonClick(DeliveryStatus deliveryStatus) {
                showDetails(deliveryStatus);
            }
        });

        deliveryStatusRecyclerView.setAdapter(adapter);

        return view;
    }

    private void fetchDeliveryRequests() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryStatusList.clear(); // Clear the current list
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    DeliveryStatus deliveryStatus = childSnapshot.getValue(DeliveryStatus.class);
                    if (deliveryStatus != null) {
                        deliveryStatusList.add(deliveryStatus);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter of the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }


//    private List<DeliveryStatus> createDummyData() {
//        // Dummy data for testing
//        List<DeliveryStatus> list = new ArrayList<>();
//        list.add(new DeliveryStatus("123456", "Awaiting driver pickup"));
//        // Add more dummy data if necessary
//        return list;
//    }

    private void showDetails(DeliveryStatus deliveryStatus) {
        DeliveryStatusDialogFragment dialogFragment = DeliveryStatusDialogFragment.newInstance(deliveryStatus);
        dialogFragment.show(getActivity().getFragmentManager(), "deliveryDetails"); // For AppCompatActivity
        // Or use getActivity().getFragmentManager() for Activity
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new DeliveryStatusAdapter(deliveryStatusList, this::showDetails);
        deliveryStatusRecyclerView.setAdapter(adapter);
        fetchDeliveryRequests(); // Call this in onViewCreated or a similar lifecycle method
    }



}