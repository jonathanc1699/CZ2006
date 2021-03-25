package com.example.loginapp;



import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//TODO
//load data: arraylist<admin>
//save data(adminlist)

public class ClinicController extends ArrayAdapter<Clinic> implements Filterable {


    ArrayList<Clinic> ClinicsTotal;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference clinicRef = db.collection("clinic");
    public static ArrayList<Clinic> FIREBASEDATA;
    ArrayList<Clinic> Clinicsfiltered;
    Context context;
    LayoutInflater inflater;



    public ClinicController(Activity context, ArrayList<Clinic> Clinics) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, Clinics);


        Clinicsfiltered = Clinics;
        Log.d("Clinicfiltered", String.valueOf((Clinicsfiltered.size())));
        ClinicsTotal = Clinics;
        Log.d("ClinicTotal", String.valueOf((Clinicsfiltered.size())));
        inflater = LayoutInflater.from(context);




    }







    @Override
    public int getCount() {
        return Clinicsfiltered.size();
    }

    @Override
    public Clinic getItem(int position) {
        return Clinicsfiltered.get(position);
    }

    @Override
    public long getItemId(int position) {

        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (ClinicsTotal == null)
        {
            itemID = position;
        }
        else
        {
            itemID = ClinicsTotal.indexOf(Clinicsfiltered.get(position));
        }
        return itemID;
    }



    @Override
    public View getView (final int position,
                         View convertView,
                         ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.userlist_admin_page,parent, false);
        }

        ((TextView) convertView.findViewById(R.id.textView_userName)).setText(getItem(position).getClinicName());

        return convertView;

    }










    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList<Clinic> results = new ArrayList<Clinic>();
                if (ClinicsTotal == null) {
                    ClinicsTotal = new ArrayList<Clinic>(Clinicsfiltered); // saves the original data in mOriginalValues
                }
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = ClinicsTotal.size();
                    filterResults.values = ClinicsTotal;

                }else{

                    String searchStr = constraint.toString().toLowerCase();

                    for(Clinic Clinic:ClinicsTotal){
                        if(Clinic.getClinicName()!=null && Clinic.getClinicName().toLowerCase().contains(searchStr)){
                            results.add(Clinic);

                        }
                    }
                    filterResults.count = results.size();
                    filterResults.values = results;


                }

                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Clinicsfiltered = (ArrayList<Clinic>) results.values;

                notifyDataSetChanged();

            }
        };
        return filter;
    }
public static ArrayList<Clinic> passMeAllData(){ return FIREBASEDATA;}
/*public  ArrayList<Clinic> getAllDocs(){
        clinicRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Clinic> CLINICDATA = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CLINICDATA = (ArrayList<Clinic>) document.get("clinic");
                    }
                }
                return CLINICDATA;
            }
        });
}*/

}


