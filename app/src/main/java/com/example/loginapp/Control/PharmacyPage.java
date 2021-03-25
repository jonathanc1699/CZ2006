package com.example.loginapp.Control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginapp.Boundary.mainactivityAdmin;
import com.example.loginapp.R;

public class PharmacyPage extends AppCompatActivity {
    TextView mTextView_namePharmacy;
    TextView mTextView_openingHoursPharmacy;
    TextView mTextView_addressPharmacy;
    Button mbutton_direction;

    String PharmacyName;
    String PharmacyAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_page);
        mbutton_direction = (Button) findViewById(R.id.button_direction_pharm);
        mTextView_namePharmacy = (TextView) findViewById(R.id.textview_namePharmacy);
        mTextView_addressPharmacy = (TextView) findViewById(R.id.textview_addressPharmacy);
        mTextView_openingHoursPharmacy = (TextView) findViewById(R.id.textview_openingHoursPharmacy);
        
        mTextView_namePharmacy.setText("Name of Pharmacy:   " + PharmacyName);
        mTextView_addressPharmacy.setText("Address of Pharmacy:   " + PharmacyAddress);
        mTextView_openingHoursPharmacy.setText("Opening Hours of Pharmacy:  8AM - 8PM ");

        
        
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}