package com.example.loginapp.Boundary;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginapp.Control.ClinicPage;
import com.example.loginapp.Control.MapAdapter;
import com.example.loginapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapAdapter mController;
    private Button nearbyBtn;
    private Button nearestBtn;
    private  Button listviewBtn;
    private static int TIME_OUT = 1000*8;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "Creating map activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mController = new MapAdapter();

        nearbyBtn = (Button) findViewById(R.id.nearbyBtn);
        nearestBtn = (Button) findViewById(R.id.nearestbutton);
        listviewBtn = (Button) findViewById(R.id.listviewbutton);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //list button, when clicked, opens list view of clinics
        listviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListofClinics.class));
                finish();
            }
        });

        //nearest button, when clicked, shows nearest clinic to the user
        nearestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mMap.clear();
                    mMap = mController.getGmapWithGPS(mMap);
                    Location myLocation = mMap.getMyLocation();
                    LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLatLng).title("You are here"));
                    mController.findnearestclinic(mMap, myLatLng);
                    Log.d("tag", "marker placed");
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please enable GPS location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //nearby button, when clicked, shows clinics within 5km of user
        nearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mMap.clear();
                    mMap = mController.getGmapWithGPS(mMap);
                    Location myLocation = mMap.getMyLocation();
                    LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLatLng).title("You are here"));
                    mController.revealMarkers(mMap, myLatLng);
                    Log.d("tag", "markers placed");
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please enable GPS location", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    //Boundary level processes that occur when the map has loaded, gets user's current location and adds a marker to it, loading window for map
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Map is loading...");
        progressDialog.setTitle("Map View");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        mMap = googleMap;
        getGPSPermission();
        mMap.getUiSettings().setMapToolbarEnabled(false);
       mMap = mController.getGmap(mMap);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else{
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("tag", "My location is " + myLatLng);
                Marker m = mMap.addMarker(new MarkerOptions().position(myLatLng).title("You are here"));
                m.showInfoWindow();

            }
        });}
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            @Override
            public void run()
            {
                progressDialog.dismiss();
            }
        }, TIME_OUT);

        //when user clicks on the name of the clinic on the marker, it will direct them to clinic page of the selected clinic
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
                if (marker.getTitle() != "You are here"){
                    Intent intent = new Intent(getApplicationContext(),ClinicPage.class);
                    intent.putExtra("Clinic Name", marker.getTitle());
                    intent.putExtra("Clinic ID", (String) marker.getTag());
                    Log.d("intent", String.valueOf(intent.getStringExtra("Clinic Name")));
                    Log.d("intent", String.valueOf(intent.getStringExtra("Clinic ID")));
                    startActivity(intent);}
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
            Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            super.onBackPressed();
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setMessage("Are you sure you want to exit?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                        }
                    }).setNegativeButton("No", null).show();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getGPSPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "GPS permission not granted!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "User granted GPS permission!", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(getApplicationContext(),"User did not grant GPS permission!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}