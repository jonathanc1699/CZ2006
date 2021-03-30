package com.example.loginapp.Entity;

public class DistanceClinicToMe {
    public String ClinicName;
    public double Distance;

    public DistanceClinicToMe(String title, double computeDistanceBetween) {
        this.ClinicName = title;
        this.Distance = computeDistanceBetween;
    }

    public void DistanceClinicToMe(String ClinicName, double Distance){
        this.ClinicName = ClinicName;
        this.Distance = Distance;
    }

    public String getClinicName(){
        return this.ClinicName;
    }
    public double getDistance() {
        return this.Distance;
    }}


