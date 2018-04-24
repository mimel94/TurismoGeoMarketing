package com.mimel.turismogeomarketing.modelos;

public class UserData {
    String city;
    String description;
    String profilePhotoUrl;
    String dateProfile;

    public UserData(){

    }

    public UserData(String city, String description, String profilePhotoUrl, String dateProfile) {
        this.city = city;
        this.description = description;
        this.profilePhotoUrl = profilePhotoUrl;
        this.dateProfile = dateProfile;
    }

    public String getDateProfile() {
        return dateProfile;
    }

    public void setDateProfile(String dateProfile) {
        this.dateProfile = dateProfile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

}