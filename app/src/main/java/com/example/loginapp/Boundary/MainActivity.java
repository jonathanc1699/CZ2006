package com.example.loginapp.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginapp.Control.ChatbotActivity;
import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View deleteuserlist = findViewById(R.id.adminuserlist);
        deleteuserlist.setVisibility(View.GONE);
        View enableuserlist = findViewById(R.id.enableuserlist);
        enableuserlist.setVisibility(View.GONE);


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
