package com.example.loginapp.Boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.loginapp.Control.ChatbotActivity;
import com.example.loginapp.Control.ClinicController;
import com.example.loginapp.Control.ClinicPage;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    User user;
    String currentClinicID;
    String clinicName;
    int currentQueueNumber=1;
    Intent intent;
    Button buttonname;

    TextView currentQueue;
    TextView timing;
    TextView clinic;


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

        currentQueue = (TextView) findViewById(R.id.currentqueuenumber);
        timing= (TextView) findViewById(R.id.timing);
        clinic= (TextView) findViewById(R.id.currentclinic);

        buttonname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Log.d("3", clinicName);
                intent = new Intent(MainActivity.this, ClinicPage.class);
                intent.putExtra("main Clinic Name", clinicName);
                intent.putExtra("main Clinic ID",currentClinicID);
                startActivity(intent);
            }
        });




        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                currentClinicID = user.getClinicID();
                if(currentClinicID!=null){
                    contraint.setVisibility(View.VISIBLE);
                }
                clinicName = user.getCurrentClinic();
                clinic.setText(clinicName);
                currentQueueNumber = user.getCurrentQueue();
                clinic.setText(clinicName);
                currentQueue.setText("Current queue number: " + String.valueOf(currentQueueNumber));
                timing.setText(String.valueOf(currentQueueNumber*10)+"Mins More");



            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("mainActivity", "db error ");
            }
        };
        currentUser.addListenerForSingleValueEvent(userListener);



        View clinic = findViewById(R.id.ClinicAdminPage);
        clinic.setVisibility(View.GONE);
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
        startActivity(new Intent(getApplicationContext(),NearestClinic.class));
        finish();
    }
    public void nearestPharmacy (View view) {
        startActivity(new Intent(getApplicationContext(),NearestPharmacy.class));
        finish();
    }

//    public void ClinicAdminPage(View view) {
//        startActivity(new Intent(getApplicationContext(), Clinic_admin_page.class));
//        finish();
//    }






    public void currentAppointment (View view){

    }


}
