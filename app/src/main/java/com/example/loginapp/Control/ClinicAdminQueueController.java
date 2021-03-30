package com.example.loginapp.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

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

    public void incServeQ(String clinicID, int current_patient_count) {


        clinicRef.document(clinicID).
                update("ClinicCurrentQ", current_patient_count)
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

        Log.d("currentlyservingQ after", String.valueOf(current_patient_count));

    }

    public void clearUserClinicandQueue(String ClinicID, int currentQueue)
    {

    }

    public void sendReminderEmail(String ClinicID, int thirduserQ)
    {
        String senderemail = "cz2006sickgowhere@gmail.com";

        //TODO access firebase to fetch user info with clinicname and Qno
        /*
        String recepientemail=useremail;// fetch user's email
        Thread sender = new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    sender.sendMail("Appointment Reminder:"+ ClinicName+ " , Queue number: "+ queueno,
                            "Dear "+ username+",\n"+"There are currently "+"3 person(s) ahead of you in the queue. " +
                                    "You may make your way to " + selectedClinic.getClinicName() +
                                    "\nBest Regards,\nSickGoWhere Team.",
                            senderemail, recepientemail);

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();

         */

    }

    public void wipeAll(String clinicID)
    {
        //wipe current_patient_count
        clinicRef.document(clinicID).
                update("ClinicCurrentQ", 0)
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

        clinicRef.document(clinicID).
                update("latestQNo", 0)
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
