package com.example.sustainablemobileapp.Helper.HelperFragments.FindJobFragment;

public class DeliveryRequest {
    private String id;
    private String parcelDescription;
    private String parcelWeight;
    private String pickupAddress;
    private String pickupContactName;
    private String pickupContactPhone;
    private String deliveryAddress;
    private String deliveryRecipientName;
    private String deliveryRecipientPhone;
    private String pickupDateTime;
    private String deliveryDateTime;
    private String additionalInstructions;

    // Constructor
    public DeliveryRequest() {
        // Default constructor required for Firebase
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParcelDescription() {
        return parcelDescription;
    }

    public void setParcelDescription(String parcelDescription) {
        this.parcelDescription = parcelDescription;
    }

    public String getParcelWeight() {
        return parcelWeight;
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

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }
}
