package com.example.loginapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ClinicPage extends AppCompatActivity {

    final Context context = this;
    Button mbutton_direction;
    Button mbutton_queue;
    TextView mTextView_nameClinic;
    TextView mTextView_openingHoursClinic;
    TextView mTextView_phoneClinic;
    TextView mTextView_addressClinic;
    ImageView mImageView_Clinic;


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

        mTextView_nameClinic.setText("Name of Clinic:   " + name);
        mTextView_openingHoursClinic.setText("Opening Hours:   "+ "8am - 8pm");

        //TODO once you get the info, change the hardcode
        mTextView_addressClinic.setText("Clinic Address:   " + "Adrian road 123456");
        mTextView_phoneClinic.setText("Phone Number:   "+ "12345678");





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
            public void onClick(DialogInterface dialog, int id) {
                //TODO add to database
                sendConfirmationEmail();
                Log.e("Email sent", "Email sent to user");


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
        //String userName = FirebaseAuth.getInstance().getCurrentUser().get;
        String recipientemail = userEmail;// fetch user's email


        final ProgressDialog dialog = new ProgressDialog(ClinicPage.this);
        dialog.setTitle("Comfirming your Booking");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable(){
            public void run() {
                try {
                    //ToDO GET the booking details for the confirmation email.
                    GMailSender sender = new GMailSender("cz2006sickgowhere@gmail.com", "123456sickgowhere");
                    sender.sendMail("Booking Confirmation",
                            "Hello " +"\nThis is your Confirmation email, you queue no is 7......",
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

