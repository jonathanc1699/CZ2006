package com.example.loginapp.Boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;
/**
 * This class is used to display the admin's main menu and to manage the users
 * such as delete user and enable user
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class mainactivityAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


    }

    /**
     * sign user out of the app
     * Redirect admin back to the Login class
     * @param view Login page
     */
    public void logout (View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    /**
     * Display all users that are not disabled in the database
     * Redirect admin to the disable user page
     * @param view disable- admin page
     */
    public void adminuserlist(View view) {
        startActivity(new Intent(getApplicationContext(), DisableAdminPage.class));
        finish();
    }


    /**
     * Display all users that are  disabled in the database
     * Redirect admin to the enable user page
     * @param view enable- admin page
     */
    public void enableuserlist(View view) {
        startActivity(new Intent(getApplicationContext(), EnableAdminPage.class));
        finish();
    }



}