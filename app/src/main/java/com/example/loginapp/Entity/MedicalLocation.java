package com.example.loginapp.Entity;

public class MedicalLocation {
    private long Latitude;
    private long Longitude;

    public MedicalLocation(long Latitude, long Longitude)
    {
        this.Latitude=Latitude;
        this.Longitude = Longitude;
    }

    public MedicalLocation() {

    }

    public long getLongitude() {
        return Longitude;
    }

    public void setLongitude(long Longitude) {
        this.Latitude = Longitude;
    }

    public long getLatitude() {
        return Latitude;
    }

    public void setLatitude(long Latitude) {
        this.Latitude = Latitude;
    }

}
