package com.example.loginapp.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.Entity.DistanceClinicToMe;
import com.example.loginapp.Entity.Pharmacy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapAdapterPharmacy {
    private static GoogleMap gmap;
    private static List<Marker> markers = new ArrayList<>();
    private ArrayList<DistanceClinicToMe> DistToMe;
    final ArrayList<Pharmacy> PHARMACYDATA = new ArrayList<>();
    public MapAdapterPharmacy(){
        this.gmap=null;

    }

    //set method for map
    public void setGmap(GoogleMap gmap){
        this.gmap = gmap;
    }

    //get method for map
    //main method that gets all clinic info from the database and adds all pharmacies as markers to the app
    public GoogleMap getGmap(GoogleMap mMap){
        setGmap(mMap);
        LatLng SGLatLng = new LatLng(1.3521,103.8198);
        float zoom = 10;

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference pharmRef = db.collection("pharmacy");
        pharmRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("fb","Successfully retrieved documents from firestore");
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("fb", document.getId() + " => "+document.getData());
                        try{
                            String pharName = document.getString("pharmacy_name");
                            String pharID = document.getId();
                           Double Latitude = document.getDouble("Latitude");
                            Double Longitude = document.getDouble("Longitude");
                            String pharAddress = document.getString("pharmacy_addrress");

                            Log.d("fb","Pharmacy being added: "+ pharName);
                            if (pharName!= null){
                            PHARMACYDATA.add(new Pharmacy(Latitude,Longitude,pharID,pharName,pharAddress));}
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }Log.d("fb","Final Pharmacy List" + PHARMACYDATA);
                    try{
                        MarkerOptions markerOptions = new MarkerOptions();
                        Log.d("fb","first element in list: "+ PHARMACYDATA.get(0).toString());

                        for(Pharmacy fb: PHARMACYDATA){
                                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                                Log.d("tag", "Current clinic's location is " + Clinic);
                                markerOptions.position(Clinic);
                                markerOptions.title(fb.getPharmacy_name());
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                Marker m = gmap.addMarker(markerOptions);
                                m.setTag(fb.getPharmacy_ID());
                                gmap.moveCamera(CameraUpdateFactory.newLatLng(Clinic));
                            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SGLatLng, zoom));
                                markers.add(m);
                                Log.d("tag", "Adding Marker" + m.getTitle());
                        }
                    }catch(Exception e){
                        Log.d("tag", "Error in Data \n" + e);
                    }
                }else{
                    Log.d("fb","Error getting documents: ", task.getException());
                }
            }
        });
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SGLatLng,zoom));
        return gmap;
    }


    //a quicker method to add markers to map, markers are invisible because this method is called along with revealmarkers() or findnearestclinic()
    public GoogleMap getGmapWithGPS(GoogleMap mMap){
        setGmap(mMap);
        try{
            MarkerOptions markerOptions = new MarkerOptions();

            for (Pharmacy fb : PHARMACYDATA) {
                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                Log.d("tag", "Current clinic's location is " + Clinic);
                markerOptions.position(Clinic);
                markerOptions.title(fb.getPharmacy_name());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                Marker m = gmap.addMarker(markerOptions);
                m.setVisible(false);
                m.setTag(fb.getPharmacy_ID());
                markers.add(m);
            }

        }catch(Exception e) {
            Log.d("tag", "ERROR In DATA: \n" + e);
        }

        return gmap;
    }

    //reveals markers that are within 5km of the user
    public void revealMarkers(GoogleMap mMap , LatLng LL){
        Log.d("tag", "Markers"+ markers);
        for(int i=0;i<markers.size();i++){
            if(SphericalUtil.computeDistanceBetween(LL,markers.get(i).getPosition())<5000){
                markers.get(i).setVisible(true);
            }
        }
    }

 //creates a new array list (DistToMe) that stores the distance of every pharmacy to the user,sorts it by distance and reveals the marker of the pharmacy nearest to the user
    public void findnearestclinic(GoogleMap mMap, LatLng LL){
        Log.d("tag","Finding nearest pharmacy..");
        DistToMe=new ArrayList<>();
        for(int i=0;i<markers.size();i++){
            if(markers.get(i).getTag() != null){
            DistToMe.add(new DistanceClinicToMe((String) markers.get(i).getTag(),SphericalUtil.computeDistanceBetween(LL,markers.get(i).getPosition()),markers.get(i).getTitle()));
            }
        }
        Collections.sort(DistToMe, new Comparator<DistanceClinicToMe>() {
            @Override
            public int compare(DistanceClinicToMe o1, DistanceClinicToMe o2) {
                if(o1.getDistance() == o2.getDistance())
                    return 0;
                return o1.getDistance() < o2.getDistance() ? -1:1;
            }
        });
        Log.d("tag","Nearest pharmacy is "+DistToMe.get(0).getClinicName()+ " "+ DistToMe.get(0).getDistance()+" "+DistToMe.get(0).getClinicID());
        for(int i=0;i<markers.size();i++){
            try{
            if(markers.get(i).getTag().equals(DistToMe.get(0).getClinicID())){
                markers.get(i).setVisible(true);
                markers.get(i).showInfoWindow();
                Log.d("tag","nearest pharmacy marker set to visible");
            }}catch(NullPointerException e){
                Log.d("e","Error: "+e);
            }
        }
    }




}