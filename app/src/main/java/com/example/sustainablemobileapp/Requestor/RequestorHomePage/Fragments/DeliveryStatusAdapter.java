package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustainablemobileapp.R;

import java.util.List;

public class DeliveryStatusAdapter extends RecyclerView.Adapter<DeliveryStatusAdapter.ViewHolder> {
    private List<DeliveryStatus> deliveryStatusList;
    private OnDetailsButtonClickListener detailsButtonClickListener;

    public DeliveryStatusAdapter(List<DeliveryStatus> deliveryStatusList, OnDetailsButtonClickListener listener) {
        this.deliveryStatusList = deliveryStatusList;
        this.detailsButtonClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTextView;
        TextView deliveryStatusTextView;
        Button viewDetailsButton;

        TextView parcelDesc;

        public ViewHolder(View itemView) {
            super(itemView);
            orderNumberTextView = itemView.findViewById(R.id.orderNumberTextView);
            deliveryStatusTextView = itemView.findViewById(R.id.deliveryStatusTextView);
            parcelDesc = itemView.findViewById(R.id.parcelDesc);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestor_item_delivery_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeliveryStatus status = deliveryStatusList.get(position);

        holder.orderNumberTextView.setText("Order number: " + status.getParcelNumber());
        holder.parcelDesc.setText("Parcel Description: " + status.getParcelDescription());

        holder.viewDetailsButton.setOnClickListener(v -> {
            // Pass the deliveryStatus object to the method in the Fragment or Activity
            detailsButtonClickListener.onViewDetailsButtonClick(status);
        });
    }

    @Override
    public int getItemCount() {
        return deliveryStatusList != null ? deliveryStatusList.size() : 0;
    }

    public interface OnDetailsButtonClickListener {
        void onViewDetailsButtonClick(DeliveryStatus deliveryStatus);
    }


    // Additional methods...
}