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
    //TODO remove q and current clinic from user
    public void incServeQ(String ClinicID, int currentlyservingQ) {

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

        Log.d("currentlyservingQ after", String.valueOf(currentlyservingQ));

    }

    /*public void sendReminderEmail(String useremail,String username)
    {
        String senderemail = "cz2006sickgowhere@gmail.com";
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

    }

     */

    }
