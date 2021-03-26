package com.example.loginapp.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.loginapp.Control.ClinicAdapter;
import com.example.loginapp.R;

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
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivityForResult(myIntent, 0);
//        return true;
//    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }
}
