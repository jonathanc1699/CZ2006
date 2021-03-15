package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListofClinics extends AppCompatActivity {
    ListView listView;
    EditText SearchFilter;
    private ArrayAdapter arrayAdapter;

    //To read clinic database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_clinics);

        listView = (ListView)findViewById(R.id.listview);
        SearchFilter = (EditText)findViewById(R.id.searchFilter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fetch Clinic Names from Clinic Database and assign to List View Page

        ArrayList<String> arrayList=new ArrayList<>();
        clinicRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ClinicList : task.getResult()) {
                                Log.d("Clinic Names","Clinic Names" + ClinicList.getString("Clinic Name"));
                                //Log.d("fetch clinic works", ClinicList.getId() + " => " + ClinicList.getData());
                                arrayList.add(ClinicList.getString("Clinic Name"));
                                arrayAdapter=new ArrayAdapter(ListofClinics.this, android.R.layout.simple_list_item_1,arrayList);
                                listView.setAdapter(arrayAdapter);
                            }
                        } else {
                            Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                        }
                    }
                });


        //TODO Filter location : Default (nearest 10), by location.....
        //TODO REMINDER TO SET UP THE SEARCH FUNCTION

//pop up message when an item on the list is clicked
        //to be changed such that clicking on item redirects user to page containing info on clinic and queue
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Toast.makeText(ListofClinics.this, "clicked item"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListofClinics.this,ClinicPage.class);
                intent.putExtra("CLINIC_NAME", arrayList.get(i));
                //TODO send intents for image, phone and address
                //TODO and your queue number, current queue number
                startActivity(intent);
            }
        });

        SearchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (ListofClinics.this).arrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }*/
}