package com.example.loginapp.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.Entity.Clinic;
import com.example.loginapp.Entity.DistanceClinicToMe;
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

public class MapAdapter {
    private static GoogleMap gmap;
    private static List<Marker> markers = new ArrayList<>();
    final ArrayList<Clinic> CLINICDATA = new ArrayList<>();
    private static ArrayList<DistanceClinicToMe> DistToMe;
    public MapAdapter(){
        this.gmap=null;
    }

    //set method for map
    public void setGmap(GoogleMap gmap){
        this.gmap = gmap;
    }

    //get method for map
    //main method that gets all clinic info from the database and adds all clinics as markers to the app
    public GoogleMap getGmap(GoogleMap mMap){
        setGmap(mMap);
        LatLng SGLatLng = new LatLng(1.3521,103.8198);
        float zoom = 10;

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference clinicRef = db.collection("clinic");
        clinicRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("fb","Successfully retrieved documents from firestore");
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("fb", document.getId() + " => "+document.getData());
                        try{
                            String clinicName = document.getString("Clinic Name");
                            String clinicID = document.getId();
                           Double Latitude = document.getDouble("Latitude");
                            Double Longitude = document.getDouble("Longitude");

                            Log.d("fb","Clinic being added: "+ clinicName);
                            if (clinicName!= null){
                            CLINICDATA.add(new Clinic(clinicID,clinicName,Latitude,Longitude));}
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }Log.d("fb","Final Clinic List" + CLINICDATA);
                    try{
                        MarkerOptions markerOptions = new MarkerOptions();
                        Log.d("fb","first element in list: "+ CLINICDATA.get(0).toString());

                        for(Clinic fb: CLINICDATA){
                                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                                Log.d("tag", "Current clinic's location is " + Clinic);
                                markerOptions.position(Clinic);
                                markerOptions.title(fb.getClinicName());
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                Marker m = gmap.addMarker(markerOptions);
                                m.setTag(fb.getClinicID());
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

            for (Clinic fb : CLINICDATA) {
                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                Log.d("tag", "Current clinic's location is " + Clinic);
                markerOptions.position(Clinic);
                markerOptions.title(fb.getClinicName());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                Marker m = gmap.addMarker(markerOptions);
                m.setVisible(false);
                m.setTag(fb.getClinicID());
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


    //creates a new array list (DistToMe) that stores the distance of every clinic to the user,sorts it by distance and reveals the marker of the clinic nearest to the user
    public void findnearestclinic(GoogleMap mMap, LatLng LL) throws InterruptedException {
        DistToMe = new ArrayList<>();
        Log.d("tag","Finding nearest clinic..");
        for(int i=0;i<markers.size();i++){
            if(markers.get(i).getTag() != null) {
                DistToMe.add(new DistanceClinicToMe((String) markers.get(i).getTag(), SphericalUtil.computeDistanceBetween(LL, markers.get(i).getPosition()), markers.get(i).getTitle()));
            }
        }
        Log.d("tag","Nearest clinic is "+DistToMe.get(0).getClinicName()+ " "+ DistToMe.get(0).getDistance()+" "+DistToMe.get(0).getClinicID());
        Collections.sort(DistToMe, new Comparator<DistanceClinicToMe>() {
            @Override
            public int compare(DistanceClinicToMe o1, DistanceClinicToMe o2) {
                if(o1.getDistance() == o2.getDistance())
                return 0;
                return o1.getDistance() < o2.getDistance() ? -1:1;

            }
        });
        Log.d("tag","Nearest clinic is "+DistToMe.get(0).getClinicName()+ " "+ DistToMe.get(0).getDistance()+" "+DistToMe.get(0).getClinicID());
        for(int i=0;i<markers.size();i++){
            try{
            if( markers.get(i).getTag().equals(DistToMe.get(0).getClinicID())){
                markers.get(i).setVisible(true);
                markers.get(i).showInfoWindow();
                Log.d("tag","nearest clinic marker set to visible");
            }}catch(NullPointerException e){
                Log.d("e","Error: "+e);
            }
        }
    }
}