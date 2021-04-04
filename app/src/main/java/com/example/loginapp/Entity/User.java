package com.example.loginapp.Entity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

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


    public boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(boolean Admin) {
        Admin = Admin;
    }


    public boolean getDisabled() {
        return Disabled;
    }

    public void setDisabled(boolean Disabled) {
        Disabled = Disabled;
    }

    public int getCurrentQueue() {
        return currentQueue;
    }

    public void setCurrentQueue(int currentQueue) {
        this.currentQueue = currentQueue;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCurrentClinic() {
        return currentClinic;
    }

    public void setCurrentClinic(String currentClinic) {
        this.currentClinic = currentClinic;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserId() {return userId;}

    public void setUserId(String userId){this.userId=userId;}

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isClinicAdmin() {
        return clinicAdmin;
    }

    public void setClinicAdmin(boolean clinicAdmin) {
        this.clinicAdmin = clinicAdmin;
    }

    public String getClinicID() {
        return clinicID;
    }

    public void setClinicID(String clinicID) {
        this.clinicID = clinicID;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

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
