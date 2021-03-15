package com.example.loginapp;

public class MedicalLocation {
    private float Latitude;
    private float Longitude;

    public MedicalLocation(float Latitude, float Longitude)
    {
        this.Latitude=Latitude;
        this.Longitude = Longitude;
    }

    public MedicalLocation() {

    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float Longitude) {
        this.Latitude = Longitude;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float Latitude) {
        this.Latitude = Latitude;
    }

}
