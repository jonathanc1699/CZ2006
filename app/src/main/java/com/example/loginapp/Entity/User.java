package com.example.loginapp.Entity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the user entity with
 * the attributes fullName, email, currentQueue, currentClinic, Admin, userId, Disabled, clinicAdmin
 * ,clinicID, clinicName
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */
@IgnoreExtraProperties
public class User {

    private String fullName;
    private String email;
    private int currentQueue;
    private String currentClinic;
    private boolean Admin;
    private String userId;
    private boolean Disabled;
    private boolean clinicAdmin;
    private String clinicID;
    private String clinicName;


    /**
     * Constructor for User.
     * @param fullName Name of the user
     * @param email user's email
     * @param currentQueue current queue of user's current appointment , if any
     * @param currentClinic clinic name of user's current appointment, if any
     * @param userId ID of user
     * @param Disabled whether user is a deleted user
     * @param Admin whether user is an admin
     * @param clinicAdmin whether user is a clinic admin
     * @param clinicID clinic ID
     * @param clinicName clinic name
     */
    public User(String fullName, String email, int currentQueue, String currentClinic,String userId, boolean Disabled, boolean Admin, boolean clinicAdmin, String clinicID, String clinicName) {
        this.fullName = fullName;
        this.email = email;
        this.currentQueue = currentQueue;
        this.currentClinic = currentClinic;
        this.userId=userId;
        this.Admin = Admin;
        this.Disabled = Disabled;
        this.clinicAdmin = clinicAdmin;
        this.clinicID = clinicID;
        this.clinicName = clinicName;

    }


    public User() {
    }

    /**
     * Get user's current queue
     * @return user's current queue
     */
    public int getCurrentQueue() {
        return currentQueue;
    }

    /**
     * Set current queue of user's current appointment
     * @param currentQueue current queue of user's current appointment
     */
    public void setCurrentQueue(int currentQueue) {
        this.currentQueue = currentQueue;
    }

    /**
     * Get user's registered email
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user's registered email
     * @param email user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Get clinic name of user's current appointment
     * @return clinic name
     */
    public String getCurrentClinic() {
        return currentClinic;
    }

    /**
     * Set clinic name of user's current appointment
     * @param currentClinic clinic name of user's current appointment
     */
    public void setCurrentClinic(String currentClinic) {
        this.currentClinic = currentClinic;
    }

    /**
     * Get user's full name
     * @return user's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Get user's ID
     * @return user's ID
     */
    public String getUserId() {return userId;}

    /**
     * Gets user account status on whether the user is a clinic admin or not
     * @return true if user is a clinic admin, false if user is not a clinic admin
     */
    public boolean isClinicAdmin() {
        return clinicAdmin;
    }

    /**
     * Get Clinic ID
     * @return Clinic ID
     */
    public String getClinicID() {
        return clinicID;
    }

    /**
     * Set Clinic ID
     * @param clinicID Clinic ID
     */
    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    /**
     * Get Clinic name of user's current appointment
     * @return Clinic name
     */
    public String getClinicName() {
        return clinicName;
    }

    /**
     * Map a custom user object into a Map data type
     * @return Map of a user object
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("fullName", fullName);
        result.put("currentQueue", currentQueue);
        result.put("currentClinic", currentClinic);
        result.put("email", email);
        result.put("Admin", Admin);
        result.put("Disabled", Disabled);
        result.put("clinicAdmin", clinicAdmin);
        result.put("clinicID", clinicID);
        result.put("clinicName", clinicName);

        return result;


    }


}
