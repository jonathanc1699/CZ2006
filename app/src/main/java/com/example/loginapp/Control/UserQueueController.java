
package com.example.loginapp.Control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.loginapp.Boundary.Clinic_admin_page;
import com.example.loginapp.Entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
        import java.util.Map;

public class UserQueueController {



    final UserQueueController context = this;

    // upon registration, default for queue and current clinic are 0 and null respectively
    // queueController is used to modify the values in firebase
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //should be another way to ensure its the same current user thats logged in
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    // opens up the current user's database reference
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    User user;
    String userID;
    String currentClinic;
    int currentQNo;
    String fullName;
    String bookedClinic;


    // update current user clinic and queue to firebase
    public void assignQToUser(int latestclinicq, String clinicName, String clinicID) {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userID = user.getUserId();
                fullName = user.getFullName();


                user.setCurrentClinic(clinicName);
                user.setCurrentQueue(latestclinicq);
                user.setClinicID(clinicID);
                bookedClinic = user.getClinicID();
                currentClinic=user.getCurrentClinic();
                currentQNo = user.getCurrentQueue();
                Map<String, Object> userValues = user.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(user.getUserId(), userValues);
                databaseReference.updateChildren(childUpdates);
                Log.d("currentClinic", fullName + "> "+currentClinic);
                Log.d("currentQNo", fullName + "> "+ currentQNo);
                Log.d("currentQNo", fullName + "> "+ bookedClinic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TakeQNUM error", "Failed to load data properly", databaseError.toException());
            }

        };
        currentUser.addListenerForSingleValueEvent(userListener);
    }

    //TODO add this to code
    // update current user clinic and queue to firebase when user cancel booking
    public void cancelQUser(String userID) {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                UserQueueController.this.userID = user.getUserId();
                fullName = user.getFullName();
                user.setCurrentClinic("nil");
                user.setCurrentQueue(0);
                user.setClinicID("nil");

                currentClinic=user.getCurrentClinic();
                currentQNo = user.getCurrentQueue();
                bookedClinic = user.getClinicID();

                Map<String, Object> userValues = user.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(user.getUserId(), userValues);
                databaseReference.updateChildren(childUpdates);

                Log.d("currentClinic", fullName + "> "+currentClinic);
                Log.d("currentQNo", fullName + "> "+ currentQNo);
                Log.d("currentQNo", fullName + "> "+ bookedClinic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelQ error", "Failed to load data properly", databaseError.toException());
            }

        };
        currentUser.addListenerForSingleValueEvent(userListener);
    }

    /*
    //Send Cancellation email to user
    private void sendCancellationEmail() {
        String senderemail = "cz2006sickgowhere@gmail.com";
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String recipientemail = userEmail;// fetch user's email

        Thread sender = new Thread(new Runnable() {
            public void run() {
                try {
                    //ToDO GET the booking details for the confirmation email.
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    sender.sendMail("Appointment cancellation confirmation",
                            "This is a confirmation that your appointment with " +
                                    + clinicname + " has been cancelled at your request." +
                                    " \n\nThank you for using SickGoWhere.\n\nSickGoWhere",
                            senderemail, recipientemail);

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
        Log.d("cancellation email sent","email sent");

    }

     */
}








