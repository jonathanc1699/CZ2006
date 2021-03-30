package com.example.loginapp.Control;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.Entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ClinicAdminQueueController extends AppCompatActivity {

    final ClinicAdminQueueController context = this;
    ArrayList<User> userArrayList =new ArrayList<User>();

    //To read clinic database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    //should be another way to ensure its the same current user thats logged in
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

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
                    if(userArrayList.get(i).getCurrentQueue() != 0 && userArrayList.get(i).getFullName() != Clinic_name)
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
