package com.example.loginapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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


import java.util.ArrayList;

public class ListofClinics extends AppCompatActivity {


    ListView listView;
    //EditText SearchFilter;
    ClinicController ClinicController;
    String newtext;
    //private ArrayAdapter arrayAdapter;
    ArrayList<Clinic> Clinic=new ArrayList<Clinic>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference clinicRef = db.collection("clinic");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);




        //initialize the views
        listView = (ListView) findViewById(R.id.List_view_users);
        listView.setEmptyView(findViewById(R.id.empty_subtitle_text));





        //TODO link with the rest

        clinicRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ClinicList : task.getResult()) {
                                //Log.d("Clinic Names","Clinic Names" + ClinicList.getString("Clinic Name"));
                                String clinicID=ClinicList.getId();
                                String clinicName = ClinicList.getString("Clinic Name");
                                //Log.d("TAG", clinicID);
                                Clinic.add(new Clinic(clinicID,clinicName));

                            }
                            ClinicController=new ClinicController(ListofClinics.this,Clinic);
                            listView.setAdapter(ClinicController);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                                    //Toast.makeText(ListofClinics.this, "clicked item"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ListofClinics.this,ClinicPage.class);
                                    intent.putExtra("Clinic Name",ClinicController.getItem(i).getClinicName());
                                    intent.putExtra("Clinic ID",ClinicController.getItem(i).getClinicID());
                                    Log.d("intent", String.valueOf(intent.getStringExtra("Clinic Name")));
                                    Log.d("intent", String.valueOf(intent.getStringExtra("Clinic ID")));
                                    //TODO send intents for image, phone and address
                                    //TODO and your queue number, current queue number
                                    startActivity(intent);
                                }
                            });
                        } else {
                            Log.d("fetch clinic error", "Error getting documents: ", task.getException());
                        }
                    }
                });




        // Start listing users from the beginning, 1000 at a time.




        //TODO Filter location : Default (nearest 10), by location.....


        //TODO REMINDER TO SET UP THE SEARCH FUNCTION

//pop up message when an item on the list is clicked
        //to be changed such that clicking on item redirects user to page containing info on clinic and queue



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

                ClinicController.getFilter().filter(newText);


                return true;
            }
        });


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.searchView) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

