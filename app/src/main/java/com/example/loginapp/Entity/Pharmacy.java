package com.example.loginapp.Entity;

/**
 * This class implements the Pharmacy entity with the attributes pharmacy_address, pharmacy_name, pharmacy_ID
 *
 * This class extends from MedicalLocation class.
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class Pharmacy extends MedicalLocation{

    private String pharmacy_address;
    private String pharmacy_name;
    private String pharmacy_ID;

    /**
     * Constructor for Pharmacy.
     * @param Latitude latitude of pharmacy
     * @param Longitude Longitude of pharmacy
     * @param pharmacy_ID Pharmacy's ID
     * @param pharmacy_name Pharmacy's name
     * @param pharmacy_address Pharmacy's address
     */
    public Pharmacy(double Latitude, double Longitude, String pharmacy_ID, String pharmacy_name, String pharmacy_address) {
        super(Latitude, Longitude);
        this.pharmacy_address=pharmacy_address;
        this.pharmacy_name =pharmacy_name;
        this.pharmacy_ID=pharmacy_ID;
    }

    public Pharmacy() {
    }

    /**
     * Get pharmacy's ID
     * @return Pharmacy's ID
     */
    public String getPharmacy_ID() {
        return pharmacy_ID;
    }

    /**
     * Get pharmacy's address
     * @return pharmacy's address
     */
    public String getPharmacy_address() {
        return pharmacy_address;
    }

    /**
     * Get pharmacy's name
     * @return pharmacy's name
     */
    public String getPharmacy_name() {
        return pharmacy_name;
    }

    /**
     * Set pharmacy's address
     * @param pharmacy_address pharmacy's address
     */
    public void setPharmacy_address(String pharmacy_address) {
        this.pharmacy_address = pharmacy_address;
    }

    /**
     * Set pharmacy's name
     * @param pharmacy_name pharmacy's name
     */
    public void setPharmacy_name(String pharmacy_name) {
        this.pharmacy_name = pharmacy_name;
    }

    /**
     * Set pharmacy's ID
     * @param pharmacy_ID pharmacy's ID
     */

    public void setPharmacy_ID(String pharmacy_ID) {
        this.pharmacy_ID = pharmacy_ID;
    }
}


