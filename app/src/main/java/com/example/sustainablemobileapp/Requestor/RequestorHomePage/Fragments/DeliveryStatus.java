package com.example.sustainablemobileapp.Requestor.RequestorHomePage.Fragments;
import java.io.Serializable;
public class DeliveryStatus implements Serializable {

    private String orderNumber; // Field for storing the order number
    private String status; // Field for storing the delivery status
    private String additionalInstructions;
    private String deliveryAddress;
    private String deliveryDateTime;
    private String deliveryRecipientName;
    private String deliveryRecipientPhone;
    private boolean isFragile;
    private String parcelDescription;
    private String parcelNumber;
    private String parcelSize;
    private String parcelWeight;
    private String pickupAddress;
    private String pickupContactName;
    private String pickupContactPhone;
    private String pickupDateTime;

    // Default no-arg constructor
    public DeliveryStatus() {
        // No-arg constructor required for Firebase and Serializable
    }


    // Getter for the orderNumber field
    public String getOrderNumber() {
        return orderNumber;
    }

    // Setter for the orderNumber field
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    // Getter for the status field
    public String getStatus() {
        return status;
    }

    // Getters and Setters
    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }



    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getDeliveryRecipientName() {
        return deliveryRecipientName;
    }

    public void setDeliveryRecipientName(String deliveryRecipientName) {
        this.deliveryRecipientName = deliveryRecipientName;
    }

    public String getDeliveryRecipientPhone() {
        return deliveryRecipientPhone;
    }

    public void setDeliveryRecipientPhone(String deliveryRecipientPhone) {
        this.deliveryRecipientPhone = deliveryRecipientPhone;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }

    public String getParcelDescription() {
        return parcelDescription;
    }

    public void setParcelDescription(String parcelDescription) {
        this.parcelDescription = parcelDescription;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getParcelSize() {
        return parcelSize;
    }

    public void setParcelSize(String parcelSize) {
        this.parcelSize = parcelSize;
    }

    public String getParcelWeight() {
        return parcelWeight;
    }

    // Assuming the weight is stored as a string with "kg" appended, you may want to parse it
    public double getParcelWeightAsDouble() {
        if (parcelWeight != null && !parcelWeight.isEmpty()) {
            return Double.parseDouble(parcelWeight.replaceAll("[^\\d.]", ""));
        }
        return 0;
    }

    public void setParcelWeight(String parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupContactName() {
        return pickupContactName;
    }

    public void setPickupContactName(String pickupContactName) {
        this.pickupContactName = pickupContactName;
    }

    public String getPickupContactPhone() {
        return pickupContactPhone;
    }

    public void setPickupContactPhone(String pickupContactPhone) {
        this.pickupContactPhone = pickupContactPhone;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    // Additional helper methods and business logic can be added as needed
}
