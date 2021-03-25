package com.example.loginapp.Boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.loginapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleMap extends AppCompatActivity {
    //Initialize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    private static List<Marker> markers = new ArrayList<>();
    private com.google.android.gms.maps.GoogleMap mMap;
    //private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        //assign variable
        //supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);
       /* //initialize Places SDK
        Places.initialize(getApplicationContext(),"AIzaSyCQZB7Tr_xDM_9yDAvg5PNfYnhbJjtsnDc");
        //Create new Places client instance
        PlacesClient placesClient = Places.createClient(this);
        //Initialize autocompletesupportfragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(new LatLng(1.3521,103.8198), new LatLng(1.3540,10390)));
        autocompleteFragment.setCountries("SG");
        //specify types of places data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //Get info on selected place
                Log.i(TAG, "Place: "+place.getName()+", "+place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                //Error handling
                Log.i(TAG, "An error occurred: "+status);

            }
        });*/

        // Check permission
        if (ActivityCompat.checkSelfPermission(GoogleMap.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            //Call method
            getCurrentLocation();

        } else {
            //When permission denied
            //Request permission
            ActivityCompat.requestPermissions(GoogleMap.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        //initialize task location
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //When success
                if (location != null) {
                    //Sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {
                            mMap = googleMap;
                            if (ActivityCompat.checkSelfPermission(GoogleMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GoogleMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},44);
                                }
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            KmlLayer layer = null;
                            try{
                                layer = new KmlLayer(mMap, R.raw.chasclinicskml,getApplicationContext());
                                layer.addLayerToMap();
                                layer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                                    @Override
                                    public void onFeatureClick(Feature feature) {
                                        Log.i("KML", "Feature clicked: "+ feature.getProperty("descriptiongi"));
                                    }
                                });
                                KmlLayer layer2 = null;
                                layer2 = new KmlLayer(mMap, R.raw.pharmacylocationskml,getApplicationContext());
                                layer2.addLayerToMap();
                                layer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                                    @Override
                                    public void onFeatureClick(Feature feature) {
                                        Log.i("KML", "Feature clicked: "+ feature.getProperty("description"));
                                    }
                                });
                            } catch (IOException e){
                                e.printStackTrace();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                            //Initalize Lat Lng
                            LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
                            //Create marker options
                            MarkerOptions options = new MarkerOptions().position(latlng).title("I am here");
                            //Zoom map
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,15));
                            //add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //When permission granted
                //Call method
                getCurrentLocation();
            }
        }
    }

    public void revealMarkers (GoogleMap mMap, LatLng LL){
        for(int i =0; i<markers.size(); i++){
            if(SphericalUtil.computeDistanceBetween(LL, markers.get(i).getPosition())<2400){
                markers.get(i).setVisible(true);
            }

        }
    }

}