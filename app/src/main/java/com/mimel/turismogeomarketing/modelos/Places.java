package com.mimel.turismogeomarketing.modelos;

public class Places {
    String name;
    String description;
    String profilePhotoUrl;
    String paymentMethod;
    String longitud;
    String latitud;
    String city;
    String type;
    int score;

    public Places() {

    }

    public Places(String name, String description, String profilePhotoUrl, String paymentMethod, String longitud, String latitud, String city, String type) {
        this.name = name;
        this.description = description;
        this.profilePhotoUrl = profilePhotoUrl;
        this.paymentMethod = paymentMethod;
        this.longitud = longitud;
        this.latitud = latitud;
        this.city = city;
        this.type = type;
        this.score = 0;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLongitud() {
        return longitud;
    }

    public String getLatitud() {
        return latitud;
    }


    public void setLocation(String latitud, String  longitud) {
        this.latitud= latitud;
        this.longitud = longitud;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}