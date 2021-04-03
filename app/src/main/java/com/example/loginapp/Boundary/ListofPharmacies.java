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

import com.example.loginapp.Control.PharmacyPage;
import com.example.loginapp.Control.PharmacyController;
import com.example.loginapp.Control.PharmacyPage;
import com.example.loginapp.Entity.Pharmacy;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Collections;

public class ListofPharmacies extends AppCompatActivity {


    ListView listView;
    //EditText SearchFilter;
    com.example.loginapp.Control.PharmacyController PharmacyController;
    String newtext;
    //private ArrayAdapter arrayAdapter;
    ArrayList<com.example.loginapp.Entity.Pharmacy> Pharmacy=new ArrayList<Pharmacy>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference PharmacyRef = db.collection("pharmacy");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        //initialize the views
        listView = (ListView) findViewById(R.id.List_view_users);
        listView.setEmptyView(findViewById(R.id.empty_subtitle_text));





        //TODO link with the rest

        PharmacyRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot PharmacyList : task.getResult()) {
                                //Log.d("Pharmacy Names","Pharmacy Names" + PharmacyList.getString("Pharmacy Name"));
                                String PharmacyID=PharmacyList.getId();
                                String PharmacyName = PharmacyList.getString("pharmacy_name");

                                //Log.d("TAG", PharmacyID);



                                if(PharmacyName!=null){
                                    Pharmacy.add(new Pharmacy(0, 0, PharmacyID,PharmacyName, null));
                                }

                            }
                            PharmacyController=new PharmacyController(ListofPharmacies.this,Pharmacy);
                            listView.setAdapter(PharmacyController);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                                    //Toast.makeText(ListofPharmacys.this, "clicked item"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ListofPharmacies.this, PharmacyPage.class);
                                    intent.putExtra("Pharmacy Name",PharmacyController.getItem(i).getPharmacy_name());
                                    intent.putExtra("Pharmacy ID",PharmacyController.getItem(i).getPharmacy_ID());
                                    Log.d("intent", String.valueOf(intent.getStringExtra("Pharmacy Name")));
                                    Log.d("intent", String.valueOf(intent.getStringExtra("Pharmacy ID")));
                                    //TODO send intents for image, phone and address
                                    //TODO and your queue number, current queue number
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d("fetch Pharmacy error", "Error getting documents: ", task.getException());
                        }
                    }
                });




        // Start listing users from the beginning, 1000 at a time.




        //TODO Filter location : Default (nearest 10), by location.....


        //TODO REMINDER TO SET UP THE SEARCH FUNCTION

//pop up message when an item on the list is clicked
        //to be changed such that clicking on item redirects user to page containing info on Pharmacy and queue



    }





    //
//        String[] username = {"Russell","Jon","Xuanhui"};
//        String[] useremail = {"Russell@gmail.com","Jon@gmail.com","Xuanhui@gmail.com"};
//
//        for(int i=0;i<username.length;i++){
//            User.add(new User(useremail[i],username[i]));
//        }
    //




    //From this

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

                PharmacyController.getFilter().filter(newText);


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
                Collections.sort(Pharmacy, (p1, p2) -> p1.getPharmacy_name().compareTo(p2.getPharmacy_name()));
                //PharmacyController.clear();
                PharmacyController.addAll(Pharmacy);
                PharmacyController.notifyDataSetChanged();

                return true;
            case R.id.arrangedist:

                return true;




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MapsActivityPharmacy.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




}


