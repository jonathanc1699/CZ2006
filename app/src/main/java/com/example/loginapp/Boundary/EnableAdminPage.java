package com.example.loginapp.Boundary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.loginapp.Control.AdminController;
import com.example.loginapp.Control.EnableDeletedUser;
import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
/**
 * This class is used by an admin to enable a user so he can use the SickGoWhere app
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */


public class EnableAdminPage extends AppCompatActivity {
    ArrayList<com.example.loginapp.Entity.User> User=new ArrayList<com.example.loginapp.Entity.User>();

    AdminController mAdminController;
    ListView listView;
    FirebaseAuth fAuth=FirebaseAuth.getInstance();
    String newtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //initialize the views
        listView = (ListView) findViewById(R.id.List_view_users);
        listView.setEmptyView(findViewById(R.id.empty_subtitle_text));



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Users");
        //get list of users, making sure that they aren't already disabled, an admin, or a clinic admin
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.getKey();
                    Log.d("TAG", uid);
                    String email=ds.child("email").getValue(String.class);
                    String name=ds.child("fullName").getValue(String.class);
                    Boolean isDisabled = ds.child("disabled").getValue(Boolean.class);
                    Boolean isAdmin = ds.child("admin").getValue(Boolean.class);
                    if(isDisabled==null){
                        isDisabled = ds.child("Disabled").getValue(Boolean.class);
                    }
                    if(isAdmin==null){
                        isAdmin = ds.child("Admin").getValue(Boolean.class);
                    }

                    if(isAdmin==false && isDisabled==true){
                        User.add(new User(name,email,0, "nil",uid,true,false,false,"nil","nil"));
                    }

                }

                mAdminController = new AdminController(EnableAdminPage.this,User);
                listView.setAdapter(mAdminController);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                        showEnableDialog(mAdminController.getItemId(position));

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.searchView);

        SearchView searchView = (SearchView) menuItem.getActionView();

        MenuItem alph = menu.findItem(R.id.arrangebyalphabetical);
        alph.setVisible(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newtext=newText;

                Log.e("Main"," data search"+newText);

                mAdminController.getFilter().filter(newText);



                return true;
            }
        });


        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


        if(id == R.id.searchView){

            return true;
        }
        else{
            Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
            startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * This function is used to shows a confirmation dialog to ask admin if he really wants to enable user
     * if yes , system will find user in database and enable the account
     * An email notification will be sent to user
     * @param position index of user to be deleted
     */
    private void showEnableDialog(final long position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable User?");
        //finds user in database and enables account
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //enable user and refresh page
                DatabaseReference UserToUpdate = FirebaseDatabase.getInstance().getReference("Users").child((User.get(((int)position))).getUserId());
                HashMap<String, Object> map = new HashMap<>();
                map.put("disabled", false);




                String username =User.get(((int)position)).getFullName();
                Log.d("username", username);
                String useremail=User.get(((int)position)).getEmail();
                Log.d("email", useremail);
                EnableDeletedUser enableDeletedUser = new EnableDeletedUser();
                enableDeletedUser.sendEnableEmail( useremail, username);
                UserToUpdate.updateChildren(map);
                mAdminController.remove(User.get((int) position));
                mAdminController.getFilter().filter(newtext);



            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     *  Return back to Admin homepage
     */
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), mainactivityAdmin.class);
        startActivityForResult(myIntent, 0);
        super.onBackPressed();
    }


}
