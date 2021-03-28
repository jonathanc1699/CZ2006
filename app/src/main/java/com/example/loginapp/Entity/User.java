package com.example.loginapp.Entity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


    public User(String fullName, String email, int currentQueue, String currentClinic,String userId, boolean Disabled, boolean Admin) {
        this.fullName = fullName;
        this.email = email;
        this.currentQueue = currentQueue;
        this.currentClinic = currentClinic;
        this.userId=userId;
        this.Admin = Admin;
        this.Disabled = Disabled;

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

        return result;


    }

}
