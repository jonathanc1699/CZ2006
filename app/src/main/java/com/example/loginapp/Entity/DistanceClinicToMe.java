package com.example.loginapp.Entity;

/**
 * This class implements the DistanceClinicToMe entity with the attributes ClinicName, Distance, ClinicID
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */
public class DistanceClinicToMe {
    private String ClinicName;
    private double Distance;
    private String ClinicID;

    /**
     * Constructor for DistanceClinicToMe.
     * @param ClinicID Clinic's ID
     * @param computeDistanceBetween Distance between current location and clinic
     * @param title Clinic name
     */
    public DistanceClinicToMe(String ClinicID, double computeDistanceBetween, String title) {
        this.ClinicName = title;
        this.Distance = computeDistanceBetween;
        this.ClinicID = ClinicID;
    }

    /**
     * Get Clinic's name
     * @return clinic's name
     */
    public String getClinicName(){
        return this.ClinicName;
    }

    /**
     * Get distance between current location and clinic
     * @return distance between current location and clinic
     */
    public double getDistance() {
        return this.Distance;
    }

    /**
     * Get Clinic's ID
     * @return Clinic's ID
     */
    public String getClinicID(){ return this.ClinicID;}
}


