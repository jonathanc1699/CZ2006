package com.example.loginapp.Boundary;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.loginapp.Control.ClinicController;
import com.example.loginapp.Control.ClinicPage;
import com.example.loginapp.Control.MapAdapterPharmacy;
import com.example.loginapp.Control.PharmacyPage;
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
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;

public class MapsActivityPharmacy extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapAdapterPharmacy mController;
    private ClinicController clinicController;
    private PersistentSearchView persistentSearchView;
    private Button nearbyBtn;
    private Button nearestBtn;
    private Button listviewBtn;
    private ProgressBar progressBar;
    private static int TIME_OUT = 1000*5;
    private boolean result;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    //private final ArrayList<Clinic> CLINICDATA = ClinicAdapter.getFirebasedata();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "Creating map activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_pharmacy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mController = new MapAdapterPharmacy();

        //persistentSearchView = (PersistentSearchView) findViewById(R.id.persistentSearchView);
        nearbyBtn = (Button) findViewById(R.id.nearbyBtn1);
        nearestBtn = (Button) findViewById(R.id.nearestbutton1);
        listviewBtn = (Button) findViewById(R.id.listviewbutton1);
//        progressBar = findViewById(R.id.progressBar3);
//        progressBar.setVisibility(View.VISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 250);

        persistentSearchView.setSuggestionsDisabled(false);
        persistentSearchView.isDismissibleOnTouchOutside();
        persistentSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {
            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                mMap.clear();
                searchView.collapse(true);
                result = mController.plotSearchMarkers(query);

                if (result == false) {
                    Toast.makeText(getApplicationContext(), "No Results Found", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        listviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListofPharmacies.class));
                // insert on button click, start queueActivity
                finish();
            }
        });
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 12));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Please enable GPS location", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

//        AppCompatImageButton viewClinicBtn = (AppCompatImageButton) findViewById(R.id.viewClinicsBtn);
//        viewClinicBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View r) {
//                Log.d("TAG", "Clicked View Clinics Button");
//                Intent i = new Intent(MapsActivity.this, ListofClinics.class);
//                MapsActivity.this.startActivity(i);
//            }
//        });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        progressDialog = new ProgressDialog(MapsActivityPharmacy.this);
        progressDialog.setMessage("Map is loading...");
        progressDialog.setTitle("Map View");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        mMap = googleMap;
        getGPSPermission();
        mMap.getUiSettings().setMapToolbarEnabled(false);
       mMap = mController.getGmap(mMap);
        Toast.makeText(getApplicationContext(),"Map is being loaded...",Toast.LENGTH_LONG).show();
        //Location myLocation = mMap.getMyLocation();
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
                Log.d("tag", "My location is " + location);
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


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                marker.hideInfoWindow();
                if (marker.getTitle() != "You are here"){
                    Intent intent = new Intent(getApplicationContext(), PharmacyPage.class);
                    intent.putExtra("Pharmacy Name", marker.getTitle());
                    intent.putExtra("Pharmacy ID", (String) marker.getTag());
                    Log.d("intent", String.valueOf(intent.getStringExtra("Pharmacy Name")));
                    Log.d("intent", String.valueOf(intent.getStringExtra("Pharmacy ID")));
                    startActivity(intent);}
            }
        });


        //LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        //mController.revealMarkers(mMap, myLatLng);

        /*mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String markerInfo = (String) marker.getTag();
                String clinicObject = markerInfo.substring(0, markerInfo.indexOf("|"));

                String stringPosition = markerInfo.substring(markerInfo.lastIndexOf("|") + 1);
                int position = Integer.parseInt(stringPosition);

                JSONObject obj = null;
                try {
                    obj = new JSONObject(clinicObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(MapsActivity.this, ListofClinics.class);
                MapsActivity.this.startActivity(i);
            }
        });*/

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
            ActivityCompat.requestPermissions(MapsActivityPharmacy.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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