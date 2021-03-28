
package com.example.loginapp.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.Entity.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
        import java.util.Map;

public class UserQueueController {

    // upon registration, default for queue and current clinic are 0 and null respectively
    // queueController is used to modify the values in firebase
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //should be another way to ensure its the same current user thats logged in
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    // opens up the current user's database reference
    final DatabaseReference currentUser = databaseReference.child(firebaseUser.getUid());



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    User user;
    String userID;
    String currentClinic;
    int currentQNo;
    String fullName;


    // update current user clinic and queue to firebase
    public void assignQToUser(int latestclinicq, String clinicName, String clinicID) {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userID = user.getUserId();
                fullName = user.getFullName();

                user.setCurrentClinic(clinicName);
                user.setCurrentQueue(latestclinicq);
                currentClinic=user.getCurrentClinic();
                currentQNo = user.getCurrentQueue();
                Map<String, Object> userValues = user.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(user.getUserId(), userValues);
                databaseReference.updateChildren(childUpdates);
                Log.d("currentClinic", fullName + "> "+currentClinic);
                Log.d("currentQNo", fullName + "> "+ currentQNo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TakeQNUM error", "Failed to load data properly", databaseError.toException());
            }

        };
        currentUser.addListenerForSingleValueEvent(userListener);
    }

    //TODO add this to code
    // update current user clinic and queue to firebase when user cancel booking
    public void cancelQUser(String clinicName) {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                userID = user.getUserId();
                fullName = user.getFullName();
                user.setCurrentClinic("nil");
                user.setCurrentQueue(0);

                currentClinic=user.getCurrentClinic();
                currentQNo = user.getCurrentQueue();

                Map<String, Object> userValues = user.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(user.getUserId(), userValues);
                databaseReference.updateChildren(childUpdates);

                Log.d("currentClinic", fullName + "> "+currentClinic);
                Log.d("currentQNo", fullName + "> "+ currentQNo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("cancelQ error", "Failed to load data properly", databaseError.toException());
            }

        };
        currentUser.addListenerForSingleValueEvent(userListener);
    }

}








