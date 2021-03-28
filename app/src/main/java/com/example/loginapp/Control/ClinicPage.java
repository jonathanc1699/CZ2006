package com.example.loginapp.Control;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginapp.Boundary.mainactivityAdmin;
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ClinicPage extends AppCompatActivity {

    final Context context = this;
    Button mbutton_direction;
    Button mbutton_queue;
    TextView mTextView_nameClinic;
    TextView mTextView_openingHoursClinic;
    TextView mTextView_phoneClinic;
    TextView mTextView_addressClinic;
    ImageView mImageView_Clinic;

    //To read clinic database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    //
    long telephone;
    String streetName;
    String clinicName;
    long postal;
    Object block;
    Object floor;
    String unitNumber;
    long unit;
    String ClinicID;
    String address;

    Clinic selectedClinic;

    int currentlyservingQ;
    int latestclinicq;
    int serveTime = 10;
    //so that the patients can make their way down when they receive their email
    int buffertime = 15;
    boolean godown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_page);

        String clinicID= getIntent().getStringExtra("Clinic ID");
        String clinicName= getIntent().getStringExtra("Clinic Name");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mImageView_Clinic = (ImageView) findViewById(R.id.image_Clinic);
        mbutton_queue = (Button) findViewById(R.id.button_queue);
        mbutton_direction = (Button) findViewById(R.id.button_direction);
        mTextView_nameClinic = (TextView) findViewById(R.id.textview_nameClinic);
        mTextView_openingHoursClinic = (TextView) findViewById(R.id.textview_openingHoursClinic);
        mTextView_phoneClinic = (TextView) findViewById(R.id.textview_phoneClinic);
        mTextView_addressClinic = (TextView) findViewById(R.id.textview_addressClinic);


        clinicRef.document(clinicID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ClinicDetailList = task.getResult();

                                Map<String, Object> map = ClinicDetailList.getData();
                                selectedClinic = ClinicDetailList.toObject(Clinic.class);

                                Log.d("Clinic Info", String.valueOf(ClinicDetailList.getData()));

                                selectedClinic.setClinicID(clinicID);
                                streetName = selectedClinic.getStreetname();
                                telephone = selectedClinic.getTelephone();
                                postal = selectedClinic.getPostal();
                                block = selectedClinic.getBlock();
                                floor = selectedClinic.getFloor();

                                //Get clinic current and last Q number
                                currentlyservingQ = selectedClinic.getClinicCurrentQ();
                                latestclinicq = selectedClinic.getLatestQNo();

                                if (ClinicDetailList.contains("Unit number")&&ClinicDetailList.contains("Floor")&&ClinicDetailList.contains("Block")) {
                                    if (ClinicDetailList.get("Unit number") instanceof String && ClinicDetailList.get("Floor") instanceof String && ClinicDetailList.get("Block") instanceof String) {
                                        unitNumber = (String) ClinicDetailList.get("Unit number");
                                        floor = String.valueOf(ClinicDetailList.get("Floor"));
                                        block = String.valueOf(ClinicDetailList.get("Block"));
                                        address = "Clinic Address: " + block + " " +
                                                streetName + " #0" + floor + "-" + unitNumber + " Block " +
                                                block + " Singapore" + postal;
                                        mTextView_addressClinic.setText(address);
                                    } else {
                                        unit = (long) ClinicDetailList.get("Unit number");
                                        floor = (long) ClinicDetailList.get("Floor");
                                        block = (long) ClinicDetailList.get("Block");

                                        address="Clinic Address: " + block + " " +
                                                streetName + " #0" + floor + "-" + unit + " Block " +
                                                block + " Singapore" + postal;
                                        mTextView_addressClinic.setText(address);
                                    }
                                } else {
                                    address="Clinic Address: " + block + " " +
                                            streetName + ", Level: " + floor + " Block " +
                                            block + " s" + postal;
                                    mTextView_addressClinic.setText(address);
                                }


                                mTextView_nameClinic.setText("Name of Clinic:   " + clinicName);
                                mTextView_openingHoursClinic.setText("Opening Hours:   " + "8am - 8pm");
                                mTextView_phoneClinic.setText("Telephone:   " + telephone);
                            }
                         else {
                            Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                        }
                    }
                });

        mbutton_queue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                showfilterselection();
            }
        });
        mbutton_direction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO need to link to googlemap
                // Create a Uri from an intent string. Use the result to create an Intent.
                Log.d("directions","trying to open gmaps for directions to "+ selectedClinic.getStreetname());
                Uri gmmIntentUri = Uri.parse("google.navigation:q= "+ selectedClinic.getLatitude() + ","+ selectedClinic.getLongitude());
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                Log.d("directions","opening gmaps for direction to "+ selectedClinic.getStreetname());



                //TODO Shift the following code to ClinicAdmin class when the UI is completed
                //Increment q function
                if(latestclinicq>currentlyservingQ)
                {
                    ClinicAdminQueueController clinicAdminQueueController = new ClinicAdminQueueController();
                    clinicAdminQueueController.incServeQ(selectedClinic.getClinicID(),currentlyservingQ);

                    currentlyservingQ++;
                }
                else
                {
                    final ProgressDialog incqueue = new ProgressDialog(ClinicPage.this);
                    incqueue.setTitle("Unable to increase current Queue");
                    incqueue.setMessage("Maximum Queue number reached");
                    incqueue.show();
                    //set timer for dialog window to close
                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            incqueue.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 4000);

                    System.out.println("Cannot increase Queue number");
                }


                Log.d("ClinicCurrentQ", String.valueOf(currentlyservingQ));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showfilterselection() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_clinic_page, null);
        final TextView mTextview_yourqueuenumber = (TextView) promptsView.findViewById(R.id.textView_yourQueueNumber);
        final TextView mTextview_currentqueuenumber = (TextView) promptsView.findViewById(R.id.textView_currentQueueNumber);
        final TextView mTextview_estimatedwaitingtime = (TextView) promptsView.findViewById(R.id.textview_estimatedWaitingTime);

        //Get start, close and current time
        String start = selectedClinic.getStartTime();
        String close = selectedClinic.getClosingTime();
        LocalTime startTime = LocalTime.parse(start);
        LocalTime closingTime = LocalTime.parse(close);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
        sdf.setTimeZone(tz);

        java.util.Date date = new java.util.Date();
        Timestamp local = new Timestamp(date.getTime());
        //String strTime = sdf.format(date);

        String strTime = "14:00:00";
        System.out.println("Local in String format " + strTime);

        //one hour before so that last hour of operation , patients would not be anble to make any appointment
        //time buffer
        LocalTime onehrbefore = closingTime.minus(1, ChronoUnit.HOURS);

        int waitingTime = (latestclinicq - currentlyservingQ) * serveTime + buffertime;


        mTextview_yourqueuenumber.setText(String.valueOf((latestclinicq + 1)));
        mTextview_currentqueuenumber.setText(String.valueOf((currentlyservingQ)));


        if (waitingTime > 60) {
            int hour = waitingTime / 60;
            int min = waitingTime % 60;

            mTextview_estimatedwaitingtime.setText(hour + "hr " + min + " mins");
        } else
            mTextview_estimatedwaitingtime.setText(waitingTime + " mins");

        Log.d("currentlyservingQ bef", String.valueOf(currentlyservingQ));

        Log.d("latestclinicq before", String.valueOf(latestclinicq));


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(DialogInterface dialog, int id) {
                int mins = (LocalTime.parse(strTime)).getMinute();
                int hours = (LocalTime.parse(strTime)).getHour();

                int totalqueueleft = (((onehrbefore.getHour() - hours) * 60) + (60 - mins)) / serveTime;

                if ((((latestclinicq + 1) - currentlyservingQ) < totalqueueleft) && (startTime.isBefore(LocalTime.parse(strTime)) && (onehrbefore.isAfter(LocalTime.parse(strTime))) && closingTime.isAfter(LocalTime.parse(strTime)))) {
                    //more than 3 ppl
                    if (waitingTime > 40) {
                        sendConfirmationEmail();
                        Log.e("Email sent", "Email sent to user");
                    }
                    //less than 3 ppl, make way down now
                    else{
                        makeYourWayDown();

                        Log.e("Email sent", "Email sent to user");
                    }

                }
                //one hour before closing dont allow booking
                else if (startTime.isBefore(LocalTime.parse(strTime)) && (onehrbefore.isBefore(LocalTime.parse(strTime))) && closingTime.isAfter(LocalTime.parse(strTime))) {
                    final ProgressDialog closingdialog = new ProgressDialog(ClinicPage.this);
                    closingdialog.setTitle("Fail to book appointment");
                    closingdialog.setMessage("Clinic is closing soon \nIf it is an emergency, please visit the hospital\nPlease try again from 0800 to 1900. \nThank you");
                    closingdialog.show();
                    //set timer for dialog window to close
                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            closingdialog.cancel();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 7000);

                    System.out.println("Cannot book appt");

                } else if ((((latestclinicq + 1) - currentlyservingQ) > totalqueueleft) && (startTime.isBefore(LocalTime.parse(strTime)) && (onehrbefore.isAfter(LocalTime.parse(strTime))) && closingTime.isAfter(LocalTime.parse(strTime)))) {
                    final ProgressDialog bookingfulldialog = new ProgressDialog(ClinicPage.this);
                    bookingfulldialog.setTitle("Fail to book appointment");
                    bookingfulldialog.setMessage("Clinic is fully booked for the day.\nIf it is an emergency, please visit the hospital\nThank you");
                    bookingfulldialog.show();


                    //set timer for dialog window to close
                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            bookingfulldialog.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 7000);

                    System.out.println("Booking full");
                } else if (startTime.isAfter(LocalTime.parse(strTime)) && closingTime.isAfter(LocalTime.parse(strTime))) {
                    final ProgressDialog notopenyet = new ProgressDialog(ClinicPage.this);
                    notopenyet.setTitle("Fail to book appointment");
                    notopenyet.setMessage("Clinic is not open yet \nPlease try again when the clinic is open at 8am.\nIf it is an emergency, please visit the hospital\nThank you.");
                    notopenyet.show();
                    //set timer for dialog window to close
                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            notopenyet.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 7000);

                    System.out.println("Cannot book appt");
                } else {
                    final ProgressDialog faildialog = new ProgressDialog(ClinicPage.this);
                    faildialog.setTitle("Fail to book appointment");
                    faildialog.setMessage("Clinic is closed \nPlease try again when the clinic is open.\nIf it is an emergency, please visit the hospital\nThank you.");
                    faildialog.show();


                    //set timer for dialog window to close
                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            faildialog.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 7000);

                    System.out.println("Cannot book appt");

                }
            }


        });


        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();


    }

    //Send Confirmation email to user
    private void sendConfirmationEmail() {
        String senderemail = "cz2006sickgowhere@gmail.com";
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String recipientemail = userEmail;// fetch user's email

        final ProgressDialog dialog = new ProgressDialog(ClinicPage.this);
        dialog.setTitle("Confirming your Booking");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            public void run() {
                try {
                    //ToDO GET the booking details for the confirmation email.
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    if (godown=true)
                    {
                        //TODO add user queue number
                        sender.sendMail("Booking Confirmation: "+ selectedClinic.getClinicName() + ", Queue No:",
                                "Hello,\nThis is your Confirmation email, you queue no is .....\n"+
                                        "There are currently " + ((latestclinicq + 1) - currentlyservingQ)
                                + "person(s) ahead of you in the queue. You may make your way to "+ selectedClinic.getClinicName()
                                        + "\n\nClinic Address:"  + block + " "+streetName + " #0" +
                                        floor + "-" + unit + " Block " + block + " Singapore" + postal+
                                        " \nThank you your using SickGoWhere.\n\nSickGoWhere",
                                senderemail, recipientemail);
                        dialog.dismiss();
                    }
                    else
                    {
                        sender.sendMail("Booking Confirmation",
                                "This is your Confirmation email, you queue no is 7......",
                                senderemail, recipientemail);
                    }
                    dialog.dismiss();

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();

        //UPDATE latestQNo when new booking is made
        latestclinicq++;
        clinicRef.document(selectedClinic.getClinicID()).
                update("latestQNo", latestclinicq)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ClinicCurrentQ", "Update ClinicCurrentQ successfully updated!");
                        Log.d("clinicCurrentQ", selectedClinic.getClinicID());
                        Log.d("clinicCurrentQ", selectedClinic.getClinicName());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ClinicCurrentQ", "Error updating document", e);
                    }
                });
        UserQueueController userQueueController = new UserQueueController();
        userQueueController.assignQToUser(latestclinicq,selectedClinic.getClinicName(),selectedClinic.getClinicID());


        //TODO change to ALL clinic .
        //TODO i dont think the current q update belongs here***
        currentlyservingQ++;


        clinicRef.document(selectedClinic.getClinicID()).
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
        Log.d("latestclinicq after", String.valueOf(latestclinicq));
        
    }

    private void makeYourWayDown() {
        godown = true;
        AlertDialog.Builder goClinicAlert = new AlertDialog.Builder(context);
        goClinicAlert.setMessage("Booking is confirmed. Check your email for your booking confirmation. \n \nThere are currently " + ((latestclinicq + 1) - currentlyservingQ) +
                " person(s) ahead of you in the queue. You may make your way to " + selectedClinic.getClinicName());
        Log.d("currentCLinciEmail", selectedClinic.getClinicName());
        goClinicAlert.setCancelable(true);

        goClinicAlert.setPositiveButton(
                "Got it!",
                (dialog, id) -> dialog.cancel());
        AlertDialog alertPatient = goClinicAlert.create();
        alertPatient.show();
        sendConfirmationEmail();
    }


}







