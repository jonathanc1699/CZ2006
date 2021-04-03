package com.example.loginapp.Entity;

public class DistanceClinicToMe {
    private String ClinicName;
    private double Distance;
    private String ClinicID;

    public DistanceClinicToMe(String ClinicID, double computeDistanceBetween, String title) {
        this.ClinicName = title;
        this.Distance = computeDistanceBetween;
        this.ClinicID = ClinicID;
    }

    public void setDistanceClinicToMe(String ClinicName, double Distance, String ClinicID){
        this.ClinicName = ClinicName;
        this.Distance = Distance;
        this.ClinicID = ClinicID;
    }

    public String getClinicName(){
        return this.ClinicName;
    }
    public double getDistance() {
        return this.Distance;
    }
    public String getClinicID(){ return this.ClinicID;}
}


