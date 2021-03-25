package com.example.loginapp;

import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

public class MapAdapter {
    private static GoogleMap gmap;
    private static List<Marker> markers = new ArrayList<>();
    private final ArrayList<Clinic> CLINICDATA = ClinicAdapter.getFirebasedata();

    public MapAdapter(){
        this.gmap=null;
    }

    public void setGmap(GoogleMap gmap){
        this.gmap = gmap;
    }

    public GoogleMap getGmap(GoogleMap mMap){
        setGmap(mMap);
        LatLng SGLatLng = new LatLng(1.3521,103.8198);
        float zoom = 10;

        try{
            MarkerOptions markerOptions = new MarkerOptions();

            for(Clinic fb: CLINICDATA){
                LatLng Clinic = new LatLng(fb.getLatitude(), fb.getLongitude());
                markerOptions.position(Clinic);
                markerOptions.title(fb.getClinicName());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                Marker m = gmap.addMarker(markerOptions);
                int position = CLINICDATA.indexOf(fb);
                Object DATA = new Gson().toJson(CLINICDATA.get(position));
                m.setTag(DATA + "|"+position);
                gmap.moveCamera(CameraUpdateFactory.newLatLng(Clinic));
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(SGLatLng,zoom));
                Log.d("tag","Adding Marker");
            }
        }catch(Exception e){
            Log.d("tag", "Error in Data \n" + e);
        }

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
                LatLng locationClinic = new LatLng(fb.getLatitude(),fb.getLongitude());
                markerOptions.position(locationClinic);
                markerOptions.title(fb.getClinicName());
                Marker m = gmap.addMarker(markerOptions.visible(false));
                int position = CLINICDATA.indexOf(fb);
                Object DATA = new Gson().toJson(CLINICDATA.get(position));
                m.setTag(DATA +"|"+ position);
                markers.add(m);
            }

        }catch(Exception e) {
            Log.d("tag", "ERROR In DATA: \n" + e);
        }

        return gmap;
    }

    public void revealMarkers(GoogleMap mMap , LatLng LL){
        for(int i=0;i<markers.size();i++){
            if(SphericalUtil.computeDistanceBetween(LL,markers.get(i).getPosition())<10000){
                markers.get(i).setVisible(true);
            }
        }
    }

    public GoogleMap getClinicLocation(GoogleMap mMap, Clinic clinicDetails){
        setGmap(mMap);
        try{
            LatLng clinic = new LatLng(clinicDetails.getLatitude(),clinicDetails.getLongitude());
            gmap.addMarker(new MarkerOptions().position(clinic).title(clinicDetails.getClinicName()));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(clinic));
            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(clinic,17));
        }catch (Exception e){
            Log.d("tag", "Error in data: \n"+e);
        }
        return gmap;
    }

    public boolean plotSearchMarkers (String Query){
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
