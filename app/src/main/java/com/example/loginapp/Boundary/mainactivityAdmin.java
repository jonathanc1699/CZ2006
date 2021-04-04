package com.example.loginapp.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class mainactivityAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


    }

    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    public void adminuserlist(View view) {
        startActivity(new Intent(getApplicationContext(), DisableAdminPage.class));
        finish();
    }

    public void enableuserlist(View view) {
        startActivity(new Intent(getApplicationContext(), EnableAdminPage.class));
        finish();
    }



}