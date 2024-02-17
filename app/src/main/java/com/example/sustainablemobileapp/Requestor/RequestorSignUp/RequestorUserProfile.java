package com.example.sustainablemobileapp.Requestor.RequestorSignUp;

public class RequestorUserProfile {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String username; // Optional, depending on your app's design

    private  String role;

    public RequestorUserProfile() {
        // Default constructor for Firebase
    }

    public RequestorUserProfile(String fullName, String email, String phoneNumber, String username, String role) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.role = role;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
