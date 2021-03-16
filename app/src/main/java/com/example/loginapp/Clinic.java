package com.example.loginapp;

public class Clinic extends MedicalLocation {

    private String ClinicName;
    private long Block;
    private long Floor;
    private long Postal;
    private String Streetname;
    private String Unitnumber;
    private long Telephone;
    private int ClinicCurrentQ;
    private static String startTime = "08:00:00";
    private static String closingTime = "20:00:00";
    private String ClinicID;


    public Clinic() {

    }

    public Clinic(float Latitude, float Longitude, String clinicName, long block, long floor,
                  long postal, String streetname, String unitnumber, long telephone, int clinicCurrentQ,
                  String startTime,
                  String closingTime, String clinicID) {
        super(Latitude, Longitude);
        this.ClinicName = clinicName;
        this.Block = block;
        this.Floor = floor;
        this.Postal = postal;
        this.Streetname = streetname;
        this.Unitnumber = unitnumber;
        this.Telephone = telephone;
        this.ClinicCurrentQ = clinicCurrentQ;
        this.startTime = startTime;
        this.closingTime = closingTime;
        this.ClinicID = clinicID;
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

    public String getUnitnumber() {
        return Unitnumber;
    }

    public void setUnitnumber(String unitnumber) {
        this.Unitnumber = unitnumber;
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

    public String getStartTime() {
        return startTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public String getClinicID() {
        return ClinicID;
    }

    public void setClinicID(String clinicID) {
        ClinicID = clinicID;
    }

}


