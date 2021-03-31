package com.example.loginapp.Control;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.loginapp.Boundary.MapsActivity;
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
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MapAdapter {
    private static GoogleMap gmap;
    private static List<Marker> markers = new ArrayList<>();
    //private final ArrayList<Clinic> CLINICDATA = new ArrayList<>();
    //private ClinicAdapter clinicAdapter = new ClinicAdapter();
    final ArrayList<Clinic> CLINICDATA = new ArrayList<>();
    private ArrayList<DistanceClinicToMe> DistToMe = new ArrayList<>();
    public MapAdapter(){
        this.gmap=null;
       // this.clinicAdapter.setClinicData();
       // this.CLINICDATA = clinicAdapter.getClinicData();

    }

    public void setGmap(GoogleMap gmap){
        this.gmap = gmap;
    }

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
                                //markerOptions.snippet(fb.getClinicID());
                                markerOptions.title(fb.getClinicName());
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                Marker m = gmap.addMarker(markerOptions);
                                //int position = CLINICDATA.indexOf(fb);
                                //Object DATA = new Gson().toJson(CLINICDATA.get(position));
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



        /*try{
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng RafflesClinic = new LatLng(1.284349546,103.8510725);
            gmap.addMarker(new MarkerOptions().position(RafflesClinic).title("OC Medical Raffles Place"));
            System.out.println(CLINICDATA);

            for(Clinic fb: CLINICDATA){
                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                Log.d("tag","Current clinic's location is "+Clinic);
                markerOptions.position(Clinic);
                markerOptions.title(fb.getClinicName());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                Marker m = gmap.addMarker(markerOptions);
                int position = CLINICDATA.indexOf(fb);
                Object DATA = new Gson().toJson(CLINICDATA.get(position));
                m.setTag(DATA + "|"+position);
                gmap.moveCamera(CameraUpdateFactory.newLatLng(Clinic));
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SGLatLng,zoom));
                markers.add(m);
                Log.d("tag","Adding Marker");
            }
        }catch(Exception e){
            Log.d("tag", "Error in Data \n" + e);
        }*/
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SGLatLng,zoom));
        return gmap;
    }

    /**
     * Get Map however when User have enabled Phone GPS function
     * this method will be called instead of the default getMap
     * @param mMap
     */
    public GoogleMap getGmapWithGPS(GoogleMap mMap){
        setGmap(mMap);
        try{
            MarkerOptions markerOptions = new MarkerOptions();

            for (Clinic fb : CLINICDATA) {
                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                Log.d("tag", "Current clinic's location is " + Clinic);
                markerOptions.position(Clinic);
                //markerOptions.snippet(fb.getClinicID());
                markerOptions.title(fb.getClinicName());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                Marker m = gmap.addMarker(markerOptions);
                m.setVisible(false);
                //int position = CLINICDATA.indexOf(fb);
                //Object DATA = new Gson().toJson(CLINICDATA.get(position));
                m.setTag(fb.getClinicID());
                markers.add(m);
            }

        }catch(Exception e) {
            Log.d("tag", "ERROR In DATA: \n" + e);
        }

        return gmap;
    }

    public void revealMarkers(GoogleMap mMap , LatLng LL){
        Log.d("tag", "Markers"+ markers);
        for(int i=0;i<markers.size();i++){
            if(SphericalUtil.computeDistanceBetween(LL,markers.get(i).getPosition())<5000){
                markers.get(i).setVisible(true);
            }
        }
    }

    public void findnearestclinic(GoogleMap mMap, LatLng LL){
        Log.d("tag","Finding nearest clinic..");
        for(int i=0;i<markers.size();i++){
            DistToMe.add(new DistanceClinicToMe(markers.get(i).getTitle(),SphericalUtil.computeDistanceBetween(LL,markers.get(i).getPosition())));
        } Collections.sort(DistToMe, new Comparator<DistanceClinicToMe>() {
            @Override
            public int compare(DistanceClinicToMe o1, DistanceClinicToMe o2) {
                if(o1.Distance == o2.Distance)
                return 0;
                return o1.Distance < o2.Distance ? -1:1;
            }
        });
        Log.d("tag","Nearest clinic is "+DistToMe.get(0).getClinicName().toString());
        for(int i=0;i<markers.size();i++){
            if(markers.get(i).getTitle().equals(DistToMe.get(0).getClinicName())){
                markers.get(i).setVisible(true);
                markers.get(i).showInfoWindow();
                Log.d("tag","nearest clnic marker set to visible");
            }
        }
    }


    /*public boolean plotSearchMarkers (String Query){
            if(Query.isEmpty()){
                return false;
            }

            boolean plot = false;
            boolean check;
            boolean existClinicName;
            int postalCode =-1;

            check = isMyQueryValid(Query);

            if(check==true){
                postalCode = Integer.parseInt(Query);
            }

            CharSequence subQuery = Query.toLowerCase();

            MarkerOptions markerOptions= new MarkerOptions();

            for (Clinic fb: CLINICDATA){
                LatLng clinicLocation = new LatLng(fb.getLatitude(),fb.getLongitude());
                existClinicName = fb.getClinicName().toLowerCase().contains(subQuery);

                if(existClinicName ==true){
                    markerOptions.position(clinicLocation);
                    markerOptions.title(fb.getClinicName());
                    Marker m = gmap.addMarker(markerOptions);
                    int position = CLINICDATA.indexOf(fb);
                    Object DATA = new Gson().toJson(CLINICDATA.get(position));
                    m.setTag(DATA+"|"+position);
                    plot = true;
                }

                if (fb.getPostal()== postalCode){
                    markerOptions.position(clinicLocation);
                    markerOptions.title(fb.getClinicName());
                    gmap.addMarker(markerOptions);
                    Marker m = gmap.addMarker(markerOptions);
                    int position = CLINICDATA.indexOf(fb);
                    Object DATA = new Gson().toJson(CLINICDATA.get(position));
                    m.setTag(DATA+"|"+position);
                    plot = true;
                }
            }
            return plot;
        }*/
    public static Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }

    private static boolean isMyQueryValid(String str){
        if(str == null){
            return false;
        }
        int length = str.length();
        if (length == 0){
            return false;
        }
        int i=0;
        if(str.charAt(0)=='-'){
            if(length==1){
                return false;
            } i=1;
        }
        for(;i<length;i++){
            char c = str.charAt(i);
            if (c<'0'||c>'9'){
                return false;
            }
        }
        return true;
    }




}