package com.example.loginapp.Boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.Control.ChatbotActivity;
import com.example.loginapp.Control.ClinicPage;
import com.example.loginapp.Control.FirebaseCallback;
import com.example.loginapp.Control.UserQueueController;
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements FirebaseCallback  {
    User user;
    String currentClinicID;
    String clinicName;
    int currentQueueNumber=1;
    Intent intent;
    Button buttonname;
    FloatingActionButton buttoncancelqueue;
    TextView currentQueue;
    TextView timing;
    TextView clinic;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());

    UserQueueController userQueueController = new UserQueueController();

    final MainActivity context = this;

    @Override
    public void onCallback(String value) {

    }
    public void loadClinic(FirebaseCallback Callback) {

        ValueEventListener userlistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //Map<String, Object> userValues = user.altMap();
                if(user.getClinicID() != null) {
                    String clinicID = user.getClinicID();
                    Log.d("firebase", clinicID);
                    Callback.onCallback(clinicID);
                }
                else
                {
                    String clinicID = "nil";
                    Log.d("firebase", clinicID);
                    Callback.onCallback(clinicID);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("load", "Error");

            }
        };
        currentUser.addListenerForSingleValueEvent(userlistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View deleteuserlist = findViewById(R.id.adminuserlist);
        deleteuserlist.setVisibility(View.GONE);

        View enableuserlist = findViewById(R.id.enableuserlist);
        enableuserlist.setVisibility(View.GONE);

        View contraint = findViewById(R.id.Appointmentlayout);
        contraint.setVisibility(View.GONE);


        buttonname = (Button) findViewById(R.id.appointmentBox);
        buttoncancelqueue = findViewById(R.id.Cancelqueuebutton);

        currentQueue = (TextView) findViewById(R.id.currentqueuenumber);
        timing= (TextView) findViewById(R.id.timing);
        clinic= (TextView) findViewById(R.id.currentclinic);


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                currentClinicID = user.getClinicID();
                if(currentClinicID!=null && !currentClinicID.equals("nil")){
                    contraint.setVisibility(View.VISIBLE);
                    clinicName = user.getCurrentClinic();
                    clinic.setText(clinicName);
                    currentQueueNumber = user.getCurrentQueue();
                    clinic.setText(clinicName);
                    currentQueue.setText("Current queue number: " + String.valueOf(currentQueueNumber));
                }
                else{
                    contraint.setVisibility(View.VISIBLE);
                    clinic.setText("You have no current booking");
                    currentQueue.setText("");
                    timing.setText("");
                    buttonname.setEnabled(false);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("mainActivity", "db error ");
            }
        };
        currentUser.addListenerForSingleValueEvent(userListener);

        loadClinic(new FirebaseCallback() {
            @Override
            public void onCallback(String ID) {
                currentClinicID = ID;
                Log.d("currentAppointmet11", currentClinicID);
                if (!currentClinicID.equals("nil")) {
                    Log.d("currentAppointmet", currentClinicID);
                    clinicRef.document(currentClinicID).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot ClinicDetailList = task.getResult();

                                        Map<String, Object> map = ClinicDetailList.getData();
                                        Clinic clinic = ClinicDetailList.toObject(Clinic.class);
                                        int serveTime = 10;
                                        int latestclinicq = clinic.getLatestQNo();
                                        int currentlyservingQ = clinic.getClinicCurrentQ();
                                        int waitingtime = (latestclinicq - currentlyservingQ) * serveTime + 15;
                                        int hour = waitingtime / 60;
                                        int min = waitingtime % 60;
                                        if (waitingtime > 60) {
                                            timing.setText("Waiting time: " + hour + " hr " + min + " mins");
                                        } else
                                            timing.setText("Waiting time: " + min + " mins");


                                    } else {
                                        Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    buttonname.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            // Log.d("3", clinicName);
                            intent = new Intent(MainActivity.this, ClinicPage.class);
                            Log.d("currentAppointmet11", currentClinicID);
                            intent.putExtra("main Clinic ID", currentClinicID);
                            intent.putExtra("main Clinic name", clinicName);
                            startActivity(intent);
                        }
                    });
                    buttoncancelqueue.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            //builder.setTitle("My title");\
                            builder.setMessage("Are you sure you want to cancel your appointment? " +
                                    "\n***ACTION IS IRREVERSIBLE***");

                            builder.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            userQueueController.sendCancellationEmail(clinicName);
                                            userQueueController.cancelQUser(firebaseUser.getUid());
                                        }
                                    }
                            );
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

                            //
                        }
                    });
                }
                else
                {
                    currentClinicID = "nil";
                }
            }
        });
    }




    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void chatbot (View view) {
        Intent chatbot = new Intent(this, ChatbotActivity.class);
        startActivity(chatbot);
    }

    public void nearestClinic (View view) {
        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
        finish();
    }
    public void nearestPharmacy (View view) {
        startActivity(new Intent(getApplicationContext(),MapsActivityPharmacy.class));
        finish();
    }

}
