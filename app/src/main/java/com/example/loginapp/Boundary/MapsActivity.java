package com.example.loginapp.Boundary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.Control.ClinicAdapter;
import com.example.loginapp.Control.ClinicController;
import com.example.loginapp.Control.ClinicPage;
import com.example.loginapp.Control.MapAdapter;
import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapAdapter mController;
    private ClinicController clinicController;
    private PersistentSearchView persistentSearchView;
    private Button nearbyBtn;
    private ProgressBar progressBar;
    private Button nearestBtn;
    private boolean result;
    private static int TIME_OUT = 1000*10;
    ProgressDialog progressDialog;
    private FusedLocationProviderClient mFusedLocationClient;
    //private final ArrayList<Clinic> CLINICDATA = ClinicAdapter.getFirebasedata();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "Creating map activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mController = new MapAdapter();

        //persistentSearchView = (PersistentSearchView) findViewById(R.id.persistentSearchView);
        nearbyBtn = (Button) findViewById(R.id.nearbyBtn);
        nearestBtn = (Button) findViewById(R.id.nearestbutton);
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
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
        progressDialog = new ProgressDialog(MapsActivity.this);
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
                mMap.addMarker(new MarkerOptions().position(myLatLng).title("You are here"));

            }
        });}
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            @Override
            public void run()
            {
                progressDialog.dismiss();
            }
        }, TIME_OUT);





        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle() != "You are here"){
                Intent intent = new Intent(getApplicationContext(),ClinicPage.class);
                intent.putExtra("Clinic Name", marker.getTitle());
                intent.putExtra("Clinic ID", marker.getSnippet());
                Log.d("intent", String.valueOf(intent.getStringExtra("Clinic Name")));
                Log.d("intent", String.valueOf(intent.getStringExtra("Clinic ID")));
                startActivity(intent);}
                return false;
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
    public void onBackPressed() {
            Intent myIntent = new Intent(getApplicationContext(), NearestClinic.class);
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