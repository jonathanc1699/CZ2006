package com.example.loginapp.Control;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.Entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *This class implements the ClinicAdminQueueController controller which will be used to update
 * both Firestore and Firebase.
 * Functions in this class includes, incrementing the clinic's currently serving queue number,
 * clear user's current clinic and queue, send reminder email to the third user in queue
 * and to reset clinic's latest and currently serving queue number.
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class ClinicAdminQueueController extends AppCompatActivity {

    final ClinicAdminQueueController context = this;
    ArrayList<User> userArrayList =new ArrayList<User>();

    //To read clinic database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    //should be another way to ensure its the same current user thats logged in
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

    /**
     * Updates the incremented queue number to Firestore
     * @param clinicID clinic admin's clinic ID
     * @param current_patient_count incremented serving queue number
     */
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

    /**
     * Update the user's ClinicID, CurrentClinic and CurrentQueue to default value (No current Appointment)
     * This function is used for those user who finished their appointment
     *
     * Only the user whose clinicID and CurrentQueue data matches the passed in parameters
     * will have their data updated in Firebase
     *
     * @param clinicID Clinic ID
     * @param current_patient_count CurrentQueue number
     */
    public void clearUserClinicandQueue(String clinicID, int current_patient_count)
    {

        Query FindMatchClinic = databaseReference.orderByChild("clinicID").equalTo(clinicID);
        FindMatchClinic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userArrayList.add(user);
                    Log.d("it ran", String.valueOf(userArrayList));
                }

                for (int i = 0; i < userArrayList.size(); i++) {
                    if(userArrayList.get(i).getCurrentQueue() == current_patient_count)
                    {
                        userArrayList.get(i).setCurrentQueue(0);
                        userArrayList.get(i).setCurrentClinic("nil");
                        userArrayList.get(i).setClinicID("nil");
                        Map<String, Object> userValues = userArrayList.get(i).toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(userArrayList.get(i).getUserId(), userValues);
                        databaseReference.updateChildren(childUpdates);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("query", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    /**
     * Fetch the email and username of third person in queue and send an email reminder to the user
     * So that the user can make their way down
     * @param Clinic_name clinic Name
     * @param clinicID clinic's ID
     * @param thirduserQ Queue number of the 3rd person in queue
     */
    //Send email reminder to the 3rd person in queue
    //So that the user can make their way down
    public void sendReminderEmail(String Clinic_name, String clinicID, int thirduserQ)
    {
        String senderemail = "cz2006sickgowhere@gmail.com";

        Query FindMatchClinic = databaseReference.orderByChild("clinicID").equalTo(clinicID);
        FindMatchClinic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }

                for (int i = 0; i < userArrayList.size(); i++) {
                    if(userArrayList.get(i).getCurrentQueue() == thirduserQ)
                    {
                        String recepientemail = userArrayList.get(i).getEmail();
                        String username = userArrayList.get(i).getFullName();

                        Thread sender = new Thread(new Runnable() {
                            public void run() {
                                try {
                                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                                    sender.sendMail("Appointment Reminder:"+ Clinic_name+ " , Queue number: "+ thirduserQ,
                                            "Dear "+ username+",\n"+"There are currently "+"3 person(s) ahead of you in the queue. " +
                                                    "You may make your way to " + Clinic_name +
                                                    "\nBest Regards,\nSickGoWhere Team.",
                                            senderemail, recepientemail);
                                } catch (Exception e) {
                                    Log.e("mylog", "Error: " + e.getMessage());
                                }
                            }
                        });
                        sender.start();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("query", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    /**
     * Reset the Clinic's latest queue number and currently serving queue number to 0
     * @param clinicID Clinic's ID
     * @param Clinic_name clinic's name
     */
    public void wipeAll(String clinicID, String Clinic_name)
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

        Query FindMatchClinic = databaseReference.orderByChild("clinicID").equalTo(clinicID);
        FindMatchClinic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userArrayList.add(user);
                }

                for (int i = 0; i < userArrayList.size(); i++) {
                    if(userArrayList.get(i).getCurrentQueue() != 0 && !userArrayList.get(i).isClinicAdmin() && userArrayList.get(i).getFullName() != Clinic_name)
                    {
                        userArrayList.get(i).setCurrentQueue(0);
                        userArrayList.get(i).setCurrentClinic("nil");
                        userArrayList.get(i).setClinicID("nil");
                        Map<String, Object> userValues = userArrayList.get(i).toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(userArrayList.get(i).getUserId(), userValues);
                        databaseReference.updateChildren(childUpdates);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("query", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    }

    }
