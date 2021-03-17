package com.example.loginapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    String streetName ;
    String clinicName;
    long Floor;
    long postal;
    long block;
    long floor;
    String unitNumber;
    long unit;

    Clinic selectedClinic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_page);

        String name = getIntent().getStringExtra("CLINIC_NAME");
        //TODO get intents for image, phone and address


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mImageView_Clinic = (ImageView) findViewById(R.id.image_Clinic);
        mbutton_queue = (Button) findViewById(R.id.button_queue);
        mbutton_direction = (Button) findViewById(R.id.button_direction);
        mTextView_nameClinic = (TextView) findViewById(R.id.textview_nameClinic);
        mTextView_openingHoursClinic = (TextView) findViewById(R.id.textview_openingHoursClinic);
        mTextView_phoneClinic = (TextView) findViewById(R.id.textview_phoneClinic);
        mTextView_addressClinic = (TextView) findViewById(R.id.textview_addressClinic);

        //TODO Need to assign to clinic id instead of ("Clinic Name", name)
        clinicRef.whereEqualTo("Clinic Name", name).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ClinicDetailList : task.getResult()) {
                                Map<String, Object> map = ClinicDetailList.getData();
                                selectedClinic = ClinicDetailList.toObject(Clinic.class);

                                clinicName = selectedClinic.getClinicName();
                                streetName=selectedClinic.getStreetname();
                                telephone = selectedClinic.getTelephone();
                                postal=selectedClinic.getPostal();
                                block = selectedClinic.getBlock();
                                floor = selectedClinic.getFloor();


                                if(ClinicDetailList.contains("Unit number"))
                                {
                                    if(ClinicDetailList.get("Unit number") instanceof String) {
                                        unitNumber = (String) ClinicDetailList.get("Unit number");

                                        mTextView_addressClinic.setText("Clinic Address: " + block + " " +
                                                streetName + " #0" + floor + "-" + unitNumber + " Block " +
                                                block + " Singapore" + postal);
                                    }

                                    else {
                                        unit = (long) ClinicDetailList.get("Unit number");

                                        mTextView_addressClinic.setText("Clinic Address: " + block + " " +
                                                streetName + " #0" + floor + "-" + unit + " Block " +
                                                block + " Singapore" + postal);
                                    }

                                }
                                else
                                {
                                    mTextView_addressClinic.setText("Clinic Address: "+ block+ " " +
                                            streetName + ", Level: "+ floor+ " Block " +
                                            block+" s" + postal);
                                }


                                mTextView_nameClinic.setText("Name of Clinic:   " + clinicName);
                                mTextView_openingHoursClinic.setText("Opening Hours:   " + "8am - 8pm");
                                mTextView_phoneClinic.setText("Telephone:   " + telephone);
                            }
                        } else {
                            Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                        }
                    }
                });


        mbutton_queue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showfilterselection();
            }
        });
        mbutton_direction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO need to link to googlemap
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


    public void showfilterselection() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.popup_clinic_page, null);
        final TextView mTextview_yourqueuenumber = (TextView) promptsView.findViewById(R.id.textView_yourQueueNumber);
        final TextView mTextview_currentqueuenumber = (TextView) promptsView.findViewById(R.id.textView_currentQueueNumber);
        final TextView mTextview_estimatedwaitingtime = (TextView) promptsView.findViewById(R.id.textview_estimatedWaitingTime);

        //TODO get information for your queue number, current queue number
        //TODO estimate waiting time can be 10min*each person
        mTextview_yourqueuenumber.setText("10");
        mTextview_currentqueuenumber.setText("7");
        mTextview_estimatedwaitingtime.setText("30" + " mins");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(DialogInterface dialog, int id) {
                //TODO add to database
                //TODO set boundary for booking of appt
                String start = selectedClinic.getStartTime();
                String close = selectedClinic.getClosingTime();

                LocalTime startTime = LocalTime.parse(start);
                LocalTime closingTime =LocalTime.parse(close);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                TimeZone tz = TimeZone.getTimeZone("Asia/Singapore");
                sdf.setTimeZone(tz);

                java.util.Date date= new java.util.Date();
                Timestamp local = new Timestamp(date.getTime());
                String strTime = sdf.format(date);
                System.out.println("Local in String format " + strTime);


                //one hour before closing dont allow booking
                LocalTime onehrbefore = closingTime.minus(1, ChronoUnit.HOURS);


                if (startTime.isBefore(LocalTime.parse(strTime)) && (onehrbefore.isAfter(LocalTime.parse(strTime)))&& closingTime.isAfter(LocalTime.parse(strTime)) )
                {
                    sendConfirmationEmail();
                    Log.e("Email sent", "Email sent to user");
                }
                else if (startTime.isBefore(LocalTime.parse(strTime)) && (onehrbefore.isBefore(LocalTime.parse(strTime)))&& closingTime.isAfter(LocalTime.parse(strTime)) )
                {
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

                }
                else if(startTime.isAfter(LocalTime.parse(strTime))&&closingTime.isAfter(LocalTime.parse(strTime))){
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
                }
                else
                {
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
        Thread sender = new Thread(new Runnable(){
            public void run() {
                try {
                    //ToDO GET the booking details for the confirmation email.
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    sender.sendMail("Booking Confirmation",
                            "This is your Confirmation email, you queue no is 7......",
                            senderemail, recipientemail);
                    dialog.dismiss();

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();

    }
}




//    private void doimage(Intent data){
//        try {
//            Uri imageUri = data.getData();
//            InputStream imageStream = getContentResolver().openInputStream(imageUri);
//            Bitmap selectedImag = BitmapFactory.decodeStream(imageStream);
//            //
//            Bitmap scaledBitmap = scaleDown(selectedImag, 400, true);
//            mimageview.setImageBitmap(scaledBitmap);
//            //
//            mimageview.setVisibility(View.VISIBLE);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//
//        }
//    }
//
//
//    // convert from byte array to bitmap
//    public static Bitmap getImage(byte[] image) {
//        return BitmapFactory.decodeByteArray(image, 0, image.length);
//    }
//
//
//    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
//                                   boolean filter) {
//        float ratio = Math.min(
//                (float) maxImageSize / realImage.getWidth(),
//                (float) maxImageSize / realImage.getHeight());
//        int width = Math.round((float) ratio * realImage.getWidth());
//        int height = Math.round((float) ratio * realImage.getHeight());
//
//        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
//                height, filter);
//        if (ratio >= 1.0){ return realImage;}
//        else {
//            return newBitmap;
//        }
//    }

