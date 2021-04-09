package com.example.loginapp.Entity;

import com.google.firebase.firestore.PropertyName;
/**
 * This class implements the Clinic entity with the attributes ClinicName, Block, Floor, Postal, Streetname,
 * Unitnumber, Telephone, ClinicCurrentQ, startTime, closingTime, ClinicID, latestQNo
 *
 * This class extends from MedicalLocation class.
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class Clinic extends MedicalLocation {

    private String ClinicName;
    private Object Block;
    private Object Floor;
    private long Postal;
    private String Streetname;
    private Object Unitnumber;
    private long Telephone;
    private int ClinicCurrentQ;
    private static String startTime = "08:00:00";
    private static String closingTime = "20:00:00";
    private String ClinicID;

    private int latestQNo;



    public Clinic() {

    }

    /**
     * Constructor for Clinic.
     * @param clinicName clinic name
     */
    public Clinic(String clinicName) {
        this.ClinicName = clinicName;
    }

    /**
     * Constructor for Clinic
     * @param clinicName clinic's name
     * @param block clinic's block
     * @param floor clinic's floor
     * @param postal clinic's postal code
     * @param streetname clinic's street name
     * @param unitnumber clinic's unit number
     * @param telephone clinic's telephone
     * @param clinicCurrentQ clinic's currently serving queue number
     * @param startTime clinic's starting time / open hour
     * @param closingTime clinic's  closing time / closing hour
     * @param clinicID clinic's ID
     * @param Longitude clinic's longitude
     * @param Latitude clinic's latitude
     * @param latestqno clinic's latest queue number
     */
    public Clinic(String clinicName, long block, Object floor,
                  long postal, String streetname, Object unitnumber, long telephone, int clinicCurrentQ,
                  String startTime,
                  String closingTime, String clinicID, double Longitude, double Latitude, int latestqno) {

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

    /**
     * Constructor for Clinic
     * @param clinicID clinic's ID
     * @param clinicName clinic's name
     */
    public Clinic(String clinicID, String clinicName) {this.ClinicID = clinicID;
        this.ClinicName = clinicName;
    }

    /**
     * Constructor for Clinic
     * @param clinicID clinic's ID
     * @param clinicName clinic's name
     * @param Latitude clinic's Latitude
     * @param Longitude clinic's Longitude
     */
    public Clinic(String clinicID,String clinicName, double Latitude, double Longitude) {
        super(Latitude, Longitude);
        this.ClinicID=clinicID;
        this.ClinicName=clinicName;
    }

    /**
     * Get Clinic's name
     * @return clinic's name
     */
    @PropertyName("Clinic Name")
    public String getClinicName() {
        return ClinicName;
    }

    /**
     * Set clinic's name
     * @param clinicName clinic name
     */
    @PropertyName("Clinic Name")
    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    /**
     * Get clinic's block number
     * @return block number
     */
    public Object getBlock() {
        return Block;
    }

    /**
     * Set clinic's block number
     * @param block block number
     */
    public void setBlock(long block) {
        Block = block;
    }

    /**
     * Get clinic's floor number
     * @return floor number
     */
    @PropertyName("Floor ")
    public Object getFloor() {
        return Floor;
    }

    /**
     * Set clinic's floor number
     * @param floor floor number
     */
    @PropertyName("Floor ")
    public void setFloor(Object floor) {
        Floor = floor;
    }

    /**
     * Get clinic's postal code
     * @return clinic's postal code
     */
    public long getPostal() {
        return Postal;
    }

    /**
     * Set clinic's postal code
     * @param postal clinic's postal code
     */
    public void setPostal(long postal) {
        Postal = postal;
    }

    /**
     * Get clinic's street name
     * @return clinic's street name
     */
    public String getStreetname() {
        return Streetname;
    }

    /**
     * Set clinic's street name
     * @param streetname clinic's street name
     */
    public void setStreetname(String streetname) {
        Streetname = streetname;
    }

    /**
     * Get clinic's unit number
     * @return clinic's unit number
     */
    @PropertyName("Unit number")
    public Object getUnitnumber() {
        return Unitnumber;
    }

    /**
     * Set clinic's unit number
     * @param unitnumber unit number
     */
    @PropertyName("Unit number")
    public void setUnitnumber(Object unitnumber) {
        Unitnumber = unitnumber;
    }

    /**
     * Get clinic's telephone number
     * @return clinic's telephone number
     */
    @PropertyName("Telephone ")
    public long getTelephone() {
        return Telephone;
    }

    /**
     * Set clinic's telephone number
     * @param telephone clinic's telephone number
     */
    @PropertyName("Telephone ")
    public void setTelephone(long telephone) {
        Telephone = telephone;
    }

    /**
     * Get clinic's currently serving queue number
     * @return clinic's currently serving queue number
     */
    @PropertyName("ClinicCurrentQ")
    public int getClinicCurrentQ() {
        return ClinicCurrentQ;
    }

    /**
     * Set clinic's currently serving queue number
     * @param clinicCurrentQ clinic's currently serving queue number
     */
    @PropertyName("ClinicCurrentQ")
    public void setClinicCurrentQ(int clinicCurrentQ) {
        ClinicCurrentQ = clinicCurrentQ;
    }

    /**
     * Get clinic's ID
     * @return clinic's ID
     */
    public String getClinicID() {
        return ClinicID;
    }

    /**
     * Set clinic's ID
     * @param clinicID clinic's ID
     */
    public void setClinicID(String clinicID) {
        ClinicID = clinicID;
    }

    /**
     * Get clinic's start time
     * @return clinic's start time
     */
    public static String getStartTime() {
        return startTime;
    }

    /**
     * Get clinic's closing time
     * @return clinic's closing time
     */
    public static String getClosingTime() {
        return closingTime;
    }


    /**
     * Get clinic's latest queue number
     * @return clinic's latest queue number
     */
    @PropertyName("latestQNo")
    public int getLatestQNo() {
        return latestQNo;
    }

    /**
     * Set clinic's latest queue number
     * @param latestQNo clinic's latest queue number
     */

    @PropertyName("latestQNo")
    public void setLatestQNo(int latestQNo) {
        this.latestQNo = latestQNo;
    }

    /**
     * Clinic name to be displayed on Google maps based on it's longitude and latitude
     * @return Clinic name and it's longitude and latitude
     */
    public String toString(){
        String toReturn = "";
        toReturn += getClinicName();
        toReturn += ", ";
        toReturn += getLatitude();
        toReturn += getLatitude();
        return toReturn;
    }


}


