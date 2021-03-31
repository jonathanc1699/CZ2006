package com.example.loginapp.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginapp.Boundary.mainactivityAdmin;
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.Entity.Pharmacy;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class PharmacyPage extends AppCompatActivity {
    TextView mTextView_namePharmacy;
    TextView mTextView_openingHoursPharmacy;
    TextView mTextView_addressPharmacy;
    Button mbutton_direction;
    Button mbutton_direction_walk;

    String PharmacyName;
    String PharmacyAddress;
    String PharmacyID;
    Pharmacy selectedPharmacy;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference pharmacyRef = db.collection("pharmacy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_page);
        mbutton_direction = (Button) findViewById(R.id.button_direction_pharm);
        mbutton_direction_walk = (Button) findViewById(R.id.button_direction_pharm_walk);
        mTextView_namePharmacy = (TextView) findViewById(R.id.textview_namePharmacy);
        mTextView_addressPharmacy = (TextView) findViewById(R.id.textview_addressPharmacy);
        mTextView_openingHoursPharmacy = (TextView) findViewById(R.id.textview_openingHoursPharmacy);

        PharmacyID = getIntent().getStringExtra("Pharmacy ID");
        PharmacyName= getIntent().getStringExtra("Pharmacy Name");





        pharmacyRef.document(PharmacyID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot PharmacyDetailList = task.getResult();

                            Map<String, Object> map = PharmacyDetailList.getData();
                            selectedPharmacy = PharmacyDetailList.toObject(Pharmacy.class);
                            selectedPharmacy.setPharmacy_ID(PharmacyID);
                            PharmacyAddress = selectedPharmacy.getPharmacy_address();
                            mTextView_namePharmacy.setText("Name of Pharmacy:   " + PharmacyName);
                            mTextView_addressPharmacy.setText("Address of Pharmacy:   " + PharmacyAddress);
                            mTextView_openingHoursPharmacy.setText("Opening Hours of Pharmacy:  8AM - 8PM ");
                        }

                        else {
                            Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                        }
                    }
                });



        mbutton_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need to link to googlemap
                // Create a Uri from an intent string. Use the result to create an Intent.
                Log.d("directions","trying to open gmaps for directions to "+ selectedPharmacy.getPharmacy_address());
                Uri gmmIntentUri = Uri.parse("google.navigation:q= "+ selectedPharmacy.getLatitude() + ","+ selectedPharmacy.getLongitude());
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                Log.d("directions","opening gmaps for direction to "+ selectedPharmacy.getPharmacy_address());
            }
        });

        mbutton_direction_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO need to link to googlemap
                // Create a Uri from an intent string. Use the result to create an Intent.
                Log.d("directions","trying to open gmaps for directions to "+ selectedPharmacy.getPharmacy_address());
                Uri gmmIntentUri = Uri.parse("google.navigation:q= "+ selectedPharmacy.getLatitude() + ","+ selectedPharmacy.getLongitude() + "&mode=w");
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                Log.d("directions","opening gmaps for direction to "+ selectedPharmacy.getPharmacy_address());
            }
        });
        
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}