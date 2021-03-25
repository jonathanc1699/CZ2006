package com.example.loginapp.Control;



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


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.loginapp.Entity.Pharmacy;
import com.example.loginapp.R;


import java.util.ArrayList;

//TODO
//load data: arraylist<admin>
//save data(adminlist)

public class PharmacyController extends ArrayAdapter<Pharmacy> implements Filterable {


    ArrayList<Pharmacy> PharmaciesTotal;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference PharmacyRef = db.collection("pharmacy");
    public static ArrayList<Pharmacy> FIREBASEDATA;
    ArrayList<Pharmacy> Pharmaciesfiltered;
    Context context;
    LayoutInflater inflater;



    public PharmacyController(Activity context, ArrayList<Pharmacy> Pharmacies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, Pharmacies);


        Pharmaciesfiltered = Pharmacies;
        Log.d("Pharmacyfiltered", String.valueOf((Pharmaciesfiltered.size())));
        PharmaciesTotal = Pharmacies;
        Log.d("PharmacyTotal", String.valueOf((Pharmaciesfiltered.size())));
        inflater = LayoutInflater.from(context);




    }







    @Override
    public int getCount() {
        return Pharmaciesfiltered.size();
    }

    @Override
    public Pharmacy getItem(int position) {
        return Pharmaciesfiltered.get(position);
    }

    @Override
    public long getItemId(int position) {

        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (PharmaciesTotal == null)
        {
            itemID = position;
        }
        else
        {
            itemID = PharmaciesTotal.indexOf(Pharmaciesfiltered.get(position));
        }
        return itemID;
    }



    @Override
    public View getView (final int position,
                         View convertView,
                         ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout_in_list,parent, false);
        }

        ((TextView) convertView.findViewById(R.id.textView_userName)).setText(getItem(position).getPharmacy_name());

        return convertView;

    }










    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList<Pharmacy> results = new ArrayList<Pharmacy>();
                if (PharmaciesTotal == null) {
                    PharmaciesTotal = new ArrayList<Pharmacy>(Pharmaciesfiltered); // saves the original data in mOriginalValues
                }
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = PharmaciesTotal.size();
                    filterResults.values = PharmaciesTotal;

                }else{

                    String searchStr = constraint.toString().toLowerCase();

                    for(Pharmacy Pharmacy:PharmaciesTotal){
                        if(Pharmacy.getPharmacy_name()!=null && Pharmacy.getPharmacy_name().toLowerCase().contains(searchStr)){
                            results.add(Pharmacy);

                        }
                    }
                    filterResults.count = results.size();
                    filterResults.values = results;


                }

                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Pharmaciesfiltered = (ArrayList<Pharmacy>) results.values;

                notifyDataSetChanged();

            }
        };
        return filter;
    }
    public static ArrayList<Pharmacy> passMeAllData(){ return FIREBASEDATA;}
/*public  ArrayList<Pharmacy> getAllDocs(){
        PharmacyRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Pharmacy> PharmacyDATA = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        PharmacyDATA = (ArrayList<Pharmacy>) document.get("Pharmacy");
                    }
                }
                return PharmacyDATA;
            }
        });
}*/

}



