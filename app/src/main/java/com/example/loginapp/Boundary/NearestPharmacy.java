package com.example.loginapp.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginapp.R;

public class NearestPharmacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_clinic);
    }

    public void MapView (View view){
        startActivity(new Intent(getApplicationContext(), MapsActivityPharmacy.class));
        // insert on button click, start queueActivity
        // startActivity(new Intent(getApplicationContexxt(), QueueController.class));
        finish();
    }

    public void listView (View view){
        startActivity(new Intent(getApplicationContext(), ListofPharmacies.class));
        // insert on button click, start queueActivity
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }
}

