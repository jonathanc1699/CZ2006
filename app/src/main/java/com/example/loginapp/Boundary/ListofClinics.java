package com.example.loginapp.Boundary;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.loginapp.Control.ClinicController;
import com.example.loginapp.Control.ClinicPage;
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to display all the clinics in singapore in a list view
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class ListofClinics extends AppCompatActivity {


    ListView listView;
    com.example.loginapp.Control.ClinicController ClinicController;
    String newtext;
    ArrayList<com.example.loginapp.Entity.Clinic> Clinic=new ArrayList<Clinic>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference clinicRef = db.collection("clinic");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //initialize the views
        listView = (ListView) findViewById(R.id.List_view_users);
        listView.setEmptyView(findViewById(R.id.empty_subtitle_text));

        //get all clinic details
        clinicRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ClinicList : task.getResult()) {
                                String clinicID=ClinicList.getId();
                                String clinicName = ClinicList.getString("Clinic Name");

                                if(clinicName!=null){
                                    Clinic.add(new Clinic(clinicID,clinicName));
                                }

                            }
                            ClinicController=new ClinicController(ListofClinics.this,Clinic);
                            listView.setAdapter(ClinicController);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                                    Intent intent = new Intent(ListofClinics.this, ClinicPage.class);
                                    intent.putExtra("Clinic Name",ClinicController.getItem(i).getClinicName());
                                    intent.putExtra("Clinic ID",ClinicController.getItem(i).getClinicID());
                                    Log.d("intent", String.valueOf(intent.getStringExtra("Clinic Name")));
                                    Log.d("intent", String.valueOf(intent.getStringExtra("Clinic ID")));
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newtext = newText;

                Log.e("Main", " data search" + newText);

                ClinicController.getFilter().filter(newText);


                return true;
            }
        });


        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.searchView:

                return true;
            case R.id.arrangebyalphabetical:
                Collections.sort(Clinic, (p1, p2) -> p1.getClinicName().compareTo(p2.getClinicName()));
                //ClinicController.clear();
                ClinicController.addAll(Clinic);
                ClinicController.notifyDataSetChanged();

                return true;
            case R.id.arrangedist:

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Return back to Clinic's  Maps view
     */
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }






}

