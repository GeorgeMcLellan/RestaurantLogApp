package com.development.georgemcl.restaurantlogapp.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.georgemcl.restaurantlogapp.Database.RestaurantDbHelper;
import com.development.georgemcl.restaurantlogapp.Models.Restaurant;
import com.development.georgemcl.restaurantlogapp.R;
import com.development.georgemcl.restaurantlogapp.RecyclerViewAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFrag extends Fragment{
    private static final String TAG = "RestaurantListFrag";

    private LinkedList<Restaurant> mRestaurantList;

    private View mView;
    private RestaurantDbHelper restaurantDb;

    public RestaurantListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        Log.d(TAG, "Fragment Started");

        restaurantDb = new RestaurantDbHelper(getContext());


        createLinkedList();
        initRecyclerView();

        return mView;
    }

    private void createLinkedList() {
        Cursor cursor = restaurantDb.getAllData();
        mRestaurantList = new LinkedList<>();
        Restaurant currentRestaurant;
        while(cursor.moveToNext()) {
            currentRestaurant = new Restaurant();

            currentRestaurant.setId(cursor.getString(0));
            currentRestaurant.setName(cursor.getString(1));
            LatLng latlng = new LatLng(cursor.getDouble(2), cursor.getDouble(3));
            currentRestaurant.setLatLng(latlng);
            currentRestaurant.setCuisine(cursor.getString(4));
            currentRestaurant.setPriceLevel(cursor.getInt(5));
            currentRestaurant.setRating(cursor.getFloat(6));
            Log.d(TAG, "Adding restaurant " +currentRestaurant.getName());
            mRestaurantList.add(currentRestaurant);
        }

    }


    private void initRecyclerView(){
        RecyclerView recyclerView = mView.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mRestaurantList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }



    private void switchListForMapFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, new MapFragment()).commit();
    }
}
