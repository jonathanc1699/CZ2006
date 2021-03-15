package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QueueController extends AppCompatActivity {

// upon registration, default for queue and current clinic are 0 and null respectively
// queueController is used to modify the values in firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //should be another way to ensure its the same current user thats logged in
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    // opens up the current user's database reference
        final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());
        User user;

public void TakeQueueNumber(Clinic currentClinic) {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DB error", "Failed to load data properly", databaseError.toException());
            }


        };
         currentUser.addValueEventListener(userListener);
         String userCurrentClinic = user.getCurrentClinic();
         int userCurrentQueue = user.getCurrentQueue();

         if (userCurrentQueue == 0 && userCurrentClinic == null){
             // need a currentClinic object in clinic page from Clinic class
             user.setCurrentQueue(currentClinic.getClinicCurrentQ()+1);
             user.setCurrentClinic(currentClinic.getClinicName());
             // current user's queue number and clinic would be set to the selected one after this
         }


    }
}








