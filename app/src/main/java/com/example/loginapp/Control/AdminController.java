package com.example.loginapp.Control;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;

import com.example.loginapp.Entity.User;
import com.example.loginapp.R;

import java.util.ArrayList;

//TODO
//load data: arraylist<admin>
//save data(adminlist)

public class AdminController extends ArrayAdapter<User> implements Filterable {


    ArrayList<User> UsersTotal;
    ArrayList<User> Usersfiltered;
    Context context;
    LayoutInflater inflater;



    public AdminController(Activity context, ArrayList<User> Users) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, Users);


        Usersfiltered = Users;
        UsersTotal = Users;
        inflater = LayoutInflater.from(context);




    }







    @Override
    public int getCount() {
        return Usersfiltered.size();
    }

    @Override
    public User getItem(int position) {
        return Usersfiltered.get(position);
    }

    @Override
    public long getItemId(int position) {

        int itemID;

        // orig will be null only if we haven't filtered yet:
        if (UsersTotal == null)
        {
            itemID = position;
        }
        else
        {
            itemID = UsersTotal.indexOf(Usersfiltered.get(position));
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

        ((TextView) convertView.findViewById(R.id.textView_userEmail)).setText(getItem(position).getEmail());
        ((TextView) convertView.findViewById(R.id.textView_userName)).setText(getItem(position).getFullName());

        return convertView;

    }










    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList<User> results = new ArrayList<User>();
                if (UsersTotal == null) {
                    UsersTotal = new ArrayList<User>(Usersfiltered); // saves the original data in mOriginalValues
                }
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = UsersTotal.size();
                    filterResults.values = UsersTotal;

                }else{

                    String searchStr = constraint.toString().toLowerCase();

                    for(User user:UsersTotal){
                        if(user.getFullName().toLowerCase().contains(searchStr) || user.getEmail().toLowerCase().contains(searchStr)){
                            results.add(user);

                        }
                    }
                    filterResults.count = results.size();
                    filterResults.values = results;


                }

                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                Usersfiltered = (ArrayList<User>) results.values;

                notifyDataSetChanged();

            }
        };
        return filter;
    }


}
