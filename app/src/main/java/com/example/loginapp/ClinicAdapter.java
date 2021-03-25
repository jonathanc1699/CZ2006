package com.example.loginapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ClinicAdapter {
    public static ArrayList<Clinic> getFirebasedata(){
        final ArrayList<Clinic> ClinicData = new ArrayList<>();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference clinicRef = db.collection("clinic");
        clinicRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("fb","Successfully retreived documents from firestore");
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("fb", document.getId() + " => "+document.getData());
                        try{
                            String clinicName = document.getString("Clinic Name");
                            String clinicID = document.getId();
                            Long Latitude = document.getLong("Latitude");
                            Long Longitude = document.getLong("Longitude");
                            Log.d("fb","Clinic being added: "+ clinicName);
                            ClinicData.add(new Clinic(clinicID,clinicName,Latitude,Longitude));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }Log.d("fb","Final Clinic List" + ClinicData);
                }else{
                    Log.d("fb","Error getting documents: ", task.getException());
                }
            }
        });
        return ClinicData;
    }
}
