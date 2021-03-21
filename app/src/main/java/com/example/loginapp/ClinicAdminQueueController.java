package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClinicAdminQueueController extends AppCompatActivity {

    final ClinicAdminQueueController context = this;

    //To read clinic database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    //TODO alert button
    public void incServeQ(String ClinicID, int currentlyservingQ) {
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

        Log.d("currentlyservingQ after", String.valueOf(currentlyservingQ));

        /*
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_clinic_page, null);
        final TextView mTextview_yourqueuenumber = (TextView) promptsView.findViewById(R.id.textView_yourQueueNumber);
        final TextView mTextview_currentqueuenumber = (TextView) promptsView.findViewById(R.id.textView_currentQueueNumber);
        final TextView mTextview_estimatedwaitingtime = (TextView) promptsView.findViewById(R.id.textview_estimatedWaitingTime);


        //Alert to add serving q no
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        int newq=currentlyservingQ;

        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                clinicRef.document(ClinicID).
                        update("ClinicCurrentQ", newq)
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

                Log.d("currentlyservingQ after", String.valueOf(newq));
            }


        });


        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

         */


    }

    //dec q number incase clinic admin made a mistake on incrementing the queue numvber

    public void decServeQ(String ClinicID, int currentlyservingQ) {
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

        Log.d("currentlyservingQ after", String.valueOf(currentlyservingQ));
    }


    }
