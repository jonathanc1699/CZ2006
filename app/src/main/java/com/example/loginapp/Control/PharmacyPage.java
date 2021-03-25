package com.example.loginapp.Control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.loginapp.Boundary.mainactivityAdmin;
import com.example.loginapp.R;

public class PharmacyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_page);
    }
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), PharmacyPage.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }
}