package com.example.loginapp.Boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import com.example.loginapp.Control.ClinicAdminQueueController;
import com.example.loginapp.Control.FirebaseCallback;
import com.example.loginapp.Control.UserQueueController;
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clinic_admin_page extends AppCompatActivity implements FirebaseCallback{

    // upon registration, default for queue and current clinic are 0 and null respectively
    // queueController is used to modify the values in firebase
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //should be another way to ensure its the same current user thats logged in
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    // opens up the current user's database reference
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());

    final Clinic_admin_page context = this;
    private int current_patient_count;
    private int total_patient_count;
    private String Clinic_name;

    TextView textview_currentpatient;
    TextView textView_clinicname;
    TextView textView_totalpatient;

    User user;
    String clinicID;
    String userID;
    int clinicQ;
    String fullName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    ClinicAdminQueueController clinicAdminQueueController = new ClinicAdminQueueController();


    // used to store the clinicID retrieved from firebase synchronously
    @Override
    public void onCallback(String value){}
    // loads clinic ID and stores it for use locally in order to pass through and query firestore for clinic details
    public void loadClinic(FirebaseCallback Callback) {

        ValueEventListener userlistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //Map<String, Object> userValues = user.altMap();
                String clinicID = user.getClinicID();
                Log.d("firebase", clinicID);
                String Clinic_name = user.getClinicName();
                Log.d("Clinic name", Clinic_name);
                Log.d("ClinicID", clinicID);
                Callback.onCallback(clinicID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("load", "Error");

            }
        };
        currentUser.addListenerForSingleValueEvent(userlistener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_admin_page);

        // loadClinic contains clinicID
        loadClinic(new FirebaseCallback() {
            @Override
            public void onCallback(String ID) {
                clinicID = ID;
                //TODO change to onSnapshot instead of onComplete
                final DocumentReference AdminRef = clinicRef.document(clinicID);
                AdminRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("snapshotListener", "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d("snapshotListener", "Current data: " + snapshot.getData());
                            Clinic adminClinic = snapshot.toObject(Clinic.class);
                            Clinic_name = adminClinic.getClinicName();
                            Clinic_name = adminClinic.getClinicName();
                            current_patient_count = adminClinic.getClinicCurrentQ();
                            total_patient_count = adminClinic.getLatestQNo();
                            textView_clinicname = (TextView) findViewById(R.id.ClinicName);
                            textView_clinicname.setText(Clinic_name);
                            textView_totalpatient = (TextView) findViewById(R.id.textView_numtotalpatient);
                            textView_totalpatient.setText(String.valueOf(total_patient_count));
                            textview_currentpatient = (TextView) findViewById(R.id.textView_numcurrentlyserving);
                            textview_currentpatient.setText(String.valueOf(current_patient_count));

                        } else {
                            Log.d("snapshotListener", "Current data:  null");
                        }
                    }
                });
            }
        });
        }
    public void button_increment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("My title");
        if(total_patient_count>current_patient_count) {
            builder.setMessage("Confirm next patient? \nAction is irreversible!!!!");

            // add a button
            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            loadClinic(new FirebaseCallback() {
                                @Override
                                public void onCallback(String ID) {
                                    clinicID = ID;

                            //check loggedin user - to reset their Queue and CurrentClinic
                            clinicAdminQueueController.clearUserClinicandQueue(clinicID, current_patient_count);
                            current_patient_count++;
                            clinicAdminQueueController.incServeQ( clinicID,  current_patient_count);

                            //send reminder email to the third user in Queue
                            if((total_patient_count-current_patient_count)>=3)
                            {
                                int thirduserQ = (current_patient_count+3);
                                clinicAdminQueueController.sendReminderEmail(Clinic_name,clinicID,thirduserQ);
                                System.out.println("third user emails sent");

                            }

                            //textview_currentpatient.setText(String.valueOf(current_patient_count));


                        }
                    });}
                    }
                    );
            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        else{
            builder.setMessage("No more patients ahead");
            //Reset last guy in queue: Q number and Currentclinic
            clinicAdminQueueController.clearUserClinicandQueue(clinicID, current_patient_count);
            builder.setNegativeButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void button_wipe(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("My title");

            builder.setMessage("Confirm Wipe Current and Total Queue?");

            // add a button
            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            current_patient_count=0;
                            total_patient_count=0;
                            textview_currentpatient.setText(String.valueOf(current_patient_count));
                            textView_totalpatient.setText(String.valueOf(total_patient_count));
                            clinicAdminQueueController.wipeAll(clinicID);
                            //reflect in control
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }


}


