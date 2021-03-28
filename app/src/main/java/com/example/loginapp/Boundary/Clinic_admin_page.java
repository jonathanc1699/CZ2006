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
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.R;

import java.util.ArrayList;
import java.util.List;

public class Clinic_admin_page extends AppCompatActivity {


    final Clinic_admin_page context = this;
    private int current_patient_count=0;
    private int total_patient_count=5;
    private String Clinic_name;
    private List<Integer> list=new ArrayList<Integer>();

    private boolean increment2 = false;

    TextView textview_currentpatient;
    TextView textView_clinicname;
    TextView textView_totalpatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_admin_page);

        textView_clinicname= (TextView) findViewById(R.id.ClinicName);
        //Todo get clinic name
        Clinic_name="THE CLINIC NAME";
        textView_clinicname.setText(Clinic_name);

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

                            if (list.contains(current_patient_count + 1)) {
                                list.remove(current_patient_count);
                                current_patient_count=current_patient_count+2 ;
                                //TODO update clinic class
                            } else {
                                //TODO check loggedin user
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


    public void clinicadminnoti(String userCurrentClinic, int userCurrentQueue) {
        if(userCurrentClinic == Clinic_name) {
            list.add(userCurrentQueue);
        }

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