package com.example.loginapp;

import java.time.LocalTime;

public class Clinic extends MedicalLocation{

    private String ClinicName;
    private String Block;
    private String Floor;
    private int Postal;
    private String Streetname;
    private String UnitNumber;
    private int Telephone;
    private int ClinicCurrentQ;
    private LocalTime startTime;
    private LocalTime closingTime;


    public Clinic(float Latitude, float Longitude, String ClinicName, String Block, String Floor,
                  int Postal, String Streetname, String UnitNumber, int Telephone, int ClinicCurrentQ,
                  LocalTime startTime, LocalTime closingTime ) {
        super(Latitude, Longitude);
        this.ClinicName = ClinicName;
        this.Block = Block;
        this.Floor = Floor;
        this.Postal = Postal;
        this.Streetname = Streetname;
        this.UnitNumber = UnitNumber;
        this.Telephone = Telephone;
        this.ClinicCurrentQ =ClinicCurrentQ;
        this.closingTime = closingTime;
        this.startTime =startTime;

    }

    public String getClinicName() {
        return ClinicName;
    }

    public String getBlock() {
        return Block;
    }

    public String getFloor() {
        return Floor;
    }

    public int getPostal() {
        return Postal;
    }

    public String getStreetname() {
        return Streetname;
    }

    public String getUnitNumber() {
        return UnitNumber;
    }

    public int getTelephone() {
        return Telephone;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public void setPostal(int postal) {
        Postal = postal;
    }

    public void setStreetname(String streetname) {
        Streetname = streetname;
    }

    public void setUnitNumber(String unitNumber) {
        UnitNumber = unitNumber;
    }

    public void setTelephone(int telephone) {
        Telephone = telephone;
    }

    public int getClinicCurrentQ() {
        return ClinicCurrentQ;
    }

    public void setClinicCurrentQ(int clinicCurrentQ) {
        ClinicCurrentQ = clinicCurrentQ;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
