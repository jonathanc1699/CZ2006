package com.example.loginapp.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import com.example.loginapp.Control.ClinicAdminQueueController;
import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Clinic_admin_page extends AppCompatActivity {

    private int current_patient_count=0;
    private int total_patient_count=5;
    private String Clinic_name;

    TextView textview_currentpatient;
    TextView textView_clinicname;
    TextView textView_totalpatient;
    private String clinicID;
    User currentAdmin;

    //pre-process the user
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_admin_page);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                clinicID = user.getClinicID();
                Clinic_name = user.getClinicName();
                Log.d("clinicAdmin", user.getClinicID());
                textView_clinicname= (TextView) findViewById(R.id.ClinicName);
                textView_clinicname.setText(String.valueOf(Clinic_name));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }

        };
        currentUser.addValueEventListener(userListener);




        textView_totalpatient = (TextView) findViewById(R.id.textView_numtotalpatient);
        textView_totalpatient.setText(String.valueOf(total_patient_count));

        textview_currentpatient = (TextView) findViewById(R.id.textView_numcurrentlyserving);
        textview_currentpatient.setText(String.valueOf(current_patient_count));

        //TODO get clinic info





        //current_patient_count == ClinicCurrentQ
        //total_patient_count =latestQNo



    }

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







    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }


}