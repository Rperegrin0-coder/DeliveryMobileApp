package com.example.sustainablemobileapp.Helper.HelperSignup;

public class HelperUserProfile {
    private  String role;
    private String fullName;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean bicycle;
    private boolean walking;
    private boolean electricScooter;

    // Default no-argument constructor
    public HelperUserProfile() {
    }

    // Constructor with arguments
    public HelperUserProfile(String fullName, String dateOfBirth, String email, String phoneNumber, String address, boolean bicycle, boolean walking, boolean electricScooter, String role) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bicycle = bicycle;
        this.walking = walking;
        this.electricScooter = electricScooter;
        this.role = role;

    }

    // Public getters and setters
    public String getFullName() {
        return fullName;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isBicycle() {
        return bicycle;
    }

    public void setBicycle(boolean bicycle) {
        this.bicycle = bicycle;
    }

    public boolean isWalking() {
        return walking;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }

    public boolean isElectricScooter() {
        return electricScooter;
    }

    public void setElectricScooter(boolean electricScooter) {
        this.electricScooter = electricScooter;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
