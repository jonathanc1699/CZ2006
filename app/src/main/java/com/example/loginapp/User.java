package com.example.loginapp;

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
import java.util.HashMap;
import java.util.Map;

public class User {


    private String fullName;
    private String email;
    private int currentQueue;
    private String currentClinic;
    private boolean isAdmin;
    private String userId;
    private boolean isDisabled;

    public User(String fullName, String email, int currentQueue, String currentClinic,String userId, boolean isDisabled, boolean isAdmin) {
        this.fullName = fullName;
        this.email = email;
        this.currentQueue = currentQueue;
        this.currentClinic = currentClinic;
        this.userId=userId;
        this.isAdmin = isAdmin;
        this.isDisabled = isDisabled;

    }

    public User() {
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
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

}
