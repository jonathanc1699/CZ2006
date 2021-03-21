package com.example.loginapp;

import android.util.Log;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ClinicAdminQueueController {

    int currentlyservingQ;
    //To read clinic database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");


    //TODO
    //mbutton_increase.setOnClickListener(new View.OnClickListener() {
    //at clinic admin page
    public void incServeQ(String ClinicID) {
        currentlyservingQ++;
        clinicRef.document(ClinicID).
                update("ClinicCurrentQ", currentlyservingQ)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ClinicCurrentQ", "Update ClinicCurrentQ successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ClinicCurrentQ", "Error updating document", e);
                    }
                });

    }
}
