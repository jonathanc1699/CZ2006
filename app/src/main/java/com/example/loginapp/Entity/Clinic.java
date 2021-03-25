package com.example.loginapp.Entity;

import com.google.firebase.firestore.PropertyName;

public class Clinic extends MedicalLocation {

    private String ClinicName;
    private long Block;
    private Object Floor;
    private long Postal;
    private String Streetname;
    private Object Unitnumber;
    private long Telephone;
    private int ClinicCurrentQ;
    private static String startTime = "08:00:00";
    private static String closingTime = "22:00:00";
    private String ClinicID;

    private int latestQNo;



    public Clinic() {

    }
    public Clinic(String clinicName) {
        this.ClinicName = clinicName;
    }

    public Clinic(String clinicName, long block, Object floor,
                  long postal, String streetname, Object unitnumber, long telephone, int clinicCurrentQ,
                  String startTime,
                  String closingTime, String clinicID, long Longitude, long Latitude, int latestqno) {

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
        this.latestQNo = latestqno;
    }

    public Clinic(String clinicID, String clinicName) {this.ClinicID = clinicID;
        this.ClinicName = clinicName;
    }

    public Clinic(String clinicID,String clinicName, Long Latitude, Long Longitude) {
        super(Latitude, Longitude);
    }


    @PropertyName("Clinic Name")
    public String getClinicName() {
        return ClinicName;
    }
    @PropertyName("Clinic Name")
    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public long getBlock() {
        return Block;
    }

    public void setBlock(long block) {
        Block = block;
    }
    @PropertyName("Floor ")
    public Object getFloor() {
        return Floor;
    }
    @PropertyName("Floor ")
    public void setFloor(Object floor) {
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

    @PropertyName("Unit number")
    public Object getUnitnumber() {
        return Unitnumber;
    }
    @PropertyName("Unit number")
    public void setUnitnumber(Object unitnumber) {
        Unitnumber = unitnumber;
    }

    @PropertyName("Telephone ")
    public long getTelephone() {
        return Telephone;
    }
    @PropertyName("Telephone ")
    public void setTelephone(long telephone) {
        Telephone = telephone;
    }


    @PropertyName("ClinicCurrentQ")
    public int getClinicCurrentQ() {
        return ClinicCurrentQ;
    }


    @PropertyName("ClinicCurrentQ")
    public void setClinicCurrentQ(int clinicCurrentQ) {
        ClinicCurrentQ = clinicCurrentQ;
    }

    public String getClinicID() {
        return ClinicID;
    }

    public void setClinicID(String clinicID) {
        ClinicID = clinicID;
    }

    public static String getStartTime() {
        return startTime;
    }

    public static String getClosingTime() {
        return closingTime;
    }


    @PropertyName("latestQNo")
    public int getLatestQNo() {
        return latestQNo;
    }


    @PropertyName("latestQNo")
    public void setLatestQNo(int latestQNo) {
        this.latestQNo = latestQNo;
    }


}


