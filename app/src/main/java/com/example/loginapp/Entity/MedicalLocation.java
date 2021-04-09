package com.example.loginapp.Entity;

/**
 * This class implements the MedicalLocation entity with the attributes Longitude and Latitude
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */
public class MedicalLocation {
    private double Latitude;
    private double Longitude;

    /**
     * Constructor for MedicalLocation.
     * @param Latitude Clinic's Latitude
     * @param Longitude Clinic's longitude
     */
    public MedicalLocation(double Latitude, double Longitude)
    {
        this.Latitude=Latitude;
        this.Longitude = Longitude;
    }

    public MedicalLocation() {

    }

    /**
     * Get Clinic's longitude
     * @return Clinic's longitude
     */
    public double getLongitude() {
        return this.Longitude;
    }

    /**
     * Set Clinic's longitude
     * @param Longitude Clinic's longitude
     */

    public void setLongitude(long Longitude) {
        this.Latitude = Longitude;
    }

    /**
     * Get Clinic's latitude
     * @return Clinic's latitude
     */
    public double getLatitude() {
        return Latitude;
    }

    /**
     * Set Clinic's latitude
     * @param Latitude Clinic's latitude
     */
    public void setLatitude(long Latitude) {
        this.Latitude = Latitude;
    }

}
