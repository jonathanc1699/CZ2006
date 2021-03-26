package com.example.loginapp.Entity;

public class MedicalLocation {
    private double Latitude;
    private double Longitude;

    public MedicalLocation(double Latitude, double Longitude)
    {
        this.Latitude=Latitude;
        this.Longitude = Longitude;
    }

    public MedicalLocation() {

    }

    public double getLongitude() {
        return this.Longitude;
    }

    public void setLongitude(long Longitude) {
        this.Latitude = Longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(long Latitude) {
        this.Latitude = Latitude;
    }

}
