


package com.example.sustainablemobileapp.Helper.HelperFragments.FindJobFragment;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustainablemobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRequestAdapter extends RecyclerView.Adapter<DeliveryRequestAdapter.ViewHolder> {

    private List<DeliveryRequest> deliveryRequests;
    private OnItemClickListener onItemClickListener;



    // Updated constructor
    public DeliveryRequestAdapter(List<DeliveryRequest> deliveryRequests, OnItemClickListener onItemClickListener) {
        this.deliveryRequests = deliveryRequests != null ? deliveryRequests : new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_fragment_findjob, parent, false);
        return new ViewHolder(view, onItemClickListener); // Pass the listener to the ViewHolder
    }

    public interface OnItemClickListener {
        void onItemClick(int position); // Pass position instead of DeliveryRequest object
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnItemClickListener onItemClickListener;
        TextView parcelDescriptionTextView;
        TextView parcelWeightTextView;
        TextView pickupAddressTextView;
        TextView pickupContactNameTextView;
        TextView pickupContactPhoneTextView;
        TextView deliveryAddressTextView;
        TextView deliveryRecipientNameTextView;
        TextView deliveryRecipientPhoneTextView;
        TextView pickupDateTimeTextView;
        TextView deliveryDateTimeTextView;
        TextView additionalInstructionsTextView;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            parcelDescriptionTextView = itemView.findViewById(R.id.parcelDescriptionTextView);
            parcelWeightTextView = itemView.findViewById(R.id.parcelWeightTextView);
            pickupAddressTextView = itemView.findViewById(R.id.pickupAddressTextView);
            pickupContactNameTextView = itemView.findViewById(R.id.pickupContactNameTextView);
            pickupContactPhoneTextView = itemView.findViewById(R.id.pickupContactPhoneTextView);
            deliveryAddressTextView = itemView.findViewById(R.id.deliveryAddressTextView);
            deliveryRecipientNameTextView = itemView.findViewById(R.id.deliveryRecipientNameTextView);
            deliveryRecipientPhoneTextView = itemView.findViewById(R.id.deliveryRecipientPhoneTextView);
            pickupDateTimeTextView = itemView.findViewById(R.id.pickupDateTimeTextView);
            deliveryDateTimeTextView = itemView.findViewById(R.id.deliveryDateTimeTextView);
            additionalInstructionsTextView = itemView.findViewById(R.id.additionalInstructionsTextView);

            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);


        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(position); // Notify using position

            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data to the views for each item
        DeliveryRequest deliveryRequest = deliveryRequests.get(position);
        holder.parcelDescriptionTextView.setText(deliveryRequest.getParcelDescription());
        holder.parcelWeightTextView.setText(String.valueOf(deliveryRequest.getParcelWeight()));
        holder.pickupAddressTextView.setText(deliveryRequest.getPickupAddress());
        holder.pickupContactNameTextView.setText(deliveryRequest.getPickupContactName());
        holder.pickupContactPhoneTextView.setText(deliveryRequest.getPickupContactPhone());
        holder.deliveryAddressTextView.setText(deliveryRequest.getDeliveryAddress());
        holder.deliveryRecipientNameTextView.setText(deliveryRequest.getDeliveryRecipientName());
        holder.deliveryRecipientPhoneTextView.setText(deliveryRequest.getDeliveryRecipientPhone());
        holder.pickupDateTimeTextView.setText(deliveryRequest.getPickupDateTime());
        holder.deliveryDateTimeTextView.setText(deliveryRequest.getDeliveryDateTime());
        holder.additionalInstructionsTextView.setText(deliveryRequest.getAdditionalInstructions());
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the data set
        return deliveryRequests.size();

    }



}