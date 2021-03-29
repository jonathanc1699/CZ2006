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
import com.example.loginapp.Control.UserQueueController;
import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clinic_admin_page extends AppCompatActivity {

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
    private List<Integer> list=new ArrayList<Integer>();

    private boolean increment2 = false;

    TextView textview_currentpatient;
    TextView textView_clinicname;
    TextView textView_totalpatient;

    User user;

    String userID;
    int clinicQ;
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_admin_page);

        textView_clinicname= (TextView) findViewById(R.id.ClinicName);
        //Todo get clinic name
        //Load clinic info
        Clinic_name="THE CLINIC NAME";
        textView_clinicname.setText(Clinic_name);

        textView_totalpatient = (TextView) findViewById(R.id.textView_numtotalpatient);
        textView_totalpatient.setText(String.valueOf(total_patient_count));

        textview_currentpatient = (TextView) findViewById(R.id.textView_numcurrentlyserving);
        textview_currentpatient.setText(String.valueOf(current_patient_count));

        //current_patient_count == ClinicCurrentQ
        //total_patient_count =latestQNo

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

                            current_patient_count++;
                            //TODO check loggedin user
                            updateClinicAdminQ(current_patient_count);
                            UserQueueController userQueueController = new UserQueueController();
                            userQueueController.cancelQUser(userID);

                            int thirduserQ = 0;


                            //clinicAdminQueueController.incServeQ(String ClinicName, int currentlyservingQ)

                            ClinicAdminQueueController clinicAdminQueueController = new ClinicAdminQueueController();
                            //TODO send reminder email to the third user
                            /*if((total_patient_count-current_patient_count+1)>=3)
                                clinicAdminQueueController.sendReminderEmail(Clinic_name,thirduserQ);

                            textview_currentpatient.setText(String.valueOf(current_patient_count));
                            dialog.cancel();

                             */
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

    public void updateClinicAdminQ(int current_patient_count){
        ValueEventListener userListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userID = user.getUserId();
                fullName = user.getFullName();
                user.setCurrentQueue(clinicQ);
                clinicQ = user.getCurrentQueue();


                Map<String, Object> userValues = user.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(user.getUserId(), userValues);
                databaseReference.updateChildren(childUpdates);

                Log.d("currentQNo", fullName + "> "+ clinicQ);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("cancelQ error", "Failed to load data properly", databaseError.toException());
        }
    };
    currentUser.addListenerForSingleValueEvent(userListener);

}

}