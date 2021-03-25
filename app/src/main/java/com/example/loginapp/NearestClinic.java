package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NearestClinic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_clinic);
    }

    public void MapView (View view){
        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        // insert on button click, start queueActivity
        // startActivity(new Intent(getApplicationContexxt(), QueueController.class));
        finish();
    }

    public void listView (View view){
        startActivity(new Intent(getApplicationContext(), ListofClinics.class));
        // insert on button click, start queueActivity
        finish();
    }
}
