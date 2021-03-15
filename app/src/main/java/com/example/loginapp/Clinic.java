package com.example.loginapp;

import java.time.LocalTime;

public class Clinic extends MedicalLocation {

    private String ClinicName;
    private long Block;
    private long Floor;
    private long Postal;
    private String Streetname;
    private String UnitNumber;
    private long Telephone;
    private int ClinicCurrentQ;
    private LocalTime startTime;
    private LocalTime closingTime;
    private String ClinicID;


    public Clinic() {
        super();

    }

    public Clinic(float Latitude, float Longitude, String clinicName, long block, long floor,
                  long postal, String streetname, String unitNumber, long telephone, int clinicCurrentQ, LocalTime startTime,
                  LocalTime closingTime, String clinicID) {
        super(Latitude, Longitude);
        ClinicName = clinicName;
        Block = block;
        Floor = floor;
        Postal = postal;
        Streetname = streetname;
        UnitNumber = unitNumber;
        Telephone = telephone;
        ClinicCurrentQ = clinicCurrentQ;
        this.startTime = startTime;
        this.closingTime = closingTime;
        ClinicID = clinicID;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public long getBlock() {
        return Block;
    }

    public void setBlock(long block) {
        Block = block;
    }

    public long getFloor() {
        return Floor;
    }

    public void setFloor(long floor) {
        Floor = floor;
    }

    public long getPostal() {
        return Postal;
    }

    public void setPostal(long postal) {
        Postal = postal;
    }

    public String getStreetname() {
        return Streetname;
    }

    public void setStreetname(String streetname) {
        Streetname = streetname;
    }

    public String getUnitNumber() {
        return UnitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        UnitNumber = unitNumber;
    }

    public long getTelephone() {
        return Telephone;
    }

    public void setTelephone(long telephone) {
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

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public String getClinicID() {
        return ClinicID;
    }

    public void setClinicID(String clinicID) {
        ClinicID = clinicID;
    }

}


