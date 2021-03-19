package com.example.loginapp;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.QuerySnapshot;

public class Clinic extends MedicalLocation {

    private String ClinicName;
    private long Block;
    private long Floor;
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

    public Clinic(String clinicName, long block, long floor,
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
    public long getFloor() {
        return Floor;
    }
    @PropertyName("Floor ")
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


