package com.example.loginapp.Entity;

public class Pharmacy extends MedicalLocation{

    private String pharmacy_address;
    private String pharmacy_name;
    private String pharmacy_ID;

    public Pharmacy(long Latitude, long Longitude, String pharmacy_ID, String pharmacy_name, String pharmacy_address) {
        super(Latitude, Longitude);
        this.pharmacy_address=pharmacy_address;
        this.pharmacy_name =pharmacy_name;
        this.pharmacy_ID=pharmacy_ID;
    }

    public String getPharmacy_ID() {
        return pharmacy_ID;
    }

    public String getPharmacy_address() {
        return pharmacy_address;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public void setPharmacy_address(String pharmacy_address) {
        this.pharmacy_address = pharmacy_address;
    }

    public void setPharmacy_name(String pharmacy_name) {
        this.pharmacy_name = pharmacy_name;
    }
}