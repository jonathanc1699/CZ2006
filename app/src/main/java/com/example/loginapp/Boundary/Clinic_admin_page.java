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
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
import com.example.loginapp.Control.UserQueueController;
=======
import com.example.loginapp.Control.FirebaseCallback;
import com.example.loginapp.Entity.Clinic;
>>>>>>> Stashed changes
>>>>>>> Stashed changes
import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
<<<<<<< Updated upstream
=======
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
>>>>>>> Stashed changes

import java.util.Map;

public class Clinic_admin_page extends AppCompatActivity implements FirebaseCallback{

    private int current_patient_count=0;
    private int total_patient_count=5;
    private String Clinic_name;
<<<<<<< Updated upstream
=======
    private List<Integer> list=new ArrayList<Integer>();
    Clinic currentClinic;

    private boolean increment2 = false;
>>>>>>> Stashed changes

    TextView textview_currentpatient;
    TextView textView_clinicname;
    TextView textView_totalpatient;
    private String clinicID;
    User currentAdmin;

    //pre-process the user
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());

<<<<<<< Updated upstream

=======
<<<<<<< Updated upstream
    User user;

    String userID;
    int clinicQ;
    String fullName;
=======
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference clinics = db.collection("clinics");

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

>>>>>>> Stashed changes
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_admin_page);
<<<<<<< Updated upstream


        textView_clinicname= (TextView) findViewById(R.id.ClinicName);
        textView_clinicname.setText(String.valueOf(Clinic_name));

=======
>>>>>>> Stashed changes
        textView_totalpatient = (TextView) findViewById(R.id.textView_numtotalpatient);
        textView_totalpatient.setText(String.valueOf(total_patient_count));

        textview_currentpatient = (TextView) findViewById(R.id.textView_numcurrentlyserving);
        textview_currentpatient.setText(String.valueOf(current_patient_count));
<<<<<<< Updated upstream

        //TODO get clinic info
        // TODO need to load everything together under 1 valuelistener
//        public String loadClinicAdminData() {
//            ValueEventListener userListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User user = dataSnapshot.getValue(User.class);
//
//
//                    Map<String, Object> userValues = user.altMap();
//                    clinicID = (String) userValues.get("clinicID");
//                    Clinic_name = (String) userValues.get("clinicName");
//                    Log.d("Clinic name", Clinic_name);
//                    Log.d("ClinicID", clinicID);
//
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.w("TakeQNUM error", "Failed to load data properly", databaseError.toException());
//                }
//
//            };
//            currentUser.addListenerForSingleValueEvent(userListener);
//            return clinicID;
//        }




        //current_patient_count == ClinicCurrentQ
        //total_patient_count =latestQNo



    }
=======
        textView_clinicname= (TextView) findViewById(R.id.ClinicName);
        textView_clinicname.setText(Clinic_name);
        
        loadClinic(new FirebaseCallback() {
            @Override
            public void onCallback(String value) {
                Log.d("testtest", value);
            }
        });
        //TODO get clinic info
         //TODO need to load everything together under 1 valuelistener

//
//        clinics.document(String.valueOf(load)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot adminClinic = task.getResult();
//                    if (adminClinic.exists()) {
//                        currentClinic = adminClinic.toObject(Clinic.class);
//                        current_patient_count = currentClinic.getClinicCurrentQ();
//                        total_patient_count =currentClinic.getLatestQNo();
//                        Log.d("clinic admin page", "DocumentSnapshot data: " + adminClinic.getData());
//                    } else {
//                        Log.d("clinic admin page", "No such document");
//                    }
//                } else {
//                    Log.d("clinic admin page", "get failed with ", task.getException());
//                }
//            }
//        });

                }

>>>>>>> Stashed changes

    public void button_increment(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("My title");
        if(total_patient_count>current_patient_count){
            builder.setMessage("Confirm next patient?");

            // add a button
            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            int thirduserQ = 0;

                            //clinicAdminQueueController.incServeQ(String ClinicName, int currentlyservingQ)

                            ClinicAdminQueueController clinicAdminQueueController = new ClinicAdminQueueController();
                            //TODO send reminder email to the third user
                            if((total_patient_count-current_patient_count+1)>=3)
                                clinicAdminQueueController.sendReminderEmail(Clinic_name,thirduserQ);

                            textview_currentpatient.setText(String.valueOf(current_patient_count));
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

        }
        else{
            builder.setMessage("No more patients ahead");
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



<<<<<<< Updated upstream




=======
>>>>>>> Stashed changes
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }





}